package com.example.bookdbbackend.controller;

import com.example.bookdbbackend.dtos.LoginUserDto;
import com.example.bookdbbackend.dtos.RegisterUserDto;
import com.example.bookdbbackend.model.User;
import com.example.bookdbbackend.responses.LoginResponse;
import com.example.bookdbbackend.service.AuthenticationService;
import com.example.bookdbbackend.service.JwtService;
import com.example.bookdbbackend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private User testUser;
    private RegisterUserDto registerUserDto;
    private LoginUserDto loginUserDto;
    private static final String TEST_TOKEN = "test.jwt.token";
    private static final long TEST_EXPIRATION = 3600000;

    @BeforeEach
    void setUp() {
        // Initialize test data
        testUser = new User();
        testUser.setUser_id(1L);
        testUser.setFirst_name("John");
        testUser.setLast_name("Doe");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setRole("USER");
        // Optional fields can be set if needed for specific tests
        testUser.setStreet_number(123);
        testUser.setStreet_name("Test Street");
        testUser.setPhone_number("123-456-7890");
        testUser.setPostal_code(12345);
        testUser.setProvince("Test Province");

        registerUserDto = new RegisterUserDto();
        registerUserDto.setEmail("test@example.com");
        registerUserDto.setPassword("password123");
        registerUserDto.setFirst_name("John");
        registerUserDto.setLast_name("Doe");

        loginUserDto = new LoginUserDto();
        loginUserDto.setEmail("test@example.com");
        loginUserDto.setPassword("password123");
    }

    @Test
    void addUser_ShouldRegisterNewUser() {
        // Arrange
        when(authenticationService.signUp(any(RegisterUserDto.class))).thenReturn(testUser);

        // Act
        ResponseEntity<User> response = authController.addUser(registerUserDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
        verify(authenticationService, times(1)).signUp(registerUserDto);
    }

    @Test
    void loginUser_ShouldAuthenticateAndReturnToken() {
        // Arrange
        when(authenticationService.authenticate(any(LoginUserDto.class))).thenReturn(testUser);
        when(jwtService.generateToken(testUser)).thenReturn(TEST_TOKEN);
        when(jwtService.getExpirationTime()).thenReturn(TEST_EXPIRATION);

        // Act
        ResponseEntity<LoginResponse> response = authController.loginUser(loginUserDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TEST_TOKEN, response.getBody().getToken());
        assertEquals(TEST_EXPIRATION, response.getBody().getExpiresIn());

        verify(authenticationService, times(1)).authenticate(loginUserDto);
        verify(jwtService, times(1)).generateToken(testUser);
        verify(jwtService, times(1)).getExpirationTime();
    }

    @Test
    void getAuthenticatedUser_ShouldReturnUserDetails() {
        // Arrange
        String authHeader = "Bearer " + TEST_TOKEN;
        when(jwtService.extractUsername(TEST_TOKEN)).thenReturn(testUser.getEmail());
        when(userService.getUserByEmail(testUser.getEmail())).thenReturn(testUser);

        // Act
        ResponseEntity<User> response = authController.getAuthenticatedUser(authHeader);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());

        verify(jwtService, times(1)).extractUsername(TEST_TOKEN);
        verify(userService, times(1)).getUserByEmail(testUser.getEmail());
    }

    @Test
    void getAuthenticatedUser_WithInvalidToken_ShouldHandleError() {
        // Arrange
        String invalidToken = "Bearer invalid.token";
        when(jwtService.extractUsername("invalid.token"))
                .thenThrow(new RuntimeException("Invalid token"));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                authController.getAuthenticatedUser(invalidToken)
        );

        verify(jwtService, times(1)).extractUsername("invalid.token");
        verify(userService, never()).getUserByEmail(any());
    }
}