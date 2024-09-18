package com.example.bookdbbackend.controller;

import com.example.bookdbbackend.exception.UserNotFoundException;
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



    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(iUserService.getUsers(), HttpStatus.OK);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@RequestBody Map<String, Object> updates, @PathVariable Long id) {
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