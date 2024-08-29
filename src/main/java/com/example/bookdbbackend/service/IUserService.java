package com.example.bookdbbackend.service;

import java.util.List;
import com.example.bookdbbackend.model.User;

public interface IUserService {
        User addUser(User user);
        List<User> getUsers();

        User updateUser(User user, Long id);

        User getUserById(Long id);

        void deleteUser(Long id);
}
