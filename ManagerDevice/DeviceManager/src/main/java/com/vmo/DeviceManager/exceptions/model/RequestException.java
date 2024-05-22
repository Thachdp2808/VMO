package com.vmo.DeviceManager.exceptions.model;

public class RequestException extends RuntimeException{
    public RequestException(String message) {
        super(message);
    }
    public RequestException(int requestId) {
        super("Could not find request " + requestId);
    }
}
