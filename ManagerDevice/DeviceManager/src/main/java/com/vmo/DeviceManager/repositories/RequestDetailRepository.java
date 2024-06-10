package com.vmo.DeviceManager.repositories;

import com.vmo.DeviceManager.models.RequestDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestDetailRepository extends JpaRepository<RequestDetail,Integer> {
    @Query(value = "SELECT \n" +
            "    d.device_id, \n" +
            "    d.device_name, \n" +
            "    SUM(DATEDIFF(r.actual_end_time, rd.start_time)) AS total_duration_days\n" +
            "FROM \n" +
            "    request_details rd \n" +
            "    JOIN requests r ON rd.request_id = r.request_id\n" +
            "    JOIN devices d ON rd.device_id = d.device_id\n" +
            "WHERE \n" +
            "    r.status = 2\n" +
            "GROUP BY \n" +
            "    d.device_id, \n" +
            "    d.device_name  ", nativeQuery = true)
    List<Object[]> getDurationDay();
}
