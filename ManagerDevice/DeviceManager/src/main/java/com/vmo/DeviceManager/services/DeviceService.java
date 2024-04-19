package com.vmo.DeviceManager.services;

import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.dto.DeviceDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DeviceService {
    List<Device> getDeviceByStatus(int status);

    String addDevice(DeviceDto deviceDto);

    void updateDevice(int id, DeviceDto deviceDto);

    void deleteDevice(int id);

    List<Device> getAllDevice();
    List<Device> getMyDevice();
    Device getDeviceById(int id);

    Page<Device> pageAndSearch(List<Device> listDevice, String keyword, String type, Integer pageNo, Integer pageSize);

    Page<Device> getAll(Integer pageNo);

}
