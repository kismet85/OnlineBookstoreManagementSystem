package com.example.bookdbbackend.service;

import com.example.bookdbbackend.exception.UserNotFoundException;
import com.example.bookdbbackend.model.User;
import com.example.bookdbbackend.repository.UserRepository;
import com.example.bookdbbackend.exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Service class for handling user-related operations.
 */
@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Retrieves all users, excluding their passwords.
     *
     * @return a list of all users with passwords set to null
     */
    @Override
    public List<User> getUsers() {
        return userRepository.findAll().stream().peek(user -> user.setPassword(null)).toList();
    }

    /**
     * Adds a new user.
     *
     * @param user the user to add
     * @return the added user
     * @throws UserAlreadyExistsException if a user with the same email already exists
     */
    @Override
    public User addUser(User user) {
        if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists with email: " + user.getEmail());
        }
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    /**
     * Updates a user with the specified updates.
     *
     * @param updates the updates to apply
     * @param id the ID of the user to update
     * @return the updated user
     * @throws UserNotFoundException if the user is not found
     */
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

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user
     * @return the user with the specified ID
     * @throws UserNotFoundException if the user is not found
     */
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete
     * @throws UserNotFoundException if the user is not found
     */
    @Override
    public void deleteUser(Long id) {
        if (!userRepository.findById(id).isPresent()) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email the email of the user
     * @return the user with the specified email
     * @throws UserNotFoundException if the user is not found
     */
    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    /**
     * Searches for users based on a search term.
     *
     * @param searchTerm the search term
     * @return a list of users matching the search term
     */
    @Override
    public List<User> searchUsers(String searchTerm) {
        List<User> usersByEmail = userRepository.getUserByEmailContainingIgnoreCase(searchTerm);
        List<User> combinedResults = new ArrayList<>();
        combinedResults.addAll(usersByEmail);
        combinedResults = new ArrayList<>(new HashSet<>(combinedResults));
        return combinedResults;
    }
}