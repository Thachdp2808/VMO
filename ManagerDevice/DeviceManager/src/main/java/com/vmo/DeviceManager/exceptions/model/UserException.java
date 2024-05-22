package com.vmo.DeviceManager.exceptions.model;

public class UserException extends RuntimeException{
    public UserException(String message) {
        super(message);
    }
    public UserException(int userName) {
        super("Could not find user " + userName);
    }
}
