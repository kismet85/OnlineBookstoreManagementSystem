package com.example.bookdbbackend.service;
import com.example.bookdbbackend.model.Book;
import java.util.List;


public interface IBookService {
    Book addBook(Book book);
    List<Book> getAllBooks();
    Book getBookById(Long id);
    Book updateBook(Book book, Long id);
    void deleteBook(Long id);

}
