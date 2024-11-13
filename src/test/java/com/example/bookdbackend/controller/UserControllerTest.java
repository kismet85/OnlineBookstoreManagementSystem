package com.example.bookdbackend.controller;

import com.example.bookdbbackend.controller.UserController;
import com.example.bookdbbackend.model.User;
import com.example.bookdbbackend.service.IUserService;
import com.example.bookdbbackend.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
        String token = "Bearer paskasaatana";
        String actualToken = token.replace("Bearer ", "");
        String username = "admin";
        UserDetails userDetails = mock(UserDetails.class);
        List<User> users = List.of(new User(), new User());

        when(jwtService.extractUsername(actualToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(actualToken, userDetails)).thenReturn(true);
        when(iUserService.getUsers()).thenReturn(users);

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
        List<GrantedAuthority> authorities = Collections.singletonList(grantedAuthority);

        doReturn(authorities).when(userDetails).getAuthorities();

        ResponseEntity<List<User>> response = userController.getUsers(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
        verify(iUserService, times(1)).getUsers();
    }

    @Test
    void addUser() {
        String token = "Bearer paskasaatana";
        String actualToken = token.replace("Bearer ", "");
        String username = "admin";

        UserDetails userDetails = mock(UserDetails.class);
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
        List<GrantedAuthority> authorities = Collections.singletonList(grantedAuthority);

        doReturn(authorities).when(userDetails).getAuthorities();

        when(jwtService.extractUsername(actualToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(actualToken, userDetails)).thenReturn(true);

        User newUser = new User();
        newUser.setUser_id(1L);

        when(iUserService.addUser(any(User.class))).thenReturn(newUser);

        ResponseEntity<User> response = userController.addUser(newUser, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(newUser, response.getBody());
        verify(iUserService, times(1)).addUser(any(User.class));
    }



    @Test
    void updateUser() {
        Long id = 1L;
        Map<String, Object> updates = Map.of("name", "Updated Name");
        User updatedUser = new User();
        updatedUser.setUser_id(id);
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
        Long id = 1L;
        String token = "Bearer paskasaatana";
        String actualToken = token.replace("Bearer ", "");
        String username = "admin";

        UserDetails userDetails = mock(UserDetails.class);
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
        List<GrantedAuthority> authorities = Collections.singletonList(grantedAuthority);

        doReturn(authorities).when(userDetails).getAuthorities();

        when(jwtService.extractUsername(actualToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(actualToken, userDetails)).thenReturn(true);

        doNothing().when(iUserService).deleteUser(id);

        ResponseEntity<String> response = userController.deleteUser(id, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully deleted user with id: " + id, response.getBody());
        verify(iUserService, times(1)).deleteUser(id);
    }

}
