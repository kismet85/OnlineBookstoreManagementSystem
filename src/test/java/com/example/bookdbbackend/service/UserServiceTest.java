package com.example.bookdbbackend.service;

import com.example.bookdbbackend.exception.UserAlreadyExistsException;
import com.example.bookdbbackend.exception.UserNotFoundException;
import com.example.bookdbbackend.model.User;
import com.example.bookdbbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUser_id(1L);
        user.setFirst_name("John");
        user.setLast_name("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
    }
    @Test
    void testGetUsers() {
        User user1 = new User();
        user1.setUser_id(1L);
        user1.setFirst_name("John");
        user1.setLast_name("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("password");

        User user2 = new User();
        user2.setUser_id(2L);
        user2.setFirst_name("Jane");
        user2.setLast_name("Doe");
        user2.setEmail("jane.doe@example.com");
        user2.setPassword("password");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<User> users = userService.getUsers();

        assertNotNull(users);
        assertEquals(2, users.size());
        assertNull(users.get(0).getPassword());
        assertNull(users.get(1).getPassword());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById_UserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals("John", foundUser.getFirst_name());
        assertEquals("Doe", foundUser.getLast_name());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(1L);
        });

        assertEquals("User not found with id: 1", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testAddUser_UserAlreadyExists() {
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.addUser(user);
        });

        verify(userRepository, times(1)).findUserByEmail(user.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testAddUser_Success() {
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.addUser(user);

        assertNotNull(savedUser);
        assertEquals("John", savedUser.getFirst_name());
        assertEquals("Doe", savedUser.getLast_name());
        verify(userRepository, times(1)).findUserByEmail(user.getEmail());
        verify(bCryptPasswordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(bCryptPasswordEncoder.encode("newPassword")).thenReturn("encodedPassword");

        Map<String, Object> updates = Map.of(
                "first_name", "Jane",
                "last_name", "Smith",
                "street_number", 123,
                "street_name", "Main St",
                "phone_number", "123-456-7890",
                "postal_code", 12345,
                "province", "CA",
                "password", "newPassword",
                "email", "jane.smith@example.com"
        );

        User updatedUser = userService.updateUser(updates, 1L);

        assertNotNull(updatedUser);
        assertEquals("Jane", updatedUser.getFirst_name());
        assertEquals("Smith", updatedUser.getLast_name());
        assertEquals(123, updatedUser.getStreet_number());
        assertEquals("Main St", updatedUser.getStreet_name());
        assertEquals("123-456-7890", updatedUser.getPhone_number());
        assertEquals(12345, updatedUser.getPostal_code());
        assertEquals("CA", updatedUser.getProvince());
        assertEquals("encodedPassword", updatedUser.getPassword());
        assertEquals("jane.smith@example.com", updatedUser.getEmail());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
        verify(bCryptPasswordEncoder, times(1)).encode("newPassword");
    }

    @Test
    void testDeleteUser_UserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(1L);
        });

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).deleteById(1L);
    }
    @Test
    void testGetUserByEmail_UserExists() {
        when(userRepository.findUserByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        User foundUser = userService.getUserByEmail("john.doe@example.com");

        assertNotNull(foundUser);
        assertEquals("John", foundUser.getFirst_name());
        assertEquals("Doe", foundUser.getLast_name());
        verify(userRepository, times(1)).findUserByEmail("john.doe@example.com");
    }

    @Test
    void testGetUserByEmail_UserNotFound() {
        when(userRepository.findUserByEmail("john.doe@example.com")).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getUserByEmail("john.doe@example.com");
        });

        assertEquals("User not found with email: john.doe@example.com", exception.getMessage());
        verify(userRepository, times(1)).findUserByEmail("john.doe@example.com");
    }

    @Test
    void testSearchUsers() {
        User user1 = new User();
        user1.setUser_id(1L);
        user1.setFirst_name("John");
        user1.setLast_name("Doe");
        user1.setEmail("john.doe@example.com");

        User user2 = new User();
        user2.setUser_id(2L);
        user2.setFirst_name("Jane");
        user2.setLast_name("Doe");
        user2.setEmail("jane.doe@example.com");

        when(userRepository.getUserByEmailContainingIgnoreCase("doe")).thenReturn(List.of(user1, user2));

        List<User> users = userService.searchUsers("doe");

        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userRepository, times(1)).getUserByEmailContainingIgnoreCase("doe");
    }

}