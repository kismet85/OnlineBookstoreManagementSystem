package com.example.bookdbbackend.service;
import com.example.bookdbbackend.model.Book;
import java.util.List;
import java.util.Optional;


public interface IBookService {
    Book addBook(Book book);
    List<Book> getAllBooks();
    List<Book> getBooksByAuthorId(Long author_id);
    Book getBookById(Long id);
    Book updateBook(Book book, Long id);
    void deleteBook(Long id);
    List<Book> getBooksByTitle(String title);
    Book getBookByIsbn(String isbn);
    List<Book> getBooksByGenre(String genre);

    List<Book> getBooksByPublisherName(String publisher);

    List<Book> searchBooks(String searchTerm);
}
