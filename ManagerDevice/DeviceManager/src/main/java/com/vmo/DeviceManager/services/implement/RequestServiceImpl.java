package com.vmo.DeviceManager.services.implement;

import com.vmo.DeviceManager.exceptions.model.DeviceException;
import com.vmo.DeviceManager.exceptions.model.PagingException;
import com.vmo.DeviceManager.exceptions.model.RequestException;
import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.Request;
import com.vmo.DeviceManager.models.RequestDetail;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.dto.RequestDto;
import com.vmo.DeviceManager.models.enumEntity.Erole;
import com.vmo.DeviceManager.models.enumEntity.EstatusDevice;
import com.vmo.DeviceManager.models.enumEntity.EstatusRequest;
import com.vmo.DeviceManager.repositories.DeviceRepository;
import com.vmo.DeviceManager.repositories.RequestDetailRepository;
import com.vmo.DeviceManager.repositories.RequestRepository;
import com.vmo.DeviceManager.repositories.UserRepository;
import com.vmo.DeviceManager.services.DeviceService;
import com.vmo.DeviceManager.services.RequestService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private List<Request> pendingRequests = new ArrayList<>();

    private List<Request> requestsToSave = new ArrayList<>();
    private List<RequestDetail> pendingRequestDetails = new ArrayList<>();
    private List<RequestDetail> requestDetailsToSave = new ArrayList<>();
    private int lastInsertedRequestId;
    private final Object lock = new Object();

    public RequestServiceImpl(RequestRepository requestRepository, UserRepository userRepository, DeviceService deviceService, DeviceRepository deviceRepository, RequestDetailRepository requestDetailRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.deviceService = deviceService;
        this.deviceRepository = deviceRepository;
        this.requestDetailRepository = requestDetailRepository;
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
    @Transactional
    public String updateRequest(int requestId, RequestDto requestDto) {
        Request exitRequest = new Request();
        for (Request request : pendingRequests) {
            if (request.getRequestId() == requestId) {
                exitRequest = request;
                break;
            }
        }
        BeanUtils.copyProperties(requestDto, exitRequest);

        List<Device> devices = requestDto.getDeviceIds().stream()
                .map(deviceService::getDeviceById)
                .filter(device -> device != null && device.getStatus() == EstatusDevice.Availability)
                .collect(Collectors.toList());

        if (devices.isEmpty()) {
            throw new DeviceException("No available devices in the list");
        }
        requestDto.validateTime();
        pendingRequests.add(exitRequest);
        RequestDetail exitsRequestDetail = new RequestDetail();
        for (RequestDetail pendingRequest : pendingRequestDetails) {
            if (pendingRequest.getRequest().getRequestId() == requestId) {
                exitsRequestDetail = pendingRequest;
                for (Device device : devices) {
                    exitsRequestDetail.setDevice(device);
                    exitsRequestDetail.setStartTime(Date.valueOf(requestDto.getStart()));
                    exitsRequestDetail.setEndTime(Date.valueOf(requestDto.getEnd()));
                }
            }

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
        if (pageNo == null || pageNo <= 0) {
            throw new PagingException("Invalid page number");
        }
        if (pageSize == null || pageSize <= 0) {
            throw new PagingException("Invalid page size");
        }
        int size;
        // Tạo ra Predicate dựa trên keyword, category và type
        Predicate<Request> predicate = request -> {
            return status == null || status.isEmpty() || status.contains(request.getStatus().name());
        };

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
        int idData = requestRepository.getLastId();
        if(pendingRequests.isEmpty() ){
            lastInsertedRequestId = idData;
        }else{
            Request request1 = pendingRequests.get(pendingRequests.size()-1);
            int idPending = request1.getRequestId();
            lastInsertedRequestId = Math.max(idData, idPending);
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
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User currentUser = (User) authentication.getPrincipal();
//        System.out.print(currentUser.getUserId());
//        for(Request exitsrequest :pendingRequests){
//            if(currentUser.getUserId() != exitsrequest.getUserCreated().getUserId()){
//
//                return "Access Denied" ;
//            }
//        }


        Optional<Request> optionalRequest = pendingRequests.stream()
                .filter(request -> request.getRequestId() == id)
                .findFirst();

        if (optionalRequest.isPresent()) {
            Request request = optionalRequest.get();
            // Xác nhận yêu cầu và chuyển sang trạng thái "Processing"
            request.setStatus(EstatusRequest.Processing);
            // Di chuyển yêu cầu vào danh sách chờ lưu vào cơ sở dữ liệu


            // Lấy danh sách các chi tiết yêu cầu liên quan
            List<RequestDetail> relatedRequestDetails = pendingRequestDetails.stream()
                    .filter(detail -> detail.getRequest().getRequestId() == id)
                    .toList();

            // Lưu các chi tiết yêu cầu vào cơ sở dữ liệu
            request.setRequestDetails(relatedRequestDetails);
            requestsToSave.add(request);
            pendingRequests.remove(request);
            requestDetailsToSave.addAll(relatedRequestDetails);

            // Gỡ bỏ các chi tiết yêu cầu đã lưu khỏi danh sách chờ
            pendingRequestDetails.removeAll(relatedRequestDetails);
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

        // Kiểm tra quyền của người dùng
        if (currentUser.getRole() == Erole.USER) {
            return "Access denied";
        }

        Request requestToApprove = getRequestById(requestId);
        if (requestToApprove == null) {
            return "Request not found";
        }

        requestToApprove.setStatus(EstatusRequest.Approved);
        requestToApprove.setResolveDate(Date.valueOf(currentDate));
        requestToApprove.setUserResolve(currentUser.getUserId());
        requestRepository.save(requestToApprove);
        requestsToSave.remove(requestToApprove);


        List<RequestDetail> requestDetail = getRequestDetailById(requestId);
        requestDetailRepository.saveAll(requestDetail);
        for (RequestDetail requestDetail1 : requestDetail) {
            requestDetailsToSave.remove(requestDetail1);
        }

        // Cập nhật trạng thái của các thiết bị liên quan
        updateDeviceStatus(requestId, EstatusDevice.Utilized);

        return "Request approved successfully";
    }

    @Override
    public String rejectRequest(int requestId) {
        LocalDate currentDate = LocalDate.now();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        // Kiểm tra quyền của người dùng
        if (currentUser.getRole() == Erole.USER) {
            return "Access denied";
        }

        Request requestToReject = getRequestById(requestId);
        if (requestToReject == null) {
            return "Request not found";
        }

        requestToReject.setStatus(EstatusRequest.Rejected);
        requestToReject.setResolveDate(Date.valueOf(currentDate));
        requestToReject.setUserResolve(currentUser.getUserId());
        requestRepository.save(requestToReject);
        requestsToSave.remove(requestToReject);

        List<RequestDetail> requestDetail = getRequestDetailById(requestId);
        for (RequestDetail requestDetail1 : requestDetail) {
            requestDetailsToSave.remove(requestDetail1);
        }

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

    private List<RequestDetail> getRequestDetailById(int requestId) {
        return requestDetailsToSave.stream()
                .filter(request -> request.getRequest().getRequestId() == requestId)
                .toList();
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        pendingRequests.stream()
                .map(Request::getRequestId)
                .forEach(System.out::println);

        for (Request request : pendingRequests) {
            if (request.getRequestId() == requestId) {
                pendingRequests.remove(request);
            } else {
                return "Delete request fail";
            }
        }

        Iterator<RequestDetail> requestDetailIterator = pendingRequestDetails.iterator();
        while (requestDetailIterator.hasNext()) {
            RequestDetail requestDetail = requestDetailIterator.next();
            if (requestDetail.getRequest().getRequestId() == requestId && requestDetail.getRequest().getUserCreated().getUserId() == currentUser.getUserId()) {
                requestDetailIterator.remove();
            } else {
                return "Delete request fail";
            }
        }
        return "Delete request success";
    }

    @Override
    public String returnDevice(int id) {
        List<RequestDetail> exitsRequestDetail = requestDetailRepository.findByRequest_RequestId(id);
        for (RequestDetail list : exitsRequestDetail) {
            list.setEndTime(Date.valueOf(LocalDate.now()));
        }
        updateDeviceStatus(id, EstatusDevice.Availability);
        return "Return device successfully";
    }

    public void setPendingRequests(List<Request> pendingRequests) {
        this.pendingRequests.addAll(pendingRequests);
    }

    public void setRequestsToSave(List<Request> requestsToSave) {
        this.requestsToSave.addAll(requestsToSave);
    }

    public void setRequestDetailsToSave(List<RequestDetail> RequestDetailsToSave) {
        this.requestDetailsToSave.addAll(RequestDetailsToSave);
    }

}
