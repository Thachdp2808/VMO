package com.vmo.DeviceManager.models.event;

import org.springframework.context.ApplicationEvent;

public class RequestEvent extends ApplicationEvent {
    private final int total;
    public RequestEvent(Object source, int total) {
        super(source);
        this.total = total;
    }
    public int getTotal() {
        return total;
    }
}
