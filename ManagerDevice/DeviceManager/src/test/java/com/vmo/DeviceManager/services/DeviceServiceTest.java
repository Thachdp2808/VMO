package com.vmo.DeviceManager.services;

import com.vmo.DeviceManager.models.Category;
import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.dto.DeviceDto;
import com.vmo.DeviceManager.models.enumEntity.EstatusDevice;
import com.vmo.DeviceManager.repositories.CategoryRepository;
import com.vmo.DeviceManager.repositories.DeviceRepository;
import com.vmo.DeviceManager.services.implement.DeviceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.COLLECTION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {
    @Mock
    private DeviceRepository deviceRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private DeviceServiceImpl deviceService;
    @BeforeEach
    void setUp() {
    }

    @Test
    void addDevice_CategoryNotFound_ThrowsRuntimeException() {
        // Arrange
        DeviceDto deviceDto = new DeviceDto();
        deviceDto.setCategory(1); // Assuming category ID is 1
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deviceService.addDevice(deviceDto);
        });
        assertEquals("Category does not exist", exception.getMessage());
    }

    @Test
    void addDevice_ValidInput_DeviceAddedSuccessfully() {
        // Arrange
        DeviceDto deviceDto = new DeviceDto();
        deviceDto.setDeviceName("Device Name");
        deviceDto.setCategory(1); // Assuming category ID is 1
        deviceDto.setPrice(100);
        deviceDto.setDescription("Device description");

        Category category = new Category();
        category.setCategoryId(1);
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        // Act
        String result = deviceService.addDevice(deviceDto);

        // Assert
        assertEquals("Device added successfully", result);
    }

    @Test
    void updateDevice_DeviceExists_ReturnsUpdateSuccessful() {
//        // Given
//        int deviceId = 1;
//        DeviceDto deviceDto = new DeviceDto();
//        deviceDto.setDeviceName("New Device Name");
//        deviceDto.setCategory(1); // Giả lập category ID
//        deviceDto.setPrice(100);
//        deviceDto.setDescription("New Description");
//        deviceDto.setStatus(EstatusDevice.Availability);
//
//        // Giả lập thiết bị và category
//        Device existingDevice = new Device();
//        existingDevice.setDeviceId(deviceId);
//        existingDevice.setDeviceName("Old Device Name");
//        existingDevice.setCategory(new Category()); // Giả lập category
//        existingDevice.setPrice(50.0);
//        existingDevice.setDescription("Old Description");
//        existingDevice.setStatus(EstatusDevice.Maintenance);
//
//        when(deviceRepository.findById(deviceId)).thenReturn(Optional.of(existingDevice));
//
//        Category category = new Category(); // Tạo category giả lập
//        when(categoryRepository.findById(deviceDto.getCategory())).thenReturn(Optional.of(category));
//
//        // When
//        String result = deviceService.updateDevice(deviceId, deviceDto);
//
//        // Then
//        assertThat(result).isEqualTo("Device ID: 1, Maintenance Seconds Count: 0.0");
//        assertThat(existingDevice.getDeviceName()).isEqualTo("NEW DEVICE NAME");
//        assertThat(existingDevice.getCategory()).isEqualTo(category);
//        assertThat(existingDevice.getPrice()).isEqualTo(100.0);
//        assertThat(existingDevice.getDescription()).isEqualTo("New Description");
//        assertThat(existingDevice.getStatus()).isEqualTo(EstatusDevice.Availability);
//        verify(deviceRepository).save(existingDevice);
    }

    @Test
    void updateDevice_DeviceNotExists_ReturnsRuntimeException() {
        // Given
        int deviceId = 1;
        DeviceDto deviceDto = new DeviceDto();
        deviceDto.setDeviceName("New Device Name");
        deviceDto.setCategory(1); // Giả lập category ID
        deviceDto.setPrice(100);
        deviceDto.setDescription("New Description");
        deviceDto.setStatus(EstatusDevice.Availability);

        // Giả lập việc không tìm thấy thiết bị
        when(deviceRepository.findById(deviceId)).thenReturn(Optional.empty());

        // When/Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deviceService.updateDevice(deviceId,deviceDto);
        });
        assertEquals("Device does not exist", exception.getMessage());
    }

    @Test
    void updateDevice_CategoryNotExists_ReturnsRuntimeException() {
        // Given
        int deviceId = 1;
        DeviceDto deviceDto = new DeviceDto();
        deviceDto.setDeviceName("New Device Name");
        deviceDto.setCategory(1); // Giả lập category ID
        deviceDto.setPrice(100);
        deviceDto.setDescription("New Description");
        deviceDto.setStatus(EstatusDevice.Availability);

        // Giả lập thiết bị và category
        Device existingDevice = new Device();
        existingDevice.setDeviceId(deviceId);
        existingDevice.setDeviceName("Old Device Name");
        existingDevice.setCategory(new Category()); // Giả lập category
        existingDevice.setPrice(50.0);
        existingDevice.setDescription("Old Description");
        existingDevice.setStatus(EstatusDevice.Maintenance);

        when(deviceRepository.findById(deviceId)).thenReturn(Optional.of(existingDevice));

        when(categoryRepository.findById(deviceDto.getCategory())).thenReturn(Optional.empty());

        // When/Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deviceService.updateDevice(deviceId,deviceDto);
        });
        assertEquals("Category does not exist", exception.getMessage());
    }

    @Test
    void getAllDevice_ReturnsDeviceList() {
        // Given
        List<Device> deviceList = new ArrayList<>();
        deviceList.add(new Device());
        deviceList.add(new Device());
        when(deviceRepository.findAll()).thenReturn(deviceList);

        // When
        List<Device> result = deviceService.getAllDevice();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2); // Kiểm tra danh sách trả về có 2 phần tử
    }

    @Test
    void getAllDevice_ReturnsRuntimeException() {
        // Given
        when(deviceRepository.findAll()).thenReturn(Collections.emptyList());

        // Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deviceService.getAllDevice();
        });
        assertEquals("No devices found", exception.getMessage());

    }

    @Test
    void getMyDevice_UserAuthenticated_ReturnsDeviceList() {

        User currentUser = new User();
        currentUser.setUserId(1);
        // Given
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(currentUser);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        List<Device> devices = new ArrayList<>();
        devices.add(new Device());
        devices.add(new Device());

        when(deviceRepository.getMyDevices(1)).thenReturn(devices);

        // When
        List<Device> result = deviceService.getMyDevice();

        // Then
        assertEquals(devices.size(), result.size());
    }

    @Test
    void getMyDevice_UserAuthenticated_ReturnsRuntimeException() {

        User currentUser = new User();
        currentUser.setUserId(1);
        // Given
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(currentUser);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(deviceRepository.getMyDevices(1)).thenReturn(Collections.emptyList());

        // Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deviceService.getMyDevice();
        });
        assertEquals("No devices found", exception.getMessage());
    }


    @Test
    void getDeviceById_DeviceExists_ReturnsDevice() {
        // Given
        int deviceId = 1;
        Device device = new Device();
        device.setDeviceId(deviceId);
        when(deviceRepository.findById(deviceId)).thenReturn(Optional.of(device));

        // When
        Device result = deviceService.getDeviceById(deviceId);

        // Then
        assertEquals(device, result);
    }

    @Test
    void getDeviceById_DeviceNotExists_ThrowsRuntimeException() {
        // Given
        int deviceId = 1;
        when(deviceRepository.findById(deviceId)).thenReturn(Optional.empty());

        // Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deviceService.getDeviceById(deviceId);
        });
        assertEquals("Device does not exits", exception.getMessage());
    }

    @Test
    void pageAndSearch_ValidParameters_ReturnsPage() {
//        // Given
//        List<Device> listDevice = new ArrayList<>();
//        listDevice.add(Device.builder().deviceId(1).deviceName("Device 1").category(Category.builder().categoryId(1).categoryName("Category1").type("Type1").build()).build());
//        listDevice.add(Device.builder().deviceId(2).deviceName("Device 2").category(Category.builder().categoryId(2).categoryName("Category2").type("Type2").build()).build());
//        listDevice.add(Device.builder().deviceId(3).deviceName("Device 3").category(Category.builder().categoryId(3).categoryName("Category3").type("Type3").build()).build());
//        String keyword = "";
//        String type = "name";
//        Integer pageNo = 1;
//        Integer pageSize = 2;
//        // When
//        Page<Device> resultPage = deviceService.pageAndSearch( keyword, type, pageNo, pageSize);
//
//        // Then
//        assertEquals(2, resultPage.getContent().size());
//        assertEquals(3, resultPage.getTotalElements());
//        assertEquals(2, resultPage.getTotalPages());

    }
    @Test
    void pageAndSearch_InvalidType_ThrowsIllegalArgumentException() {
//        // Given
//        List<Device> listDevice = new ArrayList<>();
//        String keyword = "Category1";
//        String type = "invalidType";
//        Integer pageNo = 1;
//        Integer pageSize = 2;
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> deviceService.pageAndSearch( keyword, type, pageNo, pageSize));
//        assertEquals("Invalid search type", exception.getMessage());

    }

    @Test
    void getAll_ReturnsPageOfDevices() {
        List<Device> listDevice = new ArrayList<>();
        listDevice.add(Device.builder().deviceId(1).deviceName("Device 1").category(Category.builder().categoryId(1).categoryName("Category1").type("Type1").build()).build());
        listDevice.add(Device.builder().deviceId(2).deviceName("Device 2").category(Category.builder().categoryId(2).categoryName("Category2").type("Type2").build()).build());
        listDevice.add(Device.builder().deviceId(3).deviceName("Device 3").category(Category.builder().categoryId(3).categoryName("Category3").type("Type3").build()).build());

        // Given
        int pageNo = 1;
        Pageable pageable = PageRequest.of(pageNo - 1, 2);
        Page<Device> devicePage = new PageImpl<>(listDevice, pageable, listDevice.size());
        when(deviceRepository.findAll(pageable)).thenReturn(devicePage);

        // When
        Page<Device> result = deviceService.getAll(pageNo);

        // Then
        assertEquals(devicePage.getTotalElements(), result.getTotalElements());

    }
}