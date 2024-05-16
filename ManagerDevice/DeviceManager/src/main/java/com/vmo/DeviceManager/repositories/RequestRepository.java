package com.vmo.DeviceManager.repositories;

import com.vmo.DeviceManager.models.Request;
import com.vmo.DeviceManager.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
    @Query("SELECT r FROM Request r WHERE r.status IN :statuses")
    List<Request> findAllByStatusIn(List<Integer> statuses);

    List<Request> findByUserCreated(User userCreated);

    @Query("SELECT Max(requestId) FROM Request ")
    int getLastId();

}
