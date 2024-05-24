package com.vmo.DeviceManager.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.vmo.DeviceManager.exceptions.model.DeviceException;
import com.vmo.DeviceManager.exceptions.model.UserException;
import com.vmo.DeviceManager.models.Device;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.repositories.DeviceRepository;
import com.vmo.DeviceManager.repositories.UserRepository;
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
    private DeviceRepository deviceRepository;

    @Autowired
    private UserRepository userRepository;

    public String uploadFile(MultipartFile file, String type, int id) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName =   file.getOriginalFilename();
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        fileObj.delete();

        if(type.equals("device")){
            saveImageDevice(amazonS3.getUrl(bucketName, fileName).toString(), id);
        }
        if(type.equals("user")){
            saveImageUser(amazonS3.getUrl(bucketName, fileName).toString(), id);
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
        Device device = deviceRepository.findById(id).orElseThrow(()-> new DeviceException(id));
        device.setImages(fileName);
        deviceRepository.save(device);
    }

    private void saveImageUser(String fileName, int id){
        User user = userRepository.findById(id).orElseThrow(()-> new UserException(id));
        user.setImages(fileName);
        userRepository.save(user);
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
