package com.example.bookdbbackend.service;

import com.example.bookdbbackend.exception.UserNotFoundException;
import com.example.bookdbbackend.model.User;
import com.example.bookdbbackend.repository.UserRepository;
import com.example.bookdbbackend.exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.module.InvalidModuleDescriptorException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public List<User> getUsers() {
        // return user excluding the password
        return userRepository.findAll().stream().peek(user -> user.setPassword(null)).toList();
    }

    @Override
    public User addUser(User user) {
        if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists with email: " + user.getEmail());
        }
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Map<String, Object> updates, Long id) {
        User user = getUserById(id);
        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            switch (entry.getKey()) {
                case "first_name":
                    user.setFirst_name((String) entry.getValue());
                    break;
                case "last_name":
                    user.setLast_name((String) entry.getValue());
                    break;
                case "street_number":
                    user.setStreet_number((Integer) entry.getValue());
                    break;
                case "street_name":
                    user.setStreet_name((String) entry.getValue());
                    break;
                case "phone_number":
                    user.setPhone_number((String) entry.getValue());
                    break;
                case "postal_code":
                    user.setPostal_code((Integer) entry.getValue());
                    break;
                case "province":
                    user.setProvince((String) entry.getValue());
                    break;
                case "password":
                    String encodedPassword = bCryptPasswordEncoder.encode((String) entry.getValue());
                    user.setPassword(encodedPassword);
                    break;
                case "email":
                    user.setEmail((String) entry.getValue());
                    break;
            }
        }
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.findById(id).isPresent()) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    @Override
    public List<User> searchUsers(String searchTerm) {

        List<User> usersByEmail = userRepository.getUserByEmailContainingIgnoreCase(searchTerm);

        List<User> combinedResults = new ArrayList<>();

        combinedResults.addAll(usersByEmail);

        combinedResults = new ArrayList<>(new HashSet<>(combinedResults));

        return combinedResults;
    }
}
