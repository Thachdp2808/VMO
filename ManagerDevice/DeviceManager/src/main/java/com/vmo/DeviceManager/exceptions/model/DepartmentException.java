package com.vmo.DeviceManager.exceptions.model;

public class DepartmentException extends RuntimeException{
    public DepartmentException(String message) {
        super(message);
    }
    public DepartmentException(int departmentId) {
        super("Could not find department " + departmentId);
    }
}
