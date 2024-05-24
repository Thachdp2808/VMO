package com.vmo.DeviceManager.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendNewUser(String email, String password) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Welcome to Device Management");
        mimeMessageHelper.setText("""
        <p><strong>Email:</strong> <br>
           User: %s<br>
           Pass: %s
        </p>
        <p>Thanks!</p>
        """.formatted(email, password), true);
        javaMailSender.send(mimeMessage);
    }

    public void sendOtpEmail(String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Verify OTP");
        mimeMessageHelper.setText("""
         <h2>We got a request to reset your password.</h2>
        <p><strong>OTP :</strong> %s</p>
        """.formatted( otp), true);
        javaMailSender.send(mimeMessage);
    }

    public void sendPassEmail(String email, String password) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Reset Password");
        mimeMessageHelper.setText("""
        <h2>We got a request to reset your password.</h2>
        <p><strong>New Password:</strong> %s</p>
         """.formatted(password), true);

        javaMailSender.send(mimeMessage);
    }
}
