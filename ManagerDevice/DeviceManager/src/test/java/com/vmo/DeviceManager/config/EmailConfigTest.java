package com.vmo.DeviceManager.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class EmailConfigTest {
    @InjectMocks
    private EmailConfig emailConfig;
    @Test
    void getJavaMailSender() {
        // Mock values for properties
        String mailHost = "test.host";
        String mailPort = "587";
        String mailUsername = "test@example.com";
        String mailPassword = "password";

        // Set mock values
        emailConfig.mailHost = mailHost;
        emailConfig.mailPort = mailPort;
        emailConfig.mailUsername = mailUsername;
        emailConfig.mailPassword = mailPassword;

        // Call the method to be tested
        JavaMailSender javaMailSender = emailConfig.getJavaMailSender();

        // Verify JavaMailSender configuration
        assertEquals(JavaMailSenderImpl.class, javaMailSender.getClass());
        JavaMailSenderImpl mailSenderImpl = (JavaMailSenderImpl) javaMailSender;
        assertEquals(mailHost, mailSenderImpl.getHost());
        assertEquals(Integer.parseInt(mailPort), mailSenderImpl.getPort());
        assertEquals(mailUsername, mailSenderImpl.getUsername());
        assertEquals(mailPassword, mailSenderImpl.getPassword());
    }
}