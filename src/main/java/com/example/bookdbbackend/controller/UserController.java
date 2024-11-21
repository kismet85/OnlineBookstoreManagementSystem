package com.example.bookdbbackend.controller;

import com.example.bookdbbackend.exception.UserNotFoundException;
import com.example.bookdbbackend.service.JwtService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.bookdbbackend.model.User;
import com.example.bookdbbackend.service.IUserService;

import java.util.List;
import java.util.Map;

/**
 * Controller for handling user-related requests.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final IUserService iUserService;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Endpoint for getting all users.
     *
     * @param token the JWT token from the Authorization header
     * @return a list of all users
     */
    @GetMapping
    public ResponseEntity<List<User>> getUsers(@RequestHeader("Authorization") String token) {
        String actualToken = token.replace("Bearer ", "");
        String username = jwtService.extractUsername(actualToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtService.isTokenValid(actualToken, userDetails)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(iUserService.getUsers(), HttpStatus.OK);
    }

    /**
     * Endpoint for adding a new user.
     *
     * @param user the user to add
     * @param token the JWT token from the Authorization header
     * @return the added user
     */
    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User user, @RequestHeader("Authorization") String token) {
        String actualToken = token.replace("Bearer ", "");
        String username = jwtService.extractUsername(actualToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtService.isTokenValid(actualToken, userDetails)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        User registeredUser = iUserService.addUser(user);
        return ResponseEntity.ok(registeredUser);
    }

    /**
     * Endpoint for updating a user by ID.
     *
     * @param updates the updates to apply to the user
     * @param id the ID of the user
     * @param token the JWT token from the Authorization header
     * @return the updated user
     */
    @PostMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@RequestBody Map<String, Object> updates, @PathVariable Long id, @RequestHeader("Authorization") String token) {
        String actualToken = token.replace("Bearer ", "");
        String username = jwtService.extractUsername(actualToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtService.isTokenValid(actualToken, userDetails)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            User updatedUser = iUserService.updateUser(updates, id);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint for getting a user by ID.
     *
     * @param id the ID of the user
     * @return the user with the specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            User user = iUserService.getUserById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint for searching users by a query.
     *
     * @param query the search query
     * @return a list of users matching the query
     */
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String query) {
        List<User> users = iUserService.searchUsers(query);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Endpoint for deleting a user by ID.
     *
     * @param id the ID of the user
     * @param token the JWT token from the Authorization header
     * @return a response indicating the result of the deletion
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        String actualToken = token.replace("Bearer ", "");
        String username = jwtService.extractUsername(actualToken);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtService.isTokenValid(actualToken, userDetails)) {
            return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            return new ResponseEntity<>("Access denied", HttpStatus.FORBIDDEN);
        }

        try {
            iUserService.deleteUser(id);
            return new ResponseEntity<>("Successfully deleted user with id: " + id, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("An error occurred while deleting the user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}