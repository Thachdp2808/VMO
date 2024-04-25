package com.vmo.DeviceManager.services;

import com.vmo.DeviceManager.models.Department;
import com.vmo.DeviceManager.repositories.DepartmentRepository;
import com.vmo.DeviceManager.services.implement.DepartmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @BeforeEach
    void setUp() {
    }

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @Test
    void findById_DepartmentExists_ReturnsDepartment() {
        // Given
        int departmentId = 1;
        Department department = Department.builder()
                .departmentId(departmentId)
                .departmentName("Example Department")
                .build();
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));

        // When
        Department result = departmentService.findById(departmentId);

        // Then
        assertEquals(departmentId, result.getDepartmentId());
        assertEquals("Example Department", result.getDepartmentName());
    }

    @Test
    void findById_DepartmentDoesNotExist_ThrowsRuntimeException() {
        // Given
        int departmentId = 1;
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        // Then
        assertThrows(RuntimeException.class, () -> departmentService.findById(departmentId));
    }
}