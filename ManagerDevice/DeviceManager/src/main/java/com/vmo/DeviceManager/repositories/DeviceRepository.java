package com.vmo.DeviceManager.repositories;

import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.Request;
import com.vmo.DeviceManager.models.enumEntity.EstatusDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DeviceRepository extends JpaRepository<Device,Integer> {
    List<Device> getDeviceByStatus(EstatusDevice status);
    @Query("SELECT d FROM Device d " +
            "INNER JOIN RequestDetail rd ON rd.device = d " +
            "INNER JOIN rd.request r " +
            "WHERE r.userCreated.userId = ?1 AND r.status = 2")
    List<Device> getMyDevices(int userId);

    @Query(value = "SELECT c.category_name, COUNT(d.device_id) AS total_devices\n" +
            "FROM devices d\n" +
            "JOIN categories c ON d.category_id = c.category_id\n" +
            "GROUP BY c.category_name", nativeQuery = true)
    List<Object[]> countDeviceByCategory();

    @Query(value = "SELECT  c.type, COUNT(d.device_id) AS total_devices\n" +
            "FROM devices d\n" +
            "JOIN categories c ON d.category_id = c.category_id\n" +
            "GROUP BY  c.type", nativeQuery = true)
    List<Object[]> countDeviceByType();

}
