package com.vmo.DeviceManager.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailUtilTest {
    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailUtil emailUtil;

    @Test
    void sendOtpEmail() throws MessagingException {
//        String email = "example@example.com";
//        String otp = "123456";
//
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
//        mimeMessageHelper.setTo(email);
//
//        doNothing().when(javaMailSender).send(any(MimeMessage.class));
//
//        emailUtil.sendOtpEmail(email, otp);
//
//        verify(javaMailSender).send(any(MimeMessage.class));
    }

    @Test
    void sendPassEmail() throws MessagingException {
//        String email = "example@example.com";
//        String password = "newPassword";
//
//        MimeMessage mimeMessage = new MimeMessage((Session) null);
//        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
//        mimeMessageHelper.setTo(email);
//
//        doNothing().when(javaMailSender).send(any(MimeMessage.class));
//
//        emailUtil.sendPassEmail(email, password);
//
//        verify(javaMailSender).send(any(MimeMessage.class));
    }
}