package com.vmo.DeviceManager.repositories;

import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DeviceRepository extends JpaRepository<Device,Integer> {
    List<Device> getDeviceByStatus(int status);
    @Query("SELECT d FROM Device d " +
            "INNER JOIN RequestDetail rd ON rd.device = d " +
            "INNER JOIN rd.request r " +
            "WHERE r.userCreated.userId = ?1 AND r.status = 2")
    List<Device> getMyDevices(int userId);

}
