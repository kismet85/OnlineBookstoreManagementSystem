package com.example.bookdbbackend.service;

import com.example.bookdbbackend.model.User;
import com.example.bookdbbackend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void testGetUserById() {
        // Prepare test data
        Long userId = 1L;
        User mockUser = new User(); // Create a new User object
        mockUser.setUser_id(userId); // Set the user ID to match

        // Mock the behavior of userRepository
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser)); // Assuming findById returns Optional<User>

        // Call the method to test
        User user = userService.getUserById(userId);

        // Verify the user is not null
        assertNotNull(user);

        // Verify the user ID matches
        assertEquals(userId, user.getUser_id());
    }
}
