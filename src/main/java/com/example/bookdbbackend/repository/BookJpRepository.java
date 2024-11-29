package com.example.bookdbbackend.repository;

import com.example.bookdbbackend.model.BookJp;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository interface for handling BookJp-related database operations.
 */
public interface BookJpRepository extends JpaRepository<BookJp, Long> {
}