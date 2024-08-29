package com.example.bookdbbackend.service;

import com.example.bookdbbackend.model.User;
import com.example.bookdbbackend.repository.UserRepository;
import com.example.bookdbbackend.exception.UserAlreadyExistsException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {
    private UserRepository userRepository;
    @Override
    public User addUser(User user) {
        if (userRepository.findById(user.getId()).isPresent()) { throw new
                UserAlreadyExistsException(user.getEmail() + " already exists!");
        }
            return userRepository.save(user);
    }
    private boolean userAlreadyExists(Long id) {

        return userRepository.findById(id).isPresent();

    }

    @Override
    public List<User> getUsers() {
        return null;
    }

    @Override
    public User updateUser(User user, Long id) {
        return null;
    }

    @Override
    public User getUserById(Long id) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {

    }
}
