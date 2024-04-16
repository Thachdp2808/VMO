package com.vmo.DeviceManager.services;

import com.vmo.DeviceManager.models.Request;
import com.vmo.DeviceManager.models.dto.RequestDto;

import java.util.List;

public interface RequestService {
    List<Request> getRequestAdmin();

    Request findByRequestId(int id);

    void updateRequest(int requestId, RequestDto requestDto);

    List<Request> getRequestByCreatedUser();

    int addRequest(RequestDto requestDto);

    void sendRequest(int id);

    void approveRequest(int requestId);

    void rejectRequest(int requestId);

    boolean deleteRequest(int requestId);
}
