package com.example.bookdbbackend.service;

import com.example.bookdbbackend.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void testUserExistsById() {
        Long userId = 1L;

        User user = userService.getUserById(userId);

        assertNotNull(user, "User should not be null");

        assertEquals(userId, user.getUser_id(), "User ID should match");
    }
}
