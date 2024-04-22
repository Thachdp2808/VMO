package com.vmo.DeviceManager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Device imageDevice;
    private String name;

    @Override
    public String toString() {
        return "ImageDevice{" +
                "imageid=" + imageid +
                ", imageLink='" + imageLink + '\'' +
                ", imageDevice=" + imageDevice.getDeviceId() +
                ", name='" + name + '\'' +
                '}';
    }
}
