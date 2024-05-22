package com.vmo.DeviceManager.services.implement;

import com.vmo.DeviceManager.exceptions.model.DepartmentException;
import com.vmo.DeviceManager.models.Department;
import com.vmo.DeviceManager.repositories.DepartmentRepository;
import com.vmo.DeviceManager.services.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Department findById(int id) {
        return departmentRepository.findById(id).orElseThrow(() -> new DepartmentException(id));
    }

    @Override
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }
}
