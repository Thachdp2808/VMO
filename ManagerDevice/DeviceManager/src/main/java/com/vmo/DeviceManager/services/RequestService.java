package com.vmo.DeviceManager.services;

import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.Request;
import com.vmo.DeviceManager.models.dto.RequestDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RequestService {
    List<Request> getRequestAdmin();

    Request findByRequestId(int id);

    String updateRequest(int requestId, RequestDto requestDto);

    List<Request> getRequestByCreatedUser();
    Page<Request> pageAndSearchRequest(List<Request> listRequest, List<String> status, Integer pageNo, Integer pageSize);

    String addRequest(RequestDto requestDto);

    String sendRequest(int id);

    String approveRequest(int requestId);

    String rejectRequest(int requestId);

    String deleteRequest(int requestId);

    String returnDevice(int id);
}
