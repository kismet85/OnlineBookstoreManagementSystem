package com.example.bookdbbackend.repository;

import com.example.bookdbbackend.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for handling author-related database operations.
 */
public interface AuthorRepository extends JpaRepository<Author, Long> {

    /**
     * Finds an author by their first name and last name.
     *
     * @param firstName the first name of the author
     * @param lastName the last name of the author
     * @return an Optional containing the found author, or empty if no author is found
     */
    Optional<Author> findByFirstNameAndLastName(String firstName, String lastName);

    @Query("SELECT a FROM Author a JOIN a.books b WHERE b.book_id = :bookId")
    List<Author> findAuthorsByBookId(Long bookId);
}