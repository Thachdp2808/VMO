package com.vmo.DeviceManager.config;

import com.vmo.DeviceManager.models.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RequestListenerConfig {
    @Bean
    public Request.RequestEntityListener requestEntityListener() {
        return new Request.RequestEntityListener();
    }
}
