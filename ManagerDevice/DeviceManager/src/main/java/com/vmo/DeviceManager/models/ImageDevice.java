package com.vmo.DeviceManager.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "image_devices")
public class ImageDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int imageid;
    @Column(nullable = false, name = "image_link")
    private String imageLink;
    @ManyToOne
    @JoinColumn(nullable = false, name = "device_id")
    private Device imageDevice;
    private String name;
}
