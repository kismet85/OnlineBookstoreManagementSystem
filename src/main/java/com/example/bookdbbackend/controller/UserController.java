package com.example.bookdbbackend.controller;

import com.example.bookdbbackend.exception.UserNotFoundException;
import com.example.bookdbbackend.service.JwtService;
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

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final IUserService iUserService;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;


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

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            User user = iUserService.getUserById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}