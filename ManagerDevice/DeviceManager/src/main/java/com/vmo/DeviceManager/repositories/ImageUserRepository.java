package com.vmo.DeviceManager.repositories;

import com.vmo.DeviceManager.models.ImageUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageUserRepository extends JpaRepository<ImageUser, Integer> {
}
