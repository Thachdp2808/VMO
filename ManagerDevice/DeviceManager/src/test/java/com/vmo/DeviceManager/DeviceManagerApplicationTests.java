package com.vmo.DeviceManager;

import com.vmo.DeviceManager.models.Department;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.enumEntity.Erole;
import com.vmo.DeviceManager.repositories.DepartmentRepository;
import com.vmo.DeviceManager.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceManagerApplicationTests {

    @Mock
    UserRepository userRepository;

    @Mock
    DepartmentRepository departmentRepository;

    @InjectMocks
    DeviceManagerApplication deviceManagerApplication;


}
