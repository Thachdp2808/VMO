package com.vmo.DeviceManager.exceptions.model;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String message) {
        super(message);
    }

}
