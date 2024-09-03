package com.example.bookdbbackend.service;

import java.util.List;
import com.example.bookdbbackend.model.User;

public interface IUserService {

        List<User> getUsers();

        User updateUser(User user, Long id);

        User getUserById(Long id);

        User getMe();

        void deleteUser(Long id);
}
