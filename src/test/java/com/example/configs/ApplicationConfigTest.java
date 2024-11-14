package com.example.bookdbbackend.configs;

import com.example.bookdbbackend.model.User;
import com.example.bookdbbackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationConfigTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @InjectMocks
    private ApplicationConfig applicationConfig;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUser_id(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setFirst_name("John");
        testUser.setLast_name("Doe");
        testUser.setRole("USER");
    }

    @Test
    void userDetailsService_WhenUserExists_ShouldReturnUserDetails() {
        // Arrange
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(testUser));
        UserDetailsService userDetailsService = applicationConfig.userDetailsService();

        // Act
        var userDetails = userDetailsService.loadUserByUsername("test@example.com");

        // Assert
        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        verify(userRepository, times(1)).findUserByEmail("test@example.com");
    }

    @Test
    void userDetailsService_WhenUserNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
        UserDetailsService userDetailsService = applicationConfig.userDetailsService();

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("nonexistent@example.com")
        );
        verify(userRepository, times(1)).findUserByEmail("nonexistent@example.com");
    }

    @Test
    void passwordEncoder_ShouldReturnBCryptPasswordEncoder() {
        // Act
        var passwordEncoder = applicationConfig.passwordEncoder();

        // Assert
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);

        // Verify encoding works
        String password = "testPassword";
        String encodedPassword = passwordEncoder.encode(password);
        assertNotEquals(password, encodedPassword);
        assertTrue(passwordEncoder.matches(password, encodedPassword));
    }

    @Test
    void authenticationManager_ShouldReturnAuthenticationManager() throws Exception {
        // Arrange
        AuthenticationManager expectedManager = mock(AuthenticationManager.class);
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(expectedManager);

        // Act
        var authenticationManager = applicationConfig.authenticationManager(authenticationConfiguration);

        // Assert
        assertNotNull(authenticationManager);
        assertEquals(expectedManager, authenticationManager);
        verify(authenticationConfiguration, times(1)).getAuthenticationManager();
    }

    @Test
    void authenticationProvider_ShouldReturnAuthenticationProvider() {
        // Act
        AuthenticationProvider authProvider = applicationConfig.authenticationProvider();

        // Assert
        assertNotNull(authProvider);
        assertTrue(authProvider instanceof AuthenticationProvider);
    }

    @Test
    void authenticationProvider_ShouldAuthenticateValidUser() {
        // Arrange
        String rawPassword = "password123";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        testUser.setPassword(encoder.encode(rawPassword));

        when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        AuthenticationProvider authProvider = applicationConfig.authenticationProvider();
        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken("test@example.com", rawPassword);

        // Act
        var authentication = authProvider.authenticate(authRequest);

        // Assert
        assertTrue(authentication.isAuthenticated());
        assertEquals("test@example.com", authentication.getName());
        assertNotNull(authentication.getAuthorities());
        verify(userRepository, times(1)).findUserByEmail("test@example.com");
    }

    @Test
    void authenticationProvider_ShouldRejectInvalidPassword() {
        // Arrange
        String rawPassword = "password123";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        testUser.setPassword(encoder.encode(rawPassword));

        when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        AuthenticationProvider authProvider = applicationConfig.authenticationProvider();
        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken("test@example.com", "wrongpassword");

        // Act & Assert
        assertThrows(Exception.class, () -> authProvider.authenticate(authRequest));
        verify(userRepository, times(1)).findUserByEmail("test@example.com");
    }
}