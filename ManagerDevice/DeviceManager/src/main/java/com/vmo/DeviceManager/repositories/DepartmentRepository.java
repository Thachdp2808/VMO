package com.vmo.DeviceManager.repositories;

import com.vmo.DeviceManager.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Integer> {
    Optional<Department> findById(int departmentId);
}
