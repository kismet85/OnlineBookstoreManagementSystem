package com.example.configs;

import com.example.bookdbbackend.BookdbbackendApplication;
import com.example.bookdbbackend.configs.ApplicationConfig;
import com.example.bookdbbackend.configs.SecurityConfiguration;
import com.example.bookdbbackend.dtos.LoginUserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BookdbbackendApplication.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = {ApplicationConfig.class, SecurityConfiguration.class})
@Import({ApplicationConfig.class, SecurityConfiguration.class})
@ActiveProfiles("test")
class SecurityConfigurationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAdminEndpointsWithoutAuth() throws Exception {
        mockMvc.perform(get("/admin")).andExpect(status().isForbidden());
    }
}