package com.vmo.DeviceManager.repositories;

import com.vmo.DeviceManager.models.RequestDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestDetailRepository extends JpaRepository<RequestDetail,Integer> {
    @Query(value = "SELECT SUM(DATEDIFF(rd.end_time, rd.start_time)) AS total_duration_days\n" +
            "FROM request_details rd JOIN requests r ON rd.request_id = r.request_id\n" +
            "WHERE rd.device_id = ?1  ", nativeQuery = true)
    int getDurationDay(int id);

    List<RequestDetail> findByRequest_RequestId(int id);
}
