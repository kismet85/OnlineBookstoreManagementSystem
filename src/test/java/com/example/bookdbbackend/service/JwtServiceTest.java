package com.example.bookdbbackend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    private JwtService jwtService;
    private String dummySecretKey;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        jwtService = new JwtService();

        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        dummySecretKey = java.util.Base64.getEncoder().encodeToString(key.getEncoded());

        Field secretKeyField = JwtService.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        secretKeyField.set(jwtService, dummySecretKey);

        Field jwtExpirationField = JwtService.class.getDeclaredField("jwtExpiration");
        jwtExpirationField.setAccessible(true);
        jwtExpirationField.set(jwtService, 1000 * 60 * 60); // 1 hour
    }

    @Test
    void testExtractUsername() {
        when(userDetails.getUsername()).thenReturn("testuser");
        String token = jwtService.generateToken(userDetails);

        String username = jwtService.extractUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    void testExtractClaim() {
        when(userDetails.getUsername()).thenReturn("testuser");
        String token = jwtService.generateToken(userDetails);

        Date expiration = jwtService.extractClaim(token, Claims::getExpiration);
        assertNotNull(expiration);
    }

    @Test
    void testIsTokenValid() {
        when(userDetails.getUsername()).thenReturn("testuser");
        String token = jwtService.generateToken(userDetails);

        boolean isValid = jwtService.isTokenValid(token, userDetails);
        assertTrue(isValid);
    }

    @Test
    void testIsTokenExpired() throws Exception {
        when(userDetails.getUsername()).thenReturn("testuser");
        String token = jwtService.generateToken(userDetails);

        Method isTokenExpiredMethod = JwtService.class.getDeclaredMethod("isTokenExpired", String.class);
        isTokenExpiredMethod.setAccessible(true);
        boolean isExpired = (boolean) isTokenExpiredMethod.invoke(jwtService, token);
        assertFalse(isExpired);
    }

    @Test
    void testExtractExpiration() throws Exception {
        when(userDetails.getUsername()).thenReturn("testuser");
        String token = jwtService.generateToken(userDetails);

        Method extractExpirationMethod = JwtService.class.getDeclaredMethod("extractExpiration", String.class);
        extractExpirationMethod.setAccessible(true);
        Date expiration = (Date) extractExpirationMethod.invoke(jwtService, token);
        assertNotNull(expiration);
    }
}