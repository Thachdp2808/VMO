package com.vmo.DeviceManager.services;

import com.vmo.DeviceManager.models.Device;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DeviceService {
    List<Device> getDeviceByStatus(int status);

    List<Device> getAllDevice();
    Device getDeviceById(int id);

    Page<Device> pageAndSearch(List<Device> listDevice, String keyword, String type, Integer pageNo);

    Page<Device> getAll(Integer pageNo);

}
