package com.vmo.DeviceManager.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WebConfigTest {
    @Mock
    private MockMvc mockMvc;

    @Test
    public void testCorsConfiguration() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/api/v1/admin/endpoint")  // Replace with your API endpoint
//                        .header("Origin", "http://localhost:3000"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.header().exists("Access-Control-Allow-Origin"))
//                .andExpect(MockMvcResultMatchers.header().string("Access-Control-Allow-Origin", "http://localhost:3000"));

    }

    @Test
    public void testCorsConfigurationNotAllowedOrigin() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/api/v1/admin/endpoint")  // Replace with your API endpoint
//                        .header("Origin", "http://example.com"))  // Using a different origin
//                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}