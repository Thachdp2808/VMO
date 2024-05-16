package com.vmo.DeviceManager.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class OtpUtilTest {

    @Test
    void generateOtp() {
        OtpUtil otpUtil = new OtpUtil();
        String otp = otpUtil.generateOtp();
        // Kiểm tra xem OTP có đúng 6 chữ số không
        assertEquals(6, otp.length());
        // Kiểm tra xem OTP có phải là chuỗi số không
        assertTrue(otp.matches("\\d+"));
    }
}