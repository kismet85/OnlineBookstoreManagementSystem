package com.example.bookdbbackend.repository;

import com.example.bookdbbackend.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findBooksByTitleContainingIgnoreCase(String title);

    List<Book> findBooksByPublisherName(String publisher);

    List<Book> findBooksByGenreContainingIgnoreCase(String category);

    List<Book> findBooksByIsbnContainingIgnoreCase(String isbn);


    Optional<Book> findBookByIsbn(String isbn);

    @Query("SELECT b FROM Book b JOIN b.writtenBy w JOIN w.author a WHERE a.author_id = :authorId")
    List<Book> findBooksByAuthorId(Long authorId);

    @Query("SELECT b FROM Book b WHERE b.publisher.publisher_id = :publisherId")
    List<Book> findBooksByPublisherId(Long publisherId);
}
