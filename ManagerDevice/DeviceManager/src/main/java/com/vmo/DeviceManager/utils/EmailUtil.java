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

    public void sendOtpEmail(String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Verify OTP");
        mimeMessageHelper.setText("""
        <div>
          <a href="http://localhost:8080/verify-account?email=%s&otp=%s" target="_blank">click link to verify</a>
        </div>
        """.formatted(email, otp), true);

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
