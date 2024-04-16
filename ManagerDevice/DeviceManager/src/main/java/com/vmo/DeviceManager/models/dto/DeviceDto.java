package com.vmo.DeviceManager.models.dto;

import com.vmo.DeviceManager.models.enumEntity.EstatusDevice;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeviceDto {
     private String deviceName;
    private int category;
    private String description;
    private EstatusDevice status;
    private int price;
}
