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

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public User signUp(RegisterUserDto input) {
        if(userRepository.existsUserByEmail(input.getEmail())){
            throw new UserAlreadyExistsException("Email already in use");
        }

        User user = new User();
        user.setEmail(input.getEmail());
        user.setFirst_name(input.getFirst_name());
        user.setLast_name(input.getLast_name());
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword())
        );
            return userRepository.findUserByEmail(input.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
