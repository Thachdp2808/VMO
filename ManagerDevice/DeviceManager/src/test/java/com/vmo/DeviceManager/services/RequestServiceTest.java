package com.vmo.DeviceManager.services;

import com.vmo.DeviceManager.exceptions.model.DeviceException;
import com.vmo.DeviceManager.exceptions.model.RequestException;
import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.Request;
import com.vmo.DeviceManager.models.RequestDetail;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.dto.RequestDto;
import com.vmo.DeviceManager.models.enumEntity.Erole;
import com.vmo.DeviceManager.models.enumEntity.EstatusDevice;
import com.vmo.DeviceManager.models.enumEntity.EstatusRequest;
import com.vmo.DeviceManager.repositories.DeviceRepository;
import com.vmo.DeviceManager.repositories.RequestDetailRepository;
import com.vmo.DeviceManager.repositories.RequestRepository;
import com.vmo.DeviceManager.services.implement.RequestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RequestServiceTest {

    @Mock
    private RequestRepository requestRepository;
@Mock
private RequestDetailRepository requestDetailRepository;
    @InjectMocks
    private RequestServiceImpl requestService;
    @Mock
    private DeviceRepository deviceRepository;
    @Mock DeviceService deviceService;



    @Test
    void getRequestAdmin_NoRequests_ThrowsIllegalArgumentException() {

        // Given
        List<Integer> statuses = Arrays.asList(1, 2, 3);
        when(requestRepository.findAllByStatusIn(statuses)).thenReturn(Collections.emptyList());


        // Then
        RequestException exception = assertThrows(RequestException.class, () -> requestService.getRequestAdmin());
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
        assertThrows(RequestException.class, () -> {
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
    void updateRequest_Success() {
        // Given
        int requestId = 1;
        RequestDto requestDto = new RequestDto();
        requestDto.setDeviceIds(List.of(1, 2));
        requestDto.setStart(LocalDate.now());
        requestDto.setEnd(LocalDate.now().plusDays(1));

        Device device1 = new Device();
        device1.setStatus(EstatusDevice.Availability);

        Device device2 = new Device();
        device2.setStatus(EstatusDevice.Availability);

        Request request = new Request();
        request.setRequestId(requestId);
        request.setRequestDetails(new ArrayList<>());

        when(deviceService.getDeviceById(1)).thenReturn(device1);
        when(deviceService.getDeviceById(2)).thenReturn(device2);

        requestService.setPendingRequests(new ArrayList<>(List.of(request)));

        // When
        String result = requestService.updateRequest(requestId, requestDto);

        // Then
        assertEquals("Update request success", result);
        assertEquals(2, request.getRequestDetails().size());
        verify(deviceService, times(1)).getDeviceById(1);
        verify(deviceService, times(1)).getDeviceById(2);
    }

    @Test
    void updateRequest_RequestNotFound() {
        // Given
        int requestId = 1;
        RequestDto requestDto = new RequestDto();

        // When / Then
        RequestException exception = assertThrows(RequestException.class, () -> {
            requestService.updateRequest(requestId, requestDto);
        });

        assertEquals("Request not found", exception.getMessage());
    }

    @Test
    void updateRequest_NoAvailableDevices() {
        // Given
        int requestId = 1;
        RequestDto requestDto = new RequestDto();
        requestDto.setDeviceIds(List.of(1, 2));

        Device device1 = new Device();
        device1.setStatus(EstatusDevice.Maintenance);

        Device device2 = new Device();
        device2.setStatus(EstatusDevice.Maintenance);

        Request request = new Request();
        request.setRequestId(requestId);

        when(deviceService.getDeviceById(1)).thenReturn(device1);
        when(deviceService.getDeviceById(2)).thenReturn(device2);

        requestService.setPendingRequests(new ArrayList<>(List.of(request)));

        // When / Then
        DeviceException exception = assertThrows(DeviceException.class, () -> {
            requestService.updateRequest(requestId, requestDto);
        });

        assertEquals("No available devices in the list", exception.getMessage());
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
    void getLastId_EmptyPendingRequests_ReturnsRepositoryLastId() {
        // Given
        when(requestRepository.getLastId()).thenReturn(Optional.of(10));

        // When
        int lastId = requestService.getLastId();

        // Then
        assertEquals(10, lastId);
    }

    @Test
    void addRequest_NoAvailableDevices_ThrowsDeviceException() {
        User user = new User();
        user.setUserId(1);
        // Given
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        RequestDto requestDto = new RequestDto();
        requestDto.setDeviceIds(List.of(1, 2));
        when(deviceService.getDeviceById(anyInt())).thenReturn(null);

        // When / Then
        DeviceException exception = assertThrows(DeviceException.class, () -> {
            requestService.addRequest(requestDto);
        });

        assertEquals("No available devices in the list", exception.getMessage());
    }

    @Test
    void addRequest_WithAvailableDevices_ReturnsSuccessMessage() {
        User user = new User();
        user.setUserId(1);
        // Given
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        // Given
        Device device1 = new Device();
        device1.setDeviceId(1);
        device1.setStatus(EstatusDevice.Availability);

        Device device2 = new Device();
        device2.setDeviceId(2);
        device2.setStatus(EstatusDevice.Availability);


        RequestDto requestDto = new RequestDto();
        requestDto.setDeviceIds(List.of(1, 2));
        when(deviceService.getDeviceById(1)).thenReturn(device1);
        when(deviceService.getDeviceById(2)).thenReturn(device2);
        when(requestRepository.getLastId()).thenReturn(Optional.of(1));

        // When
        String result = requestService.addRequest(requestDto);

        // Then
        assertTrue(result.startsWith("Save request successful"));
    }

    @Test
    void sendRequest_Success() {
        // Tạo một đối tượng User giả mạo với quyền USER
        User user = new User();
        user.setRole(Erole.USER);
        user.setUserId(1); // id của user

        // Tạo một đối tượng Authentication giả mạo và đặt người dùng giả mạo là người dùng hiện tại
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Tạo một yêu cầu giả mạo
        Request request = new Request();
        request.setRequestId(1); // id của yêu cầu
        request.setUserCreated(user);

        // Thêm yêu cầu vào danh sách pendingRequests
        requestService.setPendingRequests(Collections.singletonList(request));

        // Gọi phương thức sendRequest
        String result = requestService.sendRequest(1);

        // Kiểm tra xem chuỗi trả về có phải là "Send successful" hay không
        assertEquals("Send successful", result);

        // Kiểm tra xem yêu cầu đã được chuyển sang trạng thái "Processing"
        assertEquals(EstatusRequest.Processing, request.getStatus());
    }

    @Test
    void approveRequest_RequestNotFound() {
        User adminUser = new User();
        adminUser.setRole(Erole.ADMIN);
        // Given
        Authentication authentication = mock(Authentication.class);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // When
        String result = requestService.approveRequest(1);

        // Then
        assertEquals("Request not found", result);
        verifyNoMoreInteractions(requestRepository);

    }

    @Test
    void rejectRequest_Admin_Success() {
        User adminUser = new User();
        adminUser.setUserId(1);
        adminUser.setRole(Erole.ADMIN);
        // Given
        Authentication authentication = new UsernamePasswordAuthenticationToken(adminUser, null);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        List<RequestDetail> list = new ArrayList<>();
        Request request = new Request();
        request.setRequestId(1);
        request.setStatus(EstatusRequest.Pending);
        request.setRequestDetails(list);
        requestService.setRequestsToSave(Collections.singletonList(request));


        // When
        String result = requestService.rejectRequest(1);

        // Then
        assertEquals("Reject successful", result);
        assertEquals(EstatusRequest.Rejected, request.getStatus());
        verify(requestRepository).save(request);
    }

    @Test
    void rejectRequest_RequestNotFound() {
        // Given
        RequestRepository requestRepository = mock(RequestRepository.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User adminUser = new User();
        adminUser.setRole(Erole.ADMIN);

        // When
        RequestException exception = assertThrows(RequestException.class, () -> {
            requestService.rejectRequest(1);
        });
        // Then
        assertEquals("Request not found", exception.getMessage());
        verifyNoMoreInteractions(requestRepository);
    }

    @Test
    void deleteRequest_Success() {
        // Given
        List<Request> pendingRequests = new ArrayList<>();
        List<RequestDetail> pendingRequestDetails = new ArrayList<>();
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User currentUser = new User();
        currentUser.setUserId(1);


        Request requestToDelete = new Request();
        requestToDelete.setRequestId(1);
        requestToDelete.setUserCreated(currentUser);
        pendingRequests.add(requestToDelete);

        RequestDetail requestDetailToDelete = new RequestDetail();
        requestDetailToDelete.setRequest(requestToDelete);
        pendingRequestDetails.add(requestDetailToDelete);
        // When
        String result = requestService.deleteRequest(1);

        // Then
        assertEquals("Delete request success", result);

    }

    @Test
    void deleteRequest_Fail() {
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User currentUser = new User();
        currentUser.setUserId(1);

        // When
        String result = requestService.deleteRequest(2);

        // Then
        assertEquals("Delete request success", result);
    }
}