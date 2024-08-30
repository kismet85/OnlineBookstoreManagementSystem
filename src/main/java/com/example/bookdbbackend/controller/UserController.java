package com.example.bookdbbackend.controller;

import com.example.bookdbbackend.exception.UserNotFoundException;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.bookdbbackend.model.User;
import com.example.bookdbbackend.service.IUserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService iUserService;

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(iUserService.getUsers(), HttpStatus.FOUND);
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return iUserService.addUser(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable Long id) {
        try {
            User updatedUser = iUserService.updateUser(user, id);
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