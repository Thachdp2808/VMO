package com.vmo.DeviceManager.services;

import com.amazonaws.services.s3.AmazonS3;
import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.repositories.DeviceRepository;
import com.vmo.DeviceManager.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class StorageServiceTest {

    @Mock
    private AmazonS3 amazonS3;

    @Mock
    private ImageDeviceRepository imageDeviceRepository;

    @Mock
    private ImageUserRepository imageUserRepository;

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StorageService storageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUploadFileForDevice() {
        // Mock data
        File file = new File("test.jpg");
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[10]);
        Device device = new Device();
        device.setDeviceId(1);

        // Mock behavior
        when(deviceRepository.findById(1)).thenReturn(Optional.of(device));

        // Test uploadFile method
        String result = storageService.uploadFile(multipartFile, "device", 1);

        // Verify
        assertEquals("File uploaded : "  + multipartFile.getOriginalFilename(), result);

    }

    @Test
    public void testUploadFileForUser() {
        // Mock data
        File file = new File("test.jpg");
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[10]);
        User user = new User();
        user.setUserId(1);

        // Mock behavior
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        // Test uploadFile method
        String result = storageService.uploadFile(multipartFile, "user", 1);

        // Verify
        assertEquals("File uploaded : " + multipartFile.getOriginalFilename(), result);

    }
    @Test
    public void testDownloadFile() throws IOException {
        // Mock data
//        byte[] fileContent = "Test file content".getBytes();
//        S3Object s3Object = mock(S3Object.class);
//        S3ObjectInputStream inputStream = new S3ObjectInputStream(new ByteArrayInputStream(fileContent), null);
//
//        // Mock behavior
//        when(amazonS3.getObject(anyString(), anyString())).thenReturn(s3Object);
//        when(s3Object.getObjectContent()).thenReturn(inputStream);
//
//        // Test downloadFile method
//        byte[] downloadedFile = storageService.downloadFile("test_file.jpg");
//
//        // Verify
//        assertEquals(fileContent.length, downloadedFile.length);

    }

    @Test
    public void testDeleteFile() {
        // Test deleteFile method
        String result = storageService.deleteFile("test_file.jpg");

        // Verify
        assertEquals("test_file.jpg removed ...", result);

    }

    @Test
    public void testConvertMultiPartFileToFile() throws IOException {
        // Mock data
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "Test file content".getBytes());

        // Test convertMultiPartFileToFile method
        File convertedFile = storageService.convertMultiPartFileToFile(multipartFile);

        // Verify
        assertEquals(multipartFile.getOriginalFilename(), convertedFile.getName());
    }
}