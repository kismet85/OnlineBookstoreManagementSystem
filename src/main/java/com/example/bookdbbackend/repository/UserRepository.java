package com.example.bookdbbackend.repository;

import com.example.bookdbbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByEmail(String email);
    Optional<User> findUserByEmail(String email);

    List<User> getUserByEmailContainingIgnoreCase(String searchTerm);
}