package com.example.bookdbbackend.service;

import com.example.bookdbbackend.dtos.BookRequest;
import com.example.bookdbbackend.model.Book;
import java.util.List;
import java.util.Map;

/**
 * Service interface for handling book-related operations.
 */
public interface IBookService {

    /**
     * Creates a new book.
     *
     * @param bookRequest the book request details
     * @return the created book
     */
    Book createBook(BookRequest bookRequest);

    /**
     * Retrieves all books based on the specified language.
     *
     * @param language the language code
     * @return a list of books
     */
    List<?> getAllBooks(String language);

    /**
     * Retrieves a book by its ID.
     *
     * @param id the ID of the book
     * @return the book with the specified ID
     */
    Book getBookById(Long id);

    /**
     * Updates a book with the specified updates.
     *
     * @param updates the updates to apply
     * @param id the ID of the book to update
     * @return the updated book
     */
    Book updateBook(Map<String, Object> updates, Long id);

    /**
     * Deletes a book by its ID.
     *
     * @param id the ID of the book to delete
     */
    void deleteBook(Long id);

    /**
     * Retrieves books by their title.
     *
     * @param title the title of the books
     * @return a list of books with the specified title
     */
    List<Book> getBooksByTitle(String title);

    /**
     * Retrieves a book by its ISBN.
     *
     * @param isbn the ISBN of the book
     * @return the book with the specified ISBN
     */
    Book getBookByIsbn(String isbn);

    /**
     * Retrieves books by their genre.
     *
     * @param genre the genre of the books
     * @return a list of books with the specified genre
     */
    List<Book> getBooksByGenre(String genre);

    /**
     * Retrieves books by their publisher's name.
     *
     * @param publisher the publisher's name
     * @return a list of books by the specified publisher
     */
    List<Book> getBooksByPublisherName(String publisher);

    /**
     * Retrieves books by their author's name.
     *
     * @param author the author's name
     * @return a list of books by the specified author
     */
    List<Book> getBooksByAuthor(String author);

    /**
     * Searches for books based on a search term.
     *
     * @param searchTerm the search term
     * @return a list of books matching the search term
     */
    List<Book> searchBooks(String searchTerm);

    /**
     * Creates a dummy book request.
     *
     * @return the dummy book request
     */
    BookRequest createDummyBook();
}