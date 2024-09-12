package com.example.bookdbbackend.controller;

import com.example.bookdbbackend.dtos.LoginUserDto;
import com.example.bookdbbackend.dtos.RegisterUserDto;
import com.example.bookdbbackend.model.User;
import com.example.bookdbbackend.repository.UserRepository;
import com.example.bookdbbackend.responses.LoginResponse;
import com.example.bookdbbackend.service.AuthenticationService;
import com.example.bookdbbackend.service.JwtService;
import com.example.bookdbbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/auth")
@RestController
public class AuthController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    private final UserService userService;


    public AuthController(JwtService jwtService, AuthenticationService authenticationService, UserRepository userRepository, UserService userService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> addUser(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signUp(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);

    }
    @GetMapping("/me")
    public ResponseEntity<User> getAuthenticatedUser(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        String username = jwtService.extractUsername(jwtToken);
        User loggedInUser = userService.getUserByEmail(username);
        return ResponseEntity.ok(loggedInUser);
    }

}
