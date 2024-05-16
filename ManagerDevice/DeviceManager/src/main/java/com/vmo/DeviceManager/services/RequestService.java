package com.vmo.DeviceManager.services;

import com.vmo.DeviceManager.models.Request;
import com.vmo.DeviceManager.models.dto.RequestDto;

import java.util.List;

public interface RequestService {
    List<Request> getRequestAdmin();

    Request findByRequestId(int id);

    String updateRequest(int requestId, RequestDto requestDto);

    List<Request> getRequestByCreatedUser();

    String addRequest(RequestDto requestDto);

    String sendRequest(int id);

    String approveRequest(int requestId);

    String rejectRequest(int requestId);

    String deleteRequest(int requestId);

    String returnDevice(int id);
}
