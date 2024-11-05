package com.example.bookdbbackend.service;
import com.example.bookdbbackend.dtos.BookRequest;
import com.example.bookdbbackend.model.Book;
import java.util.List;
import java.util.Map;


public interface IBookService {
    Book createBook(BookRequest bookRequest);
    List<?> getAllBooks(String language);

    Book getBookById(Long id);
    Book updateBook(Map<String, Object> updates, Long id);
    void deleteBook(Long id);
    List<Book> getBooksByTitle(String title);
    Book getBookByIsbn(String isbn);
    List<Book> getBooksByGenre(String genre);

    List<Book> getBooksByPublisherName(String publisher);

    List<Book> getBooksByAuthor(String author);
    List<Book> searchBooks(String searchTerm);

    BookRequest createDummyBook();
}
