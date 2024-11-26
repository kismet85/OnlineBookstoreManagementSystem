package com.example.bookdbbackend.service;

import com.example.bookdbbackend.dtos.LoginUserDto;
import com.example.bookdbbackend.dtos.RegisterUserDto;
import com.example.bookdbbackend.exception.UserAlreadyExistsException;
import com.example.bookdbbackend.model.User;
import com.example.bookdbbackend.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for handling authentication-related operations.
 */
@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Constructs an AuthenticationService with the specified dependencies.
     *
     * @param userRepository the user repository
     * @param passwordEncoder the password encoder
     * @param authenticationManager the authentication manager
     */
    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registers a new user.
     *
     * @param input the registration details
     * @return the registered user
     * @throws UserAlreadyExistsException if the email is already in use
     */
    public User signUp(RegisterUserDto input) {
        if (userRepository.existsUserByEmail(input.getEmail())) {
            throw new UserAlreadyExistsException("Email already in use");
        }

        User user = new User();
        user.setEmail(input.getEmail());
        user.setFirst_name(input.getFirst_name());
        user.setLast_name(input.getLast_name());
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        return userRepository.save(user);
    }

    /**
     * Authenticates a user.
     *
     * @param input the login details
     * @return the authenticated user
     * @throws RuntimeException if the user is not found
     */
    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword())
        );
        return userRepository.findUserByEmail(input.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}