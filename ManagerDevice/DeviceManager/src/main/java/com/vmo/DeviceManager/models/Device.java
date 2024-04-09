package com.vmo.DeviceManager.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data

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
    private int status;
    private String description;
    private int price;
    @OneToMany(mappedBy = "device")
    private List<RequestDetail> requestDetails;
    @OneToMany(mappedBy = "imageDevice")
    private List<ImageDevice> images;


}
