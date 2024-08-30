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

    private User mergeNewAndOldUserForUpdate(User existingUser, User newUser) {
        // If the new user object has a value for a field, update the existing user object with that value
        existingUser.setFirst_name(newUser.getFirst_name() != null ? newUser.getFirst_name() : existingUser.getFirst_name());
        existingUser.setLast_name(newUser.getLast_name() != null ? newUser.getLast_name() : existingUser.getLast_name());
        existingUser.setStreet_number(newUser.getStreet_number() != 0 ? newUser.getStreet_number() : existingUser.getStreet_number());
        existingUser.setStreet_name(newUser.getStreet_name() != null ? newUser.getStreet_name() : existingUser.getStreet_name());
        existingUser.setPhone_number(newUser.getPhone_number() != 0 ? newUser.getPhone_number() : existingUser.getPhone_number());
        existingUser.setPostal_code(newUser.getPostal_code() != 0 ? newUser.getPostal_code() : existingUser.getPostal_code());
        existingUser.setProvince(newUser.getProvince() != null ? newUser.getProvince() : existingUser.getProvince());
        existingUser.setRole(newUser.getRole() != null ? newUser.getRole() : existingUser.getRole());
        existingUser.setEmail(newUser.getEmail() != null ? newUser.getEmail() : existingUser.getEmail());
        return existingUser;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(iUserService.getUsers(), HttpStatus.OK);
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return iUserService.addUser(user);
    }

    @PostMapping("/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable Long id) {
        try {
            User existingUser = iUserService.getUserById(id);
            User mergedUser = mergeNewAndOldUserForUpdate(existingUser, user);
            User updatedUser = iUserService.updateUser(mergedUser, id);

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