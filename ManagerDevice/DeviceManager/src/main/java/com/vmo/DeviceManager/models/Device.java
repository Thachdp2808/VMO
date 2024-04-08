package com.vmo.DeviceManager.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int deviceId;
    @Column(nullable = false, name = "device_name")
    private String deviceName;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(nullable = false, columnDefinition = "1")
    private int status;
    private String description;
    private int price;
    @OneToMany(mappedBy = "device")
    private List<RequestDetail> requestDetails;


}
