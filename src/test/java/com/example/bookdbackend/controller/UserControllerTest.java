package com.example.bookdbackend.controller;

import com.example.bookdbbackend.controller.UserController;
import com.example.bookdbbackend.model.User;
import com.example.bookdbbackend.service.IUserService;
import com.example.bookdbbackend.service.JwtService;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserControllerTest {

    @Mock
    private IUserService iUserService;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserDetailsService userDetailsService;
    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUsers() {
    }

    @Test
    void addUser() {
    }

    @Test
    void updateUser() {
        Long id = 1L;
        Map<String, Object> updates = Map.of("name", "Updated Name");
        User updatedUser = new User();
        updatedUser.setUser_id(id);
        //updatedUser.setName("Updated Name");
        String token = "Bearer token";
        String actualToken = token.replace("Bearer ", "");
        String username = "admin";
        UserDetails userDetails = mock(UserDetails.class);

        when(jwtService.extractUsername(actualToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(actualToken, userDetails)).thenReturn(true);
        when(iUserService.updateUser(updates, id)).thenReturn(updatedUser);

        ResponseEntity<User> response = userController.updateUser(updates, id, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
        verify(iUserService, times(1)).updateUser(updates, id);
    }

    @Test
    void getUserById() {
        Long id = 1L;
        User user = new User();
        user.setUser_id(id);

        when(iUserService.getUserById(id)).thenReturn(user);

        ResponseEntity<User> response = userController.getUserById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(iUserService, times(1)).getUserById(id);
    }

    @Test
    void searchUsers() {
        String query = "test";
        List<User> users = List.of(new User(), new User());

        when(iUserService.searchUsers(query)).thenReturn(users);

        ResponseEntity<List<User>> response = userController.searchUsers(query);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
        verify(iUserService, times(1)).searchUsers(query);
    }

    @Test
    void deleteUser() {
    }
}