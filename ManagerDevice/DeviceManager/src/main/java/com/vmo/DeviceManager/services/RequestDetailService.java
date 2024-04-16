package com.vmo.DeviceManager.services;

import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.Request;
import com.vmo.DeviceManager.models.dto.RequestDto;

import java.sql.Date;
import java.util.List;

public interface RequestDetailService {
    void saveRequestDetail(Request request, List<Device> listDevice, Date start, Date end);
}
