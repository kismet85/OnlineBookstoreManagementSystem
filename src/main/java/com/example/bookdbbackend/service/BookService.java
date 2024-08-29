package com.example.bookdbbackend.service;

import com.example.bookdbbackend.exception.BookAlreadyExistsException;
import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService implements IBookService {
    private final BookRepository bookRepository;
    @Override
    public Book addBook(Book book) {
        if (bookAlreadyExists(book.getId()))
        {
            throw new BookAlreadyExistsException("Book with id " + book.getId() + " already exists");
        }
    }

    @Override
    public List<Book> getAllBooks() {
        return null;
    }

    @Override
    public Book getBookById(Long id) {
        return null;
    }

    @Override
    public Book updateBook(Book book, Long id) {
        return null;
    }

    @Override
    public void deleteBook(Long id) {

    }
}
