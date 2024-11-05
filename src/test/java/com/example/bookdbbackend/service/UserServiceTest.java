package com.example.bookdbbackend.service;

import com.example.bookdbbackend.exception.UserNotFoundException;
import com.example.bookdbbackend.model.User;
import com.example.bookdbbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test") // Use the test profile to load application-test.properties
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();

        user = new User();
        user.setUser_id(1L);
        user.setFirst_name("John");
        user.setLast_name("Doe");

        userRepository.save(user);
    }

    @Test
    public void testGetUserById_UserExists() {
        User foundUser = userService.getUserById(1L);
        assertEquals("John", foundUser.getFirst_name());
        assertEquals("Doe", foundUser.getLast_name());
    }

    @Test
    public void testGetUserById_UserNotFound() {
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(3L);
        });

        assertEquals("User not found with id: 3", exception.getMessage());
    }
}