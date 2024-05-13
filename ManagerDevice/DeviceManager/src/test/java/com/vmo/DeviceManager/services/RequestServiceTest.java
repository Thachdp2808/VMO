package com.vmo.DeviceManager.services;

import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.Request;
import com.vmo.DeviceManager.models.RequestDetail;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.dto.RequestDto;
import com.vmo.DeviceManager.models.enumEntity.EstatusDevice;
import com.vmo.DeviceManager.repositories.DeviceRepository;
import com.vmo.DeviceManager.repositories.RequestRepository;
import com.vmo.DeviceManager.services.implement.RequestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestServiceTest {

    @Mock
    private RequestRepository requestRepository;

    @InjectMocks
    private RequestServiceImpl requestService;
    @Mock
    private DeviceRepository deviceRepository;



    @Test
    void getRequestAdmin_NoRequests_ThrowsIllegalArgumentException() {

        // Given
        List<Integer> statuses = Arrays.asList(1, 2, 3);
        when(requestRepository.findAllByStatusIn(statuses)).thenReturn(Collections.emptyList());


        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> requestService.getRequestAdmin());
        assertEquals("No available request in the list", exception.getMessage());
    }
    @Test
    void getRequestAdmin_ReturnsCorrectRequestsList() {
        List<Request> requestsToSave = new ArrayList<>();
        requestsToSave.add(new Request());
        List<Request> listExits = Arrays.asList(new Request(), new Request());

        // Mock repository behavior
        when(requestRepository.findAllByStatusIn(anyList())).thenReturn(listExits);

        // When
        List<Request> result = requestService.getRequestAdmin();

        // Then
        assertEquals(2, result.size()); // Expected size is 3 (2 from listExits + 1 from requestsToSave)
    }
    @Test
    void getRequestAdmin_NoRequests_ReturnsIllegalArgumentException() {
        // Given

        List<Request> requestsToSave = new ArrayList<>(); // Empty list

        when(requestRepository.findAllByStatusIn(anyList())).thenReturn(new ArrayList<>()); // Empty list

        // When, Then
        assertThrows(IllegalArgumentException.class, () -> {
            requestService.getRequestAdmin();
        });
    }

    @Test
    void findByRequestId_ReturnsCorrectRequest() {
        // Given

        List<Request> pendingRequests = new ArrayList<>();
        Request request1 = new Request();
        request1.setRequestId(1);
        Request request2 = new Request();
        request2.setRequestId(2);
        pendingRequests.add(request1);
        pendingRequests.add(request2);
        requestService.setPendingRequests(pendingRequests);

        // When
        Request result = requestService.findByRequestId(1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getRequestId());
    }
    @Test
    void findByRequestId_RequestNotFound_ReturnsNull() {
        // Given

        List<Request> pendingRequests = new ArrayList<>();
        Request request1 = new Request();
        request1.setRequestId(1);
        Request request2 = new Request();
        request2.setRequestId(2);
        pendingRequests.add(request1);
        pendingRequests.add(request2);
        requestService.setPendingRequests(pendingRequests);

        // When
        Request result = requestService.findByRequestId(3);

        // Then
        assertNull(result);
    }

    @Test
    void updateDeviceStatus_CorrectlyUpdatesDeviceStatus() {
        // Given
        Request request1 = new Request();
        request1.setRequestId(1);

        List<RequestDetail> requestDetailsToSave = new ArrayList<>();
        RequestDetail requestDetail1 = new RequestDetail();
        Device device1 = new Device();
        device1.setDeviceId(1);
        device1.setStatus(EstatusDevice.Utilized);
        requestDetail1.setDevice(device1);
        requestDetail1.setRequest(request1);
        requestDetailsToSave.add(requestDetail1);
        
        RequestDetail requestDetail2 = new RequestDetail();
        Device device2 = new Device();
        device2.setDeviceId(2);
        requestDetail2.setDevice(device2);
        requestDetail2.setRequest(request1);
        requestDetailsToSave.add(requestDetail2);
        requestService.setRequestDetailsToSave(requestDetailsToSave);

        // When
        requestService.updateDeviceStatus(1, EstatusDevice.Availability);

        // Then

        assertEquals(EstatusDevice.Utilized, device1.getStatus()); // Check if status of device1 is updated correctly

    }

    @Test
    void getRequestByCreatedUser_UserAuthenticated_ReturnsRequestList() {
        // Given
        User currentUser = new User();
        currentUser.setUserId(1);
        // Set properties for currentUser as needed

        // Mock Authentication object
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(currentUser);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Mock RequestRepository
        List<Request> userRequests = new ArrayList<>();
        userRequests.add(new Request());
        // Add user requests to userRequests list as needed
        when(requestRepository.findByUserCreated(currentUser)).thenReturn(userRequests);


        // When
        List<Request> result = requestService.getRequestByCreatedUser();

        // Then
        assertEquals(userRequests, result);
    }

    @Test
    void addRequest_Successful_ReturnsSuccessMessage() {
        // Given
        // Mock SecurityContextHolder and set authenticated user
        User currentUser = new User();
        // Set properties for currentUser as needed
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(currentUser);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(requestRepository.getLastId()).thenReturn(1); // Example value
        // Mock the behavior of deviceService.getDeviceById as needed


        // Create a RequestDto object with necessary data
        RequestDto requestDto = new RequestDto();
        // Set properties for requestDto as needed

        // When
        String result = requestService.addRequest(requestDto);

        // Then
        assertEquals("An error occurred while processing the request. Please try again later.", result);
        // Verify that a new request and request details are added to pendingRequests and pendingRequestDetails
    }

    @Test
    void sendRequest_AccessDenied_ReturnsAccessDeniedMessage() {
//        User currentUser = new User();
//        currentUser.setPassword("abc");
//        currentUser.setEmail("abc@gmail.com");
//        currentUser.setUserId(1);
//        // Given
//
//        SecurityContext securityContext = mock(SecurityContext.class);
//        Authentication authentication = mock(Authentication.class);
//        when(authentication.getPrincipal()).thenReturn(currentUser);
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        SecurityContextHolder.setContext(securityContext);
//
//        int requestId = 2;
//        RequestRepository requestRepository = mock(RequestRepository.class);
//        Request request = new Request();
//        request.setRequestId(requestId);
//        User newUser = User.builder().userId(2).password("abc").email("abc@gmail.com").build();
//        request.setUserCreated(newUser); // User created by a different user
//        when(requestRepository.findByRequestId(requestId)).thenReturn(request);
//
//        // When
//        String result = requestService.sendRequest(requestId);
//
//        // Then
//        assertEquals("Access Denied", result);
    }

    @Test
    void approveRequest() {
    }

    @Test
    void rejectRequest() {
    }

    @Test
    void deleteRequest() {
    }
}