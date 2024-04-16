package com.vmo.DeviceManager.services.implement;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
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
    private int lastInsertedRequestId ;
    private final Object lock = new Object();

    public RequestServiceImpl(RequestRepository requestRepository, UserRepository userRepository, DeviceService deviceService, DeviceRepository deviceRepository, RequestDetailRepository requestDetailRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.deviceService = deviceService;
        this.deviceRepository = deviceRepository;
        this.requestDetailRepository = requestDetailRepository ;
    }

    @Override
    public List<Request> getRequestAdmin() {
        List<Request> allRquests = new ArrayList<>();
        List<Integer> statuses = Arrays.asList(1, 2, 3);
        List<Request> listExits = requestRepository.findAllByStatusIn(statuses);
        allRquests.addAll(requestsToSave);
        allRquests.addAll(listExits);
        return allRquests;
    }

    @Override
    public Request findByRequestId(int id) {
        for(Request request: pendingRequests){
            if(request.getRequestId() == id){
                return request;
            }
        }
        return null;
    }

    @Override
    @Transactional
    public void updateRequest(int requestId, RequestDto requestDto) {
        Request exitRequest = new Request();
        for(Request request: pendingRequests){
            if(request.getRequestId() == requestId){
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
            throw new IllegalArgumentException("No available devices in the list");
        }
        pendingRequests.add(exitRequest);
        RequestDetail exitsRequestDetail= new RequestDetail() ;
        for(RequestDetail pendingRequest: pendingRequestDetails){
            if(pendingRequest.getRequest().getRequestId() == requestId){
                exitsRequestDetail = pendingRequest;
                for(Device device: devices){
                    exitsRequestDetail.setDevice(device);
                    exitsRequestDetail.setStartTime(requestDto.getStart());
                    exitsRequestDetail.setEndTime(requestDto.getEnd());
                }
            }

        }
    }

    @Override
    public List<Request> getRequestByCreatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return requestRepository.findByUserCreated(currentUser);
    }

    @Override
    public int addRequest(RequestDto requestDto) {
        Request request = new Request();
        synchronized (lock) {
            if (lastInsertedRequestId == 0L) {
                // Nếu không có yêu cầu nào trong cơ sở dữ liệu, lấy ID của yêu cầu cuối cùng từ cơ sở dữ liệu
                lastInsertedRequestId = requestRepository.getLastId();
            }

            // Tăng ID lên 1 cho yêu cầu mới
            lastInsertedRequestId++;
        }


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
                .collect(Collectors.toList());

        if (devices.isEmpty()) {
            throw new IllegalArgumentException("No available devices in the list");
        }
        pendingRequests.add(request);
        for(Device device: devices){
            RequestDetail requestDetail = new RequestDetail();
            requestDetail.setRequest(request);
            requestDetail.setDevice(device);
            requestDetail.setStartTime(requestDto.getStart());
            requestDetail.setEndTime(requestDto.getEnd());
            pendingRequestDetails.add(requestDetail);
        }
        return request.getRequestId();
    }

    @Override
    public void sendRequest(int id) {
        for (Request request : pendingRequests) {
            if (request.getRequestId() == id) {
                request.setStatus(EstatusRequest.Processing);
                // Di chuyển yêu cầu vào danh sách chờ lưu vào cơ sở dữ liệu
                requestsToSave.add(request);
                pendingRequests.remove(request);
                break;
            }
        }
        for (RequestDetail requestDetail : pendingRequestDetails) {
            if (requestDetail.getRequest().getRequestId() == id) {
                requestDetailsToSave.add(requestDetail); // Lưu requestDetail vào cơ sở dữ liệu
            }
        }

        // Gỡ bỏ các requestDetail đã lưu khỏi danh sách requestDetailsToSave
        pendingRequestDetails.removeIf(requestDetail -> requestDetail.getRequest().getRequestId() == id);

    }

    @Override
    public void approveRequest(int requestId) {
        LocalDate currentDate = LocalDate.now();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        if(currentUser.getRole()== Erole.USER){
            return;
        }

        List<Device> listDevice = deviceService.getAllDevice();

        for (Request request : requestsToSave) {
            if (request.getRequestId() == requestId) {
                request.setStatus(EstatusRequest.Approved);
                request.setResolveDate(Date.valueOf(currentDate));
                request.setUserResolve(currentUser.getUserId());
                requestRepository.save(request);
                requestsToSave.remove(request);
                break;
            }
        }

        for (RequestDetail requestDetail : requestDetailsToSave) {
            if (requestDetail.getRequest().getRequestId() == requestId) {
                for(Device device: listDevice){
                    if(device.getDeviceId() == requestDetail.getDevice().getDeviceId()){
                        device.setStatus(EstatusDevice.Utilized);
                    }
                }
                requestDetailRepository.save(requestDetail); // Lưu requestDetail vào cơ sở dữ liệu
            }
        }

        // Gỡ bỏ các requestDetail đã lưu khỏi danh sách requestDetailsToSave
        requestDetailsToSave.removeIf(requestDetail -> requestDetail.getRequest().getRequestId() == requestId);

    }

    @Override
    public void rejectRequest(int requestId) {

    }

    @Override
    public boolean deleteRequest(int requestId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        pendingRequests.stream()
                .map(Request::getRequestId)
                .forEach(System.out::println);

        for (Request request : pendingRequests) {
            if (request.getRequestId() == requestId) {
                pendingRequests.remove(request);
            }else {
                return false;
            }
        }

        Iterator<RequestDetail> requestDetailIterator = pendingRequestDetails.iterator();
        while (requestDetailIterator.hasNext()) {
            RequestDetail requestDetail = requestDetailIterator.next();
            if (requestDetail.getRequest().getRequestId() == requestId && requestDetail.getRequest().getUserCreated().getUserId() == currentUser.getUserId()) {
                requestDetailIterator.remove();
            }else{
                return false;
            }
        }
        return true;
    }

}
