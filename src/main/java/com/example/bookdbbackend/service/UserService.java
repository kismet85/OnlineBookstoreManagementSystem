package com.example.bookdbbackend.service;

import com.example.bookdbbackend.exception.UserNotFoundException;
import com.example.bookdbbackend.model.User;
import com.example.bookdbbackend.repository.UserRepository;
import com.example.bookdbbackend.exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.module.InvalidModuleDescriptorException;
import java.util.List;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User registerUser(User user) {
        if (userRepository.findById(user.getUser_id()).isPresent()) {
            throw new
                    UserAlreadyExistsException(user.getEmail() + " already exists!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        userRepository.save(user);
        String token = generateJwtToken(user);
    }


    @Override
    public User loginUser(String email, String password) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        } else {
            throw new InvalidModuleDescriptorException("Invalid password");
        }
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User user, Long id) {
        if (!userRepository.findById(id).isPresent()) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        user.setUser_id(id);
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        if (!userRepository.findById(id).isPresent()) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        return userRepository.findById(id).get();
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.findById(id).isPresent()) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
