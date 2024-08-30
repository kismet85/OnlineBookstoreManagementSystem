package com.example.bookdbbackend.repository;

import com.example.bookdbbackend.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitle(String name);
    Optional<Book> findBookByIsbn(String isbn);
    Optional<Book> findBookByGenre(String category);

}
