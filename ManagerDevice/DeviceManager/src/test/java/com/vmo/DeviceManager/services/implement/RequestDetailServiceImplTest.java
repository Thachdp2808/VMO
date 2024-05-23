package com.vmo.DeviceManager.services.implement;

import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.Request;
import com.vmo.DeviceManager.models.RequestDetail;
import com.vmo.DeviceManager.repositories.RequestDetailRepository;
import com.vmo.DeviceManager.services.DeviceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.ArrayList;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestDetailServiceImplTest {
    @Mock
    private RequestDetailRepository requestDetailRepository;

    @Mock
    private DeviceService deviceService;

    @InjectMocks
    private RequestDetailServiceImpl requestDetailService;

    @Test
    void saveRequestDetail() {
        // Mock data
        Request request = new Request();
        Device device1 = new Device();
        Device device2 = new Device();
        List<Device> listDevice = new ArrayList<>();
        listDevice.add(device1);
        listDevice.add(device2);
        Date start = new Date(System.currentTimeMillis());
        Date end = new Date(System.currentTimeMillis());

        // Mock behavior


        // Call method
        requestDetailService.saveRequestDetail(request, listDevice, start, end);

        // Verify interactions
        verify(requestDetailRepository, times(2)).save(any(RequestDetail.class));
    }

    @Test
    void getDurationDay() {
        // Mock data
        int deviceId = 123;

        // Mock behavior
        when(requestDetailRepository.getDurationDay());

        // Call method
        String result = requestDetailService.getDurationDay();

        // Verify result
        assertEquals("Duration day of device: 5 day", result);
    }
}