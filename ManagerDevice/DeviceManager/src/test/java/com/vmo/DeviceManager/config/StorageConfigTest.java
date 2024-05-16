package com.vmo.DeviceManager.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Value;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class StorageConfigTest {
    @Mock
    private StorageConfig storageConfig;

    @MockBean
    private AmazonS3ClientBuilder amazonS3ClientBuilder;
    @Test
    void s3Client() {
//        String accessKey = "${cloud.aws.credentials.access-key}";
//        String accessSecret = "${cloud.aws.credentials.secret-key}";
//        String region = "${cloud.aws.region.static}";
//
//        // Set mock values
//        storageConfig.accessKey = accessKey;
//        storageConfig.accessSecret = accessSecret;
//        storageConfig.region = region;
//
//
//        AWSCredentials credentials = Mockito.mock(AWSCredentials.class);
//        Mockito.when(credentials.getAWSAccessKeyId()).thenReturn(accessKey);
//        Mockito.when(credentials.getAWSSecretKey()).thenReturn(accessSecret);
//
//        // Mock the AmazonS3 object
//        AmazonS3 amazonS3 = Mockito.mock(AmazonS3.class);
//
//        // Mock the AmazonS3ClientBuilder
//        Mockito.when(amazonS3ClientBuilder.standard()).thenReturn(amazonS3ClientBuilder);
//        Mockito.when(amazonS3ClientBuilder.withCredentials(any())).thenReturn(amazonS3ClientBuilder);
//        Mockito.when(amazonS3ClientBuilder.withRegion(eq(region))).thenReturn(amazonS3ClientBuilder);
//        Mockito.when(amazonS3ClientBuilder.build()).thenReturn(amazonS3);
//
//        // Call the s3Client method
//        AmazonS3 s3Client = storageConfig.s3Client();
//
//        // Verify that the correct credentials and region are used to create the client
//        Mockito.verify(amazonS3ClientBuilder).withCredentials(any());
//        Mockito.verify(amazonS3ClientBuilder).withRegion(eq(region));
//
//        // Verify that the returned AmazonS3 client is not null
//        assertEquals(amazonS3, s3Client);
    }
}