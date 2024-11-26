package com.example.bookdbbackend.repository;

import com.example.bookdbbackend.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for handling Book-related database operations.
 */
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Finds books by title containing the specified string, case insensitive.
     *
     * @param title the title to search for
     * @return a list of books with titles containing the specified string
     */
    List<Book> findBooksByTitleContainingIgnoreCase(String title);

    /**
     * Finds books by the publisher's name.
     *
     * @param publisher the publisher's name
     * @return a list of books published by the specified publisher
     */
    List<Book> findBooksByPublisherName(String publisher);

    /**
     * Finds books by genre containing the specified string, case insensitive.
     *
     * @param category the genre to search for
     * @return a list of books with genres containing the specified string
     */
    List<Book> findBooksByGenreContainingIgnoreCase(String category);

    /**
     * Finds books by ISBN containing the specified string, case insensitive.
     *
     * @param isbn the ISBN to search for
     * @return a list of books with ISBNs containing the specified string
     */
    List<Book> findBooksByIsbnContainingIgnoreCase(String isbn);

    /**
     * Finds a book by its ISBN.
     *
     * @param isbn the ISBN of the book
     * @return an Optional containing the found book, or empty if no book is found
     */
    Optional<Book> findBookByIsbn(String isbn);

    /**
     * Finds books by the publisher's ID.
     *
     * @param publisherId the ID of the publisher
     * @return a list of books published by the specified publisher
     */
    @Query("SELECT b FROM Book b WHERE b.publisher.publisher_id = :publisherId")
    List<Book> findBooksByPublisherId(Long publisherId);

    /**
     * Finds books by the author's first name containing the specified string, case insensitive.
     *
     * @param author the author's first name to search for
     * @return a list of books with authors' first names containing the specified string
     */
    List<Book> findBooksByAuthorsFirstNameContainingIgnoreCase(String author);

    /**
     * Finds books by the author's last name containing the specified string, case insensitive.
     *
     * @param author the author's last name to search for
     * @return a list of books with authors' last names containing the specified string
     */
    List<Book> findBooksByAuthorsLastNameContainingIgnoreCase(String author);
}