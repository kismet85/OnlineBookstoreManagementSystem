package com.example.bookdbbackend.service;

import java.util.List;
import java.util.Map;

import com.example.bookdbbackend.model.User;

public interface IUserService {

        List<User> getUsers();

        User updateUser(Map<String, Object> updates, Long id);

        User getUserById(Long id);

        User getUserByEmail(String email);

        List<User> searchUsers(String searchTerm);
        void deleteUser(Long id);

        User addUser(User user);
}
