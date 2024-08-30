package com.example.bookdbbackend.repository;

import com.example.bookdbbackend.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitle(String name);
    Optional<Book> findBookByIsbn(String isbn);
    Optional<Book> findBookByGenre(String category);

    @Query("SELECT b FROM Book b JOIN b.writtenBy w JOIN w.author a WHERE a.author_id = :authorId")
    List<Book> findBooksByAuthorId(Long authorId);

}
