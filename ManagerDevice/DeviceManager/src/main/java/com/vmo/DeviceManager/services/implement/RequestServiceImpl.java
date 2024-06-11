package com.vmo.DeviceManager.services.implement;

import com.vmo.DeviceManager.exceptions.model.*;
import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.Request;
import com.vmo.DeviceManager.models.RequestDetail;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.dto.RequestDto;
import com.vmo.DeviceManager.models.enumEntity.EstatusDevice;
import com.vmo.DeviceManager.models.enumEntity.EstatusRequest;
import com.vmo.DeviceManager.models.event.RequestEvent;
import com.vmo.DeviceManager.repositories.DeviceRepository;
import com.vmo.DeviceManager.repositories.RequestDetailRepository;
import com.vmo.DeviceManager.repositories.RequestRepository;
import com.vmo.DeviceManager.repositories.UserRepository;
import com.vmo.DeviceManager.services.DeviceService;
import com.vmo.DeviceManager.services.RequestService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;

    private final UserRepository userRepository;

    private final DeviceService deviceService;

    private final DeviceRepository deviceRepository;

    private final RequestDetailRepository requestDetailRepository;

    private final ApplicationEventPublisher eventPublisher;

    private List<Request> pendingRequests = new ArrayList<>();

    private List<Request> requestsToSave = new ArrayList<>();
    private int lastInsertedRequestId;
    private final Object lock = new Object();

    public RequestServiceImpl(RequestRepository requestRepository, UserRepository userRepository, DeviceService deviceService, DeviceRepository deviceRepository, RequestDetailRepository requestDetailRepository, ApplicationEventPublisher eventPublisher) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.deviceService = deviceService;
        this.deviceRepository = deviceRepository;
        this.requestDetailRepository = requestDetailRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public List<Request> getRequestAdmin() {
        List<Request> allRquests = new ArrayList<>();
        List<Integer> statuses = Arrays.asList(1, 2, 3);
        List<Request> listExits = requestRepository.findAllByStatusIn(statuses);
        allRquests.addAll(requestsToSave);
        allRquests.addAll(listExits);
        if (allRquests.isEmpty()) {
            throw new RequestException("No available request in the list");
        }
        return allRquests;
    }


    @Override
    public Request findByRequestId(int id) {
        for (Request request : pendingRequests) {
            if (request.getRequestId() == id) {
                return request;
            }
        }
        return null;
    }

    @Override
    public String updateRequest(int requestId, RequestDto requestDto) {
        // Tìm kiếm yêu cầu tồn tại dựa trên requestId
        Request exitRequest = null;
        for (Request request : pendingRequests) {
            if (request.getRequestId() == requestId) {
                exitRequest = request;
                break;
            }
        }

        if (exitRequest == null) {
            throw new RequestException("Request not found");
        }

        // Cập nhật các thuộc tính của request bằng dữ liệu từ requestDto
        BeanUtils.copyProperties(requestDto, exitRequest);

        // Lấy danh sách thiết bị từ deviceIds và kiểm tra trạng thái của từng thiết bị
        List<Device> devices = requestDto.getDeviceIds().stream()
                .map(deviceService::getDeviceById)
                .filter(device -> device != null && device.getStatus() == EstatusDevice.Availability)
                .toList();

        if (devices.isEmpty()) {
            throw new DeviceException("No available devices in the list");
        }

        // Kiểm tra và xác thực thời gian của request
        requestDto.validateTime();

        // Cập nhật chi tiết yêu cầu với các thiết bị mới
        List<RequestDetail> updatedRequestDetails = new ArrayList<>();
        for (Device device : devices) {
            RequestDetail detail = new RequestDetail();
            detail.setDevice(device);
            detail.setRequest(exitRequest);
            detail.setStartTime(Date.valueOf(requestDto.getStart()));
            detail.setEndTime(Date.valueOf(requestDto.getEnd()));
            updatedRequestDetails.add(detail);
        }

        // Cập nhật chi tiết yêu cầu của request
        exitRequest.setRequestDetails(updatedRequestDetails);

        // Không thêm lại request vào danh sách nếu đã tồn tại
        if (!pendingRequests.contains(exitRequest)) {
            pendingRequests.add(exitRequest);
        }
        return "Update request success";
    }


    @Override
    public List<Request> getRequestByCreatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        List<Request> list = requestRepository.findByUserCreated(currentUser);
        for (Request pending : pendingRequests) {
            if (pending.getUserCreated().getUserId() == currentUser.getUserId()) {
                list.add(pending);
            }
        }
        for (Request save : requestsToSave) {
            if (save.getUserCreated().getUserId() == currentUser.getUserId()) {
                list.add(save);
            }
        }
        if (list.isEmpty()) {
            throw new RequestException("No requests found");
        }
        return list;
    }

    @Override
    public Page<Request> pageAndSearchRequest(List<Request> listRequest, List<String> status, Integer pageNo, Integer pageSize) {
        if(listRequest.isEmpty()){
            throw new RequestException("List request is empty");
        }
        if (pageNo == null || pageNo <= 0) {
            throw new PagingException("Invalid page number");
        }
        if (pageSize == null || pageSize <= 0) {
            throw new PagingException("Invalid page size");
        }
        int size;
        // Tạo ra Predicate dựa trên keyword, category và type
        Predicate<Request> predicate = request -> status == null || status.isEmpty() || status.contains(request.getStatus().name());

        // Áp dụng Predicate vào danh sách thiết bị
        List<Request> filteredRequests = search(listRequest, predicate);
        size = filteredRequests.size();

        int totalPages = (int) Math.ceil((double) size / pageSize);
        if (pageNo > totalPages) {
            throw new PagingException("Page number exceeds total pages");
        }

        // Phân trang và trả về kết quả
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageSize), size);
        filteredRequests = filteredRequests.subList(start, end);
        return new PageImpl<>(filteredRequests, pageable, size);
    }

    private List<Request> search(List<Request> listRequest, Predicate<Request> predicate) {
        return listRequest.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public int getLastId(){
        Optional<Integer> idDataOptional = requestRepository.getLastId();
        int idData = idDataOptional.orElse(0);

        if(pendingRequests.isEmpty() ){
            lastInsertedRequestId = idData;
        }
        // Kiểm tra danh sách `pendingRequests` và cập nhật `lastInsertedRequestId` nếu có phần tử
        if (!pendingRequests.isEmpty()) {
            Request request1 = pendingRequests.get(pendingRequests.size() - 1);
            int idPending = request1.getRequestId();
            lastInsertedRequestId = Math.max(lastInsertedRequestId, idPending);
        }

        // Kiểm tra danh sách `requestsToSave` và cập nhật `lastInsertedRequestId` nếu có phần tử
        if (!requestsToSave.isEmpty()) {
            Request request2 = requestsToSave.get(requestsToSave.size() - 1);
            int idSave = request2.getRequestId();
            lastInsertedRequestId = Math.max(lastInsertedRequestId, idSave);
        }
        return lastInsertedRequestId;

    }
    @Override
    public String addRequest(RequestDto requestDto) {
        Request request = new Request();
        lastInsertedRequestId = getLastId();
        lastInsertedRequestId++;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        request.setUserCreated(currentUser);
        LocalDate currentDate = LocalDate.now();
        Date currentDateSql = Date.valueOf(currentDate);
        request.setRequestId(lastInsertedRequestId);
        request.setReason(requestDto.getReason());
        request.setCreatedDate(currentDateSql);
        request.setStatus(EstatusRequest.Pending);

        List<Device> devices = requestDto.getDeviceIds().stream()
                .map(deviceService::getDeviceById)
                .filter(device -> device != null && device.getStatus() == EstatusDevice.Availability)
                .toList();

        if (devices.isEmpty()) {
            throw new DeviceException("No available devices in the list");
        }

        requestDto.validateTime();
        List<RequestDetail> pendingRequestDetails = new ArrayList<>();
        for (Device device : devices) {
            RequestDetail requestDetail = new RequestDetail();
            requestDetail.setRequest(request);
            requestDetail.setDevice(device);
            requestDetail.setStartTime(Date.valueOf(requestDto.getStart()));
            requestDetail.setEndTime(Date.valueOf(requestDto.getEnd()));
            pendingRequestDetails.add(requestDetail);
        }
        request.setRequestDetails(pendingRequestDetails);
        pendingRequests.add(request);

        return "Save request successful " + lastInsertedRequestId;

    }


    @Override
    public String sendRequest(int id) {
        Optional<Request> optionalRequest = pendingRequests.stream()
                .filter(request -> request.getRequestId() == id)
                .findFirst();

        if (optionalRequest.isPresent()) {
            Request request = optionalRequest.get();
            // Xác nhận yêu cầu và chuyển sang trạng thái "Processing"
            request.setRequestId(id);
            request.setStatus(EstatusRequest.Processing);
            // Di chuyển yêu cầu vào danh sách chờ lưu vào cơ sở dữ liệu
            requestsToSave.add(request);
            pendingRequests.remove(request);
        } else {
            // Nếu không tìm thấy yêu cầu, ném ra ngoại lệ hoặc ghi log
            throw new RequestException(id);
        }
        return "Send successful";
    }

    @Override
    public String approveRequest(int requestId) {
        LocalDate currentDate = LocalDate.now();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        Request requestToApprove = getRequestById(requestId);
        if (requestToApprove == null) {
            return "Request not found";
        }

        requestToApprove.setRequestId(requestId);
        requestToApprove.setStatus(EstatusRequest.Approved);
        requestToApprove.setResolveDate(Date.valueOf(currentDate));
        requestToApprove.setUserResolve(currentUser.getUserId());
        requestRepository.save(requestToApprove);
        List<RequestDetail> requestDetail = requestToApprove.getRequestDetails();
        for(RequestDetail request: requestDetail){
            requestDetailRepository.save(request);
        }
        requestsToSave.remove(requestToApprove);
        // Cập nhật trạng thái của các thiết bị liên quan
        updateDeviceStatus(requestId, EstatusDevice.Utilized);

        return "Request approved successfully";
    }

    @Override
    public String rejectRequest(int requestId) {
        LocalDate currentDate = LocalDate.now();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        Request requestToReject = getRequestById(requestId);
        if (requestToReject == null) {
            throw new RequestException("Request not found") ;
        }
        requestToReject.setRequestId(requestId);
        requestToReject.setStatus(EstatusRequest.Rejected);
        requestToReject.setResolveDate(Date.valueOf(currentDate));
        requestToReject.setUserResolve(currentUser.getUserId());
        requestToReject.setActualEndTime(Date.valueOf(LocalDate.now()));
        requestRepository.save(requestToReject);
        requestsToSave.remove(requestToReject);

        List<RequestDetail> requestDetail = requestToReject.getRequestDetails();
        for (RequestDetail requestDetail1 : requestDetail) {
            requestDetailRepository.save(requestDetail1);
        }

        return "Reject successful";
    }

    private Request getRequestById(int requestId) {
        return requestsToSave.stream()
                .filter(request -> request.getRequestId() == requestId)
                .findFirst()
                .orElse(null);
    }


    public void updateDeviceStatus(int requestId, EstatusDevice status) {
        List<RequestDetail> requestDetails = requestDetailRepository.findAll().stream()
                .filter(requestDetail -> requestDetail.getRequest().getRequestId() == requestId)
                .toList();

        List<Integer> deviceIds = requestDetails.stream()
                .map(requestDetail -> requestDetail.getDevice().getDeviceId())
                .collect(Collectors.toList());

        List<Device> devices = deviceRepository.findAllById(deviceIds);
        devices.forEach(device -> device.setStatus(status));
        deviceRepository.saveAll(devices);
    }


    @Override
    public String deleteRequest(int requestId) {
        System.out.println(requestId);
        Iterator<Request> iterator = pendingRequests.iterator();
        while (iterator.hasNext()) {
            Request request = iterator.next();
            if (request.getRequestId() == requestId) {
                iterator.remove(); // Sử dụng Iterator để xóa phần tử an toàn
            }else{
                return "Delete request fail";
            }
        }
        return "Delete request success";
    }

    @Override
    public String returnDevice(int requestId) {
        Request request = requestRepository.findById(requestId) .orElseThrow(() -> new RequestException (requestId));
        request.setActualEndTime(Date.valueOf(LocalDate.now()));
        requestRepository.save(request);
        updateDeviceStatus(requestId, EstatusDevice.Availability);
        return "Return device successfully";
    }

    public void setPendingRequests(List<Request> pendingRequests) {
        this.pendingRequests.addAll(pendingRequests);
    }

    public void setRequestsToSave(List<Request> requestsToSave) {
        this.requestsToSave.addAll(requestsToSave);
    }


}
