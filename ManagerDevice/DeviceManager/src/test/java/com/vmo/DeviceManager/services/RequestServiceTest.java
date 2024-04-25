package com.vmo.DeviceManager.services;

import com.vmo.DeviceManager.models.Request;
import com.vmo.DeviceManager.models.RequestDetail;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.dto.RequestDto;
import com.vmo.DeviceManager.repositories.RequestRepository;
import com.vmo.DeviceManager.services.implement.RequestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

    @Test
    void getRequestAdmin_RequestsExist_ReturnsRequestList() {

        List<Request> existingRequests = new ArrayList<>();
        existingRequests.add(new Request());
        existingRequests.add(new Request());
        List<Request> savedRequests = new ArrayList<>();
        savedRequests.add(new Request());
        savedRequests.add(new Request());
        List<Integer> statuses = Arrays.asList(1, 2, 3);

        List<Request> allRquests = new ArrayList<>();
        allRquests.addAll(existingRequests);
        allRquests.addAll(savedRequests);
        when(requestRepository.findAllByStatusIn(statuses)).thenReturn(allRquests);
        // When
        List<Request> result = requestService.getRequestAdmin();

        // Then
        assertFalse(result.isEmpty());
        assertEquals(4, result.size()); // 2 requests from existingRequests + 2 requests from savedRequests
    }

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
    void findByRequestId_RequestExists_ReturnsRequest() {
        // Given
        int requestId = 1;
        Request expectedRequest = new Request();
        expectedRequest.setRequestId(requestId);

        // Mock pendingRequests list
        List<Request> pendingRequests = new ArrayList<>();
        pendingRequests.add(expectedRequest);

        // Mock RequestService




        // When
        Request actualRequest = requestService.findByRequestId(requestId);

        // Then
        assertEquals(expectedRequest, actualRequest);
    }

    @Test
    void findByRequestId_RequestDoesNotExist_ReturnsNull() {
        // Given
        int requestId = 1;

        // Mock pendingRequests list
        List<Request> pendingRequests = new ArrayList<>();

        // Mock RequestService
        when(requestService.findByRequestId(requestId)).thenReturn((Request) pendingRequests);

        // When
        Request actualRequest = requestService.findByRequestId(requestId);

        // Then
        assertNull(actualRequest);
    }

    @Test
    void updateRequest_ValidRequestDto_UpdatesRequest() {
        // Given
        int requestId = 1;
        RequestDto requestDto = new RequestDto();
        // Set properties for requestDto as needed

        // Mock pendingRequests list
        List<Request> pendingRequests = new ArrayList<>();
        Request exitRequest = new Request();
        exitRequest.setRequestId(requestId);
        // Add exitRequest to pendingRequests
        pendingRequests.add(exitRequest);

        // Mock pendingRequestDetails list
        List<RequestDetail> pendingRequestDetails = new ArrayList<>();
        // Add pendingRequestDetails as needed
        // Set pendingRequests and pendingRequestDetails in requestService
//        requestService.setPendingRequests(pendingRequests);
//        requestService.setPendingRequestDetails(pendingRequestDetails);
//        // Set deviceService in requestService
//        requestService.setDeviceService(deviceService);

        // When
        requestService.updateRequest(requestId, requestDto);

        // Then
        // Add assertions to verify that the request has been updated correctly
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
        assertEquals("Save request successful", result);
        // Verify that a new request and request details are added to pendingRequests and pendingRequestDetails
    }

    @Test
    void sendRequest_RequestExistsAndCurrentUserIsOwner_ReturnsSuccessMessage() {
        // Given
        int userID = 1;
        // Mock SecurityContextHolder and set authenticated user
        User currentUser = new User();
        currentUser.setUserId(userID);
        // Set properties for currentUser as needed
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(currentUser);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        // Create a Request object with necessary data


        Request request = new Request();
        request.setRequestId(1);
        request.setUserCreated(currentUser);
        when(requestRepository.findByRequestId(userID)).thenReturn(request);

        // When
        String result = requestService.sendRequest(request.getRequestId());

        // Then
        assertEquals("Send successful", result);
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