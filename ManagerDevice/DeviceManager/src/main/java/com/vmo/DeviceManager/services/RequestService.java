package com.vmo.DeviceManager.services;

import com.vmo.DeviceManager.models.Request;

import java.util.List;

public interface RequestService {
    List<Request> getAllByStatus();

    Request findByRequestId(int id);

    List<Request> getRequestByCreatedUser();

    int addRequest(Request request);

    void sendRequest(int id);
}
