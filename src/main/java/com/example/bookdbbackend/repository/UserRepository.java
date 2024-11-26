package com.example.bookdbbackend.repository;

import com.example.bookdbbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for handling User-related database operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Checks if a user exists by email.
     *
     * @param email the email to check
     * @return true if a user with the specified email exists, false otherwise
     */
    boolean existsUserByEmail(String email);

    /**
     * Finds a user by email.
     *
     * @param email the email of the user to find
     * @return an Optional containing the found user, or an empty Optional if no user was found
     */
    Optional<User> findUserByEmail(String email);

    /**
     * Finds users whose email contains the specified search term, ignoring case.
     *
     * @param searchTerm the search term to use
     * @return a list of users whose email contains the search term, ignoring case
     */
    List<User> getUserByEmailContainingIgnoreCase(String searchTerm);
}