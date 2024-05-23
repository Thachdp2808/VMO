package com.vmo.DeviceManager.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.ImageDevice;
import com.vmo.DeviceManager.models.ImageUser;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.repositories.DeviceRepository;
import com.vmo.DeviceManager.repositories.ImageDeviceRepository;
import com.vmo.DeviceManager.repositories.ImageUserRepository;
import com.vmo.DeviceManager.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class StorageService {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private ImageDeviceRepository imageDeviceRepository;

    @Autowired
    private ImageUserRepository imageUserRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserRepository userRepository;

    public String uploadFile(MultipartFile file, String type, int id) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =   file.getOriginalFilename();
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        fileObj.delete();

        if(type.equals("device")){
            saveImageDevice(fileName, id);
        }
        if(type.equals("user")){
            saveImageUser(fileName, id);
        }

        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    public String UploadFileDashboard(String fileName, String fileContent) {
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileContent.length());
        amazonS3.putObject(bucketName, fileName, inputStream, metadata);
        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    private void saveImageDevice(String fileName, int id){
        ImageDevice imageDevice = new ImageDevice();
        imageDevice.setImageLink(fileName);
        imageDevice.setName(fileName);
        Device Device = deviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Device does not exits"));
        imageDevice.setImageDevice(Device);
        imageDeviceRepository.save(imageDevice);
    }

    private void saveImageUser(String fileName, int id){
        ImageUser imageUser = new ImageUser();
        imageUser.setImageLink(fileName);
        imageUser.setName(fileName);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        imageUser.setUser(user);
        imageUserRepository.save(imageUser);
    }


    public byte[] downloadFile(String fileName) {
        S3Object s3Object = amazonS3.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String deleteFile(String fileName) {
        amazonS3.deleteObject(bucketName, fileName);
        return fileName + " removed ...";
    }


    File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }
}
