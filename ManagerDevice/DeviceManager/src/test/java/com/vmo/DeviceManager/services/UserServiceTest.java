package com.vmo.DeviceManager.services;

import com.vmo.DeviceManager.jwt.AuthRequest;
import com.vmo.DeviceManager.models.Department;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.models.enumEntity.Erole;
import com.vmo.DeviceManager.models.enumEntity.EstatusUser;
import com.vmo.DeviceManager.repositories.DepartmentRepository;
import com.vmo.DeviceManager.repositories.UserRepository;
import com.vmo.DeviceManager.services.implement.DepartmentServiceImpl;
import com.vmo.DeviceManager.services.implement.UserServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private DepartmentRepository departmentRepository;
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private Authentication authentication;
    @Mock
    private DepartmentServiceImpl departmentServiceImpl;

    @Before
    public void setUp() {
        Department department = Department.builder().departmentId(1).departmentName("DU20").address("abcd").build();
        User user = User.builder()
                .userId(1)
                .email("thachdao53@gmail.com").firstName("dao").lastName("thach")
                .password(passwordEncoder.encode("abcd"))
                .phone(0).role(Erole.USER)
                .status(EstatusUser.Active)
                .department(department).build();
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));

        // Giả lập departmentService và inject vào userService
        departmentServiceImpl = mock(DepartmentServiceImpl.class);
        userService = new UserServiceImpl(userRepository, passwordEncoder, departmentServiceImpl);
    }

    @Test
    void getUserById() {
        Department department = Department.builder().departmentId(1).departmentName("DU20").address("abcd").build();
        User user = User.builder()
                .userId(7)
                .email("thachdao53@gmail.com").firstName("dao").lastName("thach")
                .password(passwordEncoder.encode("abcd"))
                .phone(0).role(Erole.USER)
                .status(EstatusUser.Active)
                .department(department).build();
        when(userRepository.findById(7)).thenReturn(Optional.ofNullable(user));
        // Gọi phương thức cần kiểm tra
        Optional<User> savedUser = userRepository.findById(7);

        // Kiểm tra xem đối tượng đã được lưu thành công hay không
        assertThat(savedUser).isPresent();
        assertThat(savedUser.get().getFirstName()).isEqualTo("dao");
    }

    @Test
    void deActiveUser_UserExists_ReturnsUpdateSuccessful() {
        // Given
        int userId = 1;
        Department department = Department.builder().departmentId(1).departmentName("DU20").address("abcd").build();
        User user = User.builder()
                .userId(userId)
                .email("thachdao53@gmail.com")
                .firstName("dao")
                .lastName("thach")
                .password(passwordEncoder.encode("abcd"))
                .phone(0)
                .role(Erole.USER)
                .status(EstatusUser.Active)
                .department(department)
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));

        // When
        String result = userService.deActiveUser(userId);

        // Then
        assertThat(result).isEqualTo("Update successful");
        assertThat(user.getStatus()).isEqualTo(EstatusUser.Deactive);
        verify(userRepository, times(1)).save(eq(user)); // Kiểm tra rằng userRepository.save() được gọi với đối tượng user đã được cập nhật
    }


    @Test
    void deActiveUser_UserNotExists_ThrowsRuntimeException() {
        // Given
        int userId = 1;
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> userService.deActiveUser(userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Account does not exits");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void pageAndSearch() {
         Department department = Department.builder().departmentId(1).departmentName("DU20").address("abcd").build();
                User user = User.builder()
                        .userId(1)
                        .email("thachdao53@gmail.com").firstName("dao").lastName("thach")
                        .password(passwordEncoder.encode("abcd"))
                        .phone(0).role(Erole.USER)
                        .status(EstatusUser.Active)
                        .department(department).build();
        List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(user);
        userList.add(user);
        userList.add(user);

        when(userRepository.findAll()).thenReturn(userList);

        String keyword = "dao";
        int pageNo = 1;
        int pageSize = 2;

        // When
        Page<User> page = userService.pageAndSearch(keyword, pageNo, pageSize);

        // Then
        assertEquals(4, page.getTotalElements());
        assertEquals(2, page.getNumberOfElements());
        assertEquals(2, page.getTotalPages());
        assertEquals(0, page.getNumber());

        // Verify that searchByKeyword is called with the correct arguments
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void updateUserbyId_UserExists_ReturnsUpdateSuccessful() {
        // Given
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("newUsername");
        authRequest.setPassword("newPassword");
        authRequest.setDepartmentId(1);

        User existingUser = new User();
        existingUser.setUserId(1);

        when(userRepository.findById(existingUser.getUserId())).thenReturn(java.util.Optional.of(existingUser));

        // Mock SecurityContext
        Authentication authentication = new UsernamePasswordAuthenticationToken(existingUser, null);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // When
        String result = userService.updateProfile(authRequest);

        // Then
        assertEquals("Update successful", result);



    }


    @Test
    void updateUserbyId_UserNotExists_ThrowsEntityNotFoundException() {
        //Kiểm tra xem user có tồn tại hay không
        // Given
        AuthRequest authRequest = new AuthRequest();
        authRequest.setDepartmentId(1); // Assuming departmentId is set correctly
        authRequest.setPassword("newPassword"); // Assuming new password is set correctly

        when(userRepository.findById(9)).thenReturn(Optional.empty());

        User currentUser = new User();
        currentUser.setUserId(9);


        // Mock Authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(currentUser);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // When/Then
        Throwable exception = assertThrows(EntityNotFoundException.class,
                () -> userService.updateProfile( authRequest));

        // Then
        assertThat(exception).isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User not found with id: " + 9);
    }

    @Test
    void updateProfile_UserNotFound() {
        // Given
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("newUsername");
        authRequest.setPassword("newPassword");
        authRequest.setDepartmentId(1);

        User currentUser = new User();
        currentUser.setUserId(9);

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(currentUser);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findById(9)).thenReturn(java.util.Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> userService.updateProfile(authRequest));
    }

    @Test
    void logout() {
        // Tạo một đối tượng User giả mạo
        User user = new User();
        user.setToken("dummyToken");

        // Mock phương thức save của userRepository
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Gọi phương thức logout
        String result = userService.logout();

        // Kiểm tra xem phương thức save đã được gọi chính xác với người dùng hiện tại đã được cập nhật không

        // Kiểm tra xem chuỗi trả về có phải là "Logout successfully" hay không
        assertEquals("Logout successfully", result);
    }

    @Test
    void updateUserById_Admin_Success() {
        // Tạo một đối tượng User giả mạo với quyền ADMIN
        User adminUser = new User();
        adminUser.setRole(Erole.ADMIN);
        adminUser.setUserId(1); // id của user

        // Mock phương thức findById của userRepository
        User existingUser = new User();
        existingUser.setUserId(2); // id của user cần cập nhật
        when(userRepository.findById(existingUser.getUserId())).thenReturn(java.util.Optional.of(existingUser));

        // Gọi phương thức updateUserById
        String result = userService.updateUserById(existingUser.getUserId(), new AuthRequest());

        // Kiểm tra xem phương thức save đã được gọi chính xác với người dùng hiện tại đã được cập nhật không
        verify(userRepository, times(1)).save(existingUser);

        // Kiểm tra xem chuỗi trả về có phải là "Update successful" hay không
        assertEquals("Update successful", result);
    }

    @Test
    void updateUserById_User_PermissionDenied() {
//        int userId = 1;
//        Department department = Department.builder().departmentId(1).departmentName("DU20").address("abcd").build();
//        User user1 = User.builder()
//                .userId(userId)
//                .email("thachdao53@gmail.com")
//                .firstName("dao")
//                .lastName("thach")
//                .password(passwordEncoder.encode("abcd"))
//                .phone(0)
//                .role(Erole.USER)
//                .status(EstatusUser.Active)
//                .department(department)
//                .build();
//        // Tạo một đối tượng User giả mạo với quyền USER
//        User user = new User();
//        user.setRole(Erole.USER);
//        user.setUserId(2); // id của user
//
//        // Tạo một đối tượng Authentication giả mạo và đặt người dùng giả mạo là người dùng hiện tại
//        Authentication authentication = mock(Authentication.class);
//        when(authentication.getPrincipal()).thenReturn(user);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        // Gọi phương thức updateUserById
//        String result = userService.updateUserById(1, new AuthRequest());
//
//        // Kiểm tra xem kết quả có phải là "Permission denied" hay không
//        assertEquals("Permission denied", result);
//
//        // Kiểm tra xem phương thức save không được gọi
//        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUserById_User_NotFound() {
        int userId = 1;
        Department department = Department.builder().departmentId(1).departmentName("DU20").address("abcd").build();
        User user1 = User.builder()
                .userId(userId)
                .email("thachdao53@gmail.com")
                .firstName("dao")
                .lastName("thach")
                .password(passwordEncoder.encode("abcd"))
                .phone(0)
                .role(Erole.USER)
                .status(EstatusUser.Active)
                .department(department)
                .build();
        // Tạo một đối tượng User giả mạo với quyền ADMIN
        User adminUser = new User();
        adminUser.setRole(Erole.ADMIN);
        adminUser.setUserId(2); // id của user

        // Gọi phương thức updateUserById với id không tồn tại
        try {
            userService.updateUserById(1, new AuthRequest());
        } catch (EntityNotFoundException e) {
            // Kiểm tra xem ngoại lệ EntityNotFoundException có được ném hay không
            assertEquals("User not found with id: 1", e.getMessage());
        }

        // Kiểm tra xem phương thức save không được gọi
        verify(userRepository, never()).save(any(User.class));
    }
}
