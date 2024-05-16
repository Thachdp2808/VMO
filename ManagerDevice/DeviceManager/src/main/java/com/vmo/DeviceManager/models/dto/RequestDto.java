package com.vmo.DeviceManager.models.dto;

import com.vmo.DeviceManager.models.Device;
import lombok.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestDto {
    private String reason;
    private List<Integer> deviceIds;

    private LocalDate start;
    private LocalDate end;

    public void validateTime() {
        if (start == null) {
            start = LocalDate.now();
        }
        if (end == null) {
            end = start.plusMonths(3);
        }
    }
}
