package com.vmo.DeviceManager.repositories;

import com.vmo.DeviceManager.models.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DeviceRepository extends JpaRepository<Device,Integer> {
    List<Device> getDeviceByStatus(int status);

}
