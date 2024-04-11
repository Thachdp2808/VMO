package com.vmo.DeviceManager.services.implement;

import com.vmo.DeviceManager.models.Department;
import com.vmo.DeviceManager.repositories.DepartmentRepository;
import com.vmo.DeviceManager.services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentImpl implements DepartmentService {
    @Autowired
    DepartmentRepository departmentRepository;
    @Override
    public Department findById(int id) {
        return departmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Department does not exits"));
    }
}
