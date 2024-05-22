package com.vmo.DeviceManager.exceptions.model;

public class DeviceException extends RuntimeException {
    public DeviceException(String message) {
        super(message);
    }
    public DeviceException(int deviceId) {
        super("Could not find device " + deviceId);
    }
}
