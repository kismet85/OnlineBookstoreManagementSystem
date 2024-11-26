package com.example.bookdbbackend.service;

import java.util.List;
import java.util.Map;

import com.example.bookdbbackend.model.User;

/**
 * Service interface for handling user-related operations.
 */
public interface IUserService {

        /**
         * Retrieves all users.
         *
         * @return a list of all users
         */
        List<User> getUsers();

        /**
         * Updates a user with the specified updates.
         *
         * @param updates the updates to apply
         * @param id the ID of the user to update
         * @return the updated user
         */
        User updateUser(Map<String, Object> updates, Long id);

        /**
         * Retrieves a user by their ID.
         *
         * @param id the ID of the user
         * @return the user with the specified ID
         */
        User getUserById(Long id);

        /**
         * Retrieves a user by their email.
         *
         * @param email the email of the user
         * @return the user with the specified email
         */
        User getUserByEmail(String email);

        /**
         * Searches for users based on a search term.
         *
         * @param searchTerm the search term
         * @return a list of users matching the search term
         */
        List<User> searchUsers(String searchTerm);

        /**
         * Deletes a user by their ID.
         *
         * @param id the ID of the user to delete
         */
        void deleteUser(Long id);

        /**
         * Adds a new user.
         *
         * @param user the user to add
         * @return the added user
         */
        User addUser(User user);
}