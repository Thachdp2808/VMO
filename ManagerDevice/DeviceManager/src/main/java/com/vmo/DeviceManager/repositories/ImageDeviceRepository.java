package com.vmo.DeviceManager.repositories;

import com.vmo.DeviceManager.models.ImageDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageDeviceRepository extends JpaRepository<ImageDevice,Integer> {
}
