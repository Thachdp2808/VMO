package com.vmo.DeviceManager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vmo.DeviceManager.models.enumEntity.EstatusDevice;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

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
    private EstatusDevice status;
    private String description;
    private double price;
    @OneToMany(mappedBy = "device", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<RequestDetail> requestDetails;
    @OneToMany(mappedBy = "imageDevice", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<ImageDevice> images;


}
