package com.example.bookdbbackend.repository;

import com.example.bookdbbackend.model.Author;
import com.example.bookdbbackend.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByFirstNameAndLastName(String firstName, String lastName);
}