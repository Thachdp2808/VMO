package com.vmo.DeviceManager.models.event;

import org.springframework.context.ApplicationEvent;

public class RequestReturnEvent extends ApplicationEvent {
    private final int total;

    public RequestReturnEvent(Object source, int total) {
        super(source);
        this.total = total;
    }

    public int getTotal() {
        return total;
    }
}
