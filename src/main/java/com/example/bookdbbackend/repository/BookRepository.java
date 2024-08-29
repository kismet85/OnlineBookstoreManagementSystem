package com.example.bookdbbackend.repository;

import com.example.bookdbbackend.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
