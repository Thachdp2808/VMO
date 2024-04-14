package com.vmo.DeviceManager.models.dto;

import com.vmo.DeviceManager.models.Device;
import lombok.*;

import java.sql.Date;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestDto {
    private String reason;
    private List<Integer> deviceIds;

    private Date start;
    private Date end;
}
