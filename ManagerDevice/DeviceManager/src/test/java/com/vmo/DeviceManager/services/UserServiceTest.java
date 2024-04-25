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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    void searchUser() {
    }

    @Test
    void updateUserbyId_UserExists_ReturnsUpdateSuccessful() {
        Department department = Department.builder().departmentId(1).departmentName("DU20").address("abcd").build();
        User user = User.builder()
                .userId(7)
                .email("thachdao53@gmail.com").firstName("dao").lastName("thach")
                .password(passwordEncoder.encode("abcd"))
                .phone(0).role(Erole.USER)
                .status(EstatusUser.Active)
                .department(department).build();
        when(userRepository.findById(7)).thenReturn(Optional.ofNullable(user));

        AuthRequest authRequest = new AuthRequest();
        authRequest.setDepartmentId(1);
        authRequest.setPassword("newPassword");

        User currentUser = new User();
        currentUser.setUserId(7);


        // Mock Authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(currentUser);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Tiếp theo là mock phương thức của departmentRepository và userRepository

        // When
        String result = userService.updateUserbyId(7, authRequest);

        // Then
        assertThat(result).isEqualTo("Update successful");
        verify(passwordEncoder).encode(authRequest.getPassword());

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
                () -> userService.updateUserbyId(9, authRequest));

        // Then
        assertThat(exception).isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User not found with id: " + 9);
    }

    @Test
    void updateUserbyId_AccessDenied_ReturnsAccessDenied() {
        // Given
        int userId = 1;
        AuthRequest authRequest = new AuthRequest();
        authRequest.setDepartmentId(1); // Assuming departmentId is set correctly
        authRequest.setPassword("newPassword"); // Assuming new password is set correctly

        User currentUser = new User();
        currentUser.setUserId(9); // Assuming setUserId method is available

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(currentUser);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        // When
        String result = userService.updateUserbyId(userId, authRequest);

        // Then
        assertThat(result).isEqualTo("Access denied");
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(userRepository);
    }
}
