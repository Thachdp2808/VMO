package com.vmo.DeviceManager.services;

import com.vmo.DeviceManager.models.Department;

import java.util.List;

public interface DepartmentService {
    Department findById(int id);

    List<Department> findAll();
}
