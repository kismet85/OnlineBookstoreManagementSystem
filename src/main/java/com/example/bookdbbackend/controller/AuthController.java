package com.example.bookdbbackend.controller;

import com.example.bookdbbackend.model.User;
import com.example.bookdbbackend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.function.ToDoubleBiFunction;

@RequestMapping("/auth")
@RestController
public class AuthController {

    @Autowired
    private IUserService iUserService;

    @PostMapping("/register")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User newUser = iUserService.registerUser(user);
        //TODO generate jwt token
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestParam String email, @RequestParam String password) {
        User loggedInUser = iUserService.loginUser(email, password);
        //TODO generate jwt token
        return new ResponseEntity<>(loggedInUser, HttpStatus.OK);
    }
}
