package com.example.bookdbbackend.service;

import com.example.bookdbbackend.dtos.RegisterUserDto;
import com.example.bookdbbackend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    AuthenticationService userService; // Real service implementation

    @BeforeEach
    void setUp() {
        // Any setup if needed
    }

    @Test
    void addUserPerformanceTest() {
        long startTime = System.currentTimeMillis();


        for (long i = 1; i < 2; i++) {
            RegisterUserDto user = new RegisterUserDto(

            );


            userService.signUp(user);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        assertTrue(duration < 500, "Performance issue: Adding users took too long.");
    }
}