package com.vmo.DeviceManager.repositories;

import com.vmo.DeviceManager.models.RequestDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestDetailRepository extends JpaRepository<RequestDetail,Integer> {
}
