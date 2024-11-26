package com.example.bookdbbackend.repository;

import com.example.bookdbbackend.model.BookFa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for handling BookFa-related database operations.
 */
public interface BookFaRepository extends JpaRepository<BookFa, Long> {
}