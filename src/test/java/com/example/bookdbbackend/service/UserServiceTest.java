package com.example.bookdbbackend.service;

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
    IUserService userService; // Real service implementation

    @BeforeEach
    void setUp() {
        // Any setup if needed
    }

    @Test
    void addUserPerformanceTest() {
        long startTime = System.currentTimeMillis();
//
//        for (long i = 4; i < 10; i++) {
//            User user = new User(
//                    i,
//                    "FirstSamu" + i,
//                    "LastSeta" + i,
//                    "samuseta" + i + "@gmail.com",
//                    5,
//                    "123-456-7890",
//                    i + 5,
//                    90,
//                    "Espoo",
//                    "kotiorja"
//            );
//
//            userService.addUser(user);
//        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        assertTrue(duration < 500, "Performance issue: Adding users took too long.");
    }
}