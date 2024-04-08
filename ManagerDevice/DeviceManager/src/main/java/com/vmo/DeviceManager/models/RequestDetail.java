package com.vmo.DeviceManager.models;

import jakarta.persistence.*;

@Entity
@Table(name = "requestDetails")
public class RequestDetail {
    @Id
    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;
    @Id
    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    public RequestDetail(){

    }


}
