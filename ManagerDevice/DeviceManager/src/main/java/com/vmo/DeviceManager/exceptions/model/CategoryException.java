package com.vmo.DeviceManager.exceptions.model;

public class CategoryException extends RuntimeException {
    public CategoryException(String message) {
        super(message);
    }
    public CategoryException(int categoryId) {
        super("Could not find category " + categoryId);
    }
}
