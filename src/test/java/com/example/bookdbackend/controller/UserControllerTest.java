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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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

    @Test
    void getUsersWithUnauthorizedToken() {
        String token = "Bearer invalidtoken";
        String actualToken = token.replace("Bearer ", "");
        String username = "admin";
        UserDetails userDetails = mock(UserDetails.class);

        when(jwtService.extractUsername(actualToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(actualToken, userDetails)).thenReturn(false);

        ResponseEntity<List<User>> response = userController.getUsers(token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void addUserWithUnauthorizedToken() {
        String token = "Bearer invalidtoken";
        String actualToken = token.replace("Bearer ", "");
        String username = "admin";

        UserDetails userDetails = mock(UserDetails.class);
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
        List<GrantedAuthority> authorities = Collections.singletonList(grantedAuthority);

        doReturn(authorities).when(userDetails).getAuthorities();

        when(jwtService.extractUsername(actualToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(actualToken, userDetails)).thenReturn(false);

        User newUser = new User();
        newUser.setUser_id(1L);

        when(iUserService.addUser(any(User.class))).thenReturn(newUser);

        ResponseEntity<User> response = userController.addUser(newUser, token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void updateUserWithUnauthorizedToken() {
        Long id = 1L;
        Map<String, Object> updates = Map.of("name", "Updated Name");
        String token = "Bearer invalidtoken";
        String actualToken = token.replace("Bearer ", "");
        String username = "admin";

        UserDetails userDetails = mock(UserDetails.class);
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
        List<GrantedAuthority> authorities = Collections.singletonList(grantedAuthority);

        doReturn(authorities).when(userDetails).getAuthorities();

        when(jwtService.extractUsername(actualToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(actualToken, userDetails)).thenReturn(false);

        ResponseEntity<User> response = userController.updateUser(updates, id, token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
    /*
        @Test
        void getUserByIdWithUnauthorizedToken() {
            Long id = 1L;
            String token = "Bearer invalidtoken";
            String actualToken = token.replace("Bearer ", "");
            String username = "admin";

            UserDetails userDetails = mock(UserDetails.class);
            when(jwtService.extractUsername(actualToken)).thenReturn(username);
            when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
            when(jwtService.isTokenValid(actualToken, userDetails)).thenReturn(false);

            ResponseEntity<User> response = userController.getUserById(id);

            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        }

        @Test
        void searchUsersWithUnauthorizedToken() throws Exception {
            String query = "test";
            String token = "Bearer invalidtoken"; // Mock invalid token
            String actualToken = token.replace("Bearer ", "");
            String username = "admin";

            // Mock the JWT service to simulate invalid token
            when(jwtService.extractUsername(actualToken)).thenReturn(username);
            when(userDetailsService.loadUserByUsername(username)).thenReturn(mock(UserDetails.class));
            when(jwtService.isTokenValid(actualToken, mock(UserDetails.class))).thenReturn(false); // Invalid token

            // Set up MockMvc to perform the request
            mockMvc.perform(get("/users/search") // Adjust the URL as per your controller
                            .param("query", query)
                            .header("Authorization", token))  // Add the invalid token in the header
                    .andExpect(status().isUnauthorized());  // Expect a 401 Unauthorized status
        }
        */
    @Test
    void deleteUserWithUnauthorizedToken() {
        Long id = 1L;
        String token = "Bearer invalidtoken";
        String actualToken = token.replace("Bearer ", "");
        String username = "admin";

        UserDetails userDetails = mock(UserDetails.class);
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
        List<GrantedAuthority> authorities = Collections.singletonList(grantedAuthority);

        doReturn(authorities).when(userDetails).getAuthorities();

        when(jwtService.extractUsername(actualToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(actualToken, userDetails)).thenReturn(false);

        ResponseEntity<String> response = userController.deleteUser(id, token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void getUsersWithInvalidToken() {
        String token = "Bearer invalidtoken";
        String actualToken = token.replace("Bearer ", "");
        String username = "admin";
        UserDetails userDetails = mock(UserDetails.class);

        when(jwtService.extractUsername(actualToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(actualToken, userDetails)).thenReturn(false);

        ResponseEntity<List<User>> response = userController.getUsers(token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void addUserWithNullUser() {
        String token = "Bearer paskasaatana";

        ResponseEntity<User> response = userController.addUser(null, token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }


    @Test
    void updateUserWithNullUser() {
        Long id = 1L;
        Map<String, Object> updates = Map.of("name", "Updated Name");
        String token = "Bearer paskasaatana";
        when(iUserService.getUserById(id)).thenReturn(null);

        ResponseEntity<User> response = userController.updateUser(updates, id, token);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    }

/*
    @Test
    void getUserByIdWithNonExistentId() {
        Long id = 999L;
        String token = "Bearer paskasaatana";  // mock token, adjust as needed

        // Mocking the service method to return null when the user with id 999 is requested
        when(iUserService.getUserById(id)).thenReturn(null);

        // Mocking the request header with the token
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", token);

        // Call the controller method
        ResponseEntity<String> response = userController.getUserById(id, token);

        // Check that the response status is 404 if the user was not found
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Check that the response body contains the expected error message (e.g., "User not found")
        assertEquals("User not found", response.getBody());
    }
*/


    @Test
    void getUsersWhenNoUsersExist() {
        String token = "Bearer paskasaatana";
        String actualToken = token.replace("Bearer ", "");
        String username = "admin";
        UserDetails userDetails = mock(UserDetails.class);

        // Mock roles/authorities for the user using doReturn
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
        List<GrantedAuthority> authorities = Collections.singletonList(grantedAuthority);
        doReturn(authorities).when(userDetails).getAuthorities();  // use doReturn for getter methods

        when(jwtService.extractUsername(actualToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(actualToken, userDetails)).thenReturn(true);
        when(iUserService.getUsers()).thenReturn(Collections.emptyList());

        ResponseEntity<List<User>> response = userController.getUsers(token);

        // Expecting a 200 OK status, as the user now has the correct role
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
    }

    @Test
    void addUserWithRoleCheck() {
        String token = "Bearer paskasaatana";
        String actualToken = token.replace("Bearer ", "");
        String username = "admin";

        UserDetails userDetails = mock(UserDetails.class);
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        List<GrantedAuthority> authorities = Collections.singletonList(grantedAuthority);

        doReturn(authorities).when(userDetails).getAuthorities();

        when(jwtService.extractUsername(actualToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(actualToken, userDetails)).thenReturn(true);

        User newUser = new User();
        newUser.setUser_id(1L);

        ResponseEntity<User> response = userController.addUser(newUser, token);

        // Check that the response status is FORBIDDEN
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        // Since there's no body in the response, check that it is null
        assertNull(response.getBody());
    }



}
