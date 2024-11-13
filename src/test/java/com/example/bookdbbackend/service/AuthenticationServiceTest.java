package com.example.bookdbbackend.service;

import com.example.bookdbbackend.dtos.LoginUserDto;
import com.example.bookdbbackend.dtos.RegisterUserDto;
import com.example.bookdbbackend.exception.UserAlreadyExistsException;

import com.example.bookdbbackend.model.User;

import com.example.bookdbbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignUp() {
        RegisterUserDto input = new RegisterUserDto();
        input.setEmail("test@example.com");
        input.setFirst_name("John");
        input.setLast_name("Doe");
        input.setPassword("password");

        when(userRepository.existsUserByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        User result = authenticationService.signUp(input);

        assertNotNull(result);
        verify(userRepository, times(1)).existsUserByEmail(input.getEmail());
        verify(passwordEncoder, times(1)).encode(input.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testSignUpUserAlreadyExists() {
        RegisterUserDto input = new RegisterUserDto();
        input.setEmail("test@example.com");

        when(userRepository.existsUserByEmail(anyString())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> authenticationService.signUp(input));
        verify(userRepository, times(1)).existsUserByEmail(input.getEmail());
    }

    @Test
    void testAuthenticate() {
        LoginUserDto input = new LoginUserDto();
        input.setEmail("test@example.com");
        input.setPassword("password");

        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));

        User result = authenticationService.authenticate(input);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findUserByEmail(input.getEmail());
    }

    @Test
    void testAuthenticateUserNotFound() {
        LoginUserDto input = new LoginUserDto();
        input.setEmail("test@example.com");
        input.setPassword("password");

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authenticationService.authenticate(input));
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findUserByEmail(input.getEmail());
    }

}