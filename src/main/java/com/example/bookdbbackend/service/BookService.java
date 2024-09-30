package com.example.bookdbbackend.service;

import com.example.bookdbbackend.exception.BookAlreadyExistsException;
import com.example.bookdbbackend.exception.BookNotFoundException;
import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BookService implements IBookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);


    @Autowired
    private final BookRepository bookRepository;


    @Override
    public Book addBook(Book book) {
        if (bookAlreadyExists(book.getBook_id())) {
            throw new BookAlreadyExistsException("Book with id " + book.getBook_id() + " already exists");
        }
        return bookRepository.save(book);
    }

    private boolean bookAlreadyExists(Long id) {
        return bookRepository.findById(id).isPresent();
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isEmpty()) {
            throw new BookNotFoundException("Book with id " + id + " not found");
        }

        return book.get();
    }

    @Override
    public Book updateBook(Map<String, Object> updates, Long id) {
        try{
            logger.info("Updates: " + updates.toString());
            Book book = getBookById(id);
            if (!bookAlreadyExists(id)) {
                throw new BookNotFoundException("Book with id " + id + " not found");
            }
            for (Map.Entry<String, Object> entry : updates.entrySet())
                switch (entry.getKey()) {
                    case "title":
                        book.setTitle((String) entry.getValue());
                        break;
                    case "isbn":
                        book.setIsbn((String) entry.getValue());
                        break;
                    case "genre":
                        book.setGenre((String) entry.getValue());
                        break;
                    case "type":
                        book.setType((String) entry.getValue());
                        break;
                    case "price":
                        // Explicitly parse the value as BigDecimal
                        // This is kind of stupid because now the value is not really a big decimal
                        if (entry.getValue() instanceof Number) {
                            book.setPrice(BigDecimal.valueOf(((Number) entry.getValue()).doubleValue()));
                        }
                        break;
                    case "publication_year":
                        book.setPublication_year((Integer) entry.getValue());
                        break;
                    case "book_condition":
                        book.setBook_condition((String) entry.getValue());
                        break;
                    case "image_url":
                        book.setImage_url((String) entry.getValue());
                        break;
                }
            return bookRepository.save(book);
        } catch (Exception e) {
            throw new BookNotFoundException("Book with id " + id + " not found");
        }
    }


    @Override
    public void deleteBook(Long id) {
        if (!bookAlreadyExists(id)) {
            throw new BookNotFoundException("Book with id " + id + " not found");
        }
        bookRepository.deleteById(id);
    }

    @Override
    public List<Book> getBooksByTitle(String title) {
        List<Book> books = bookRepository.findBooksByTitleContainingIgnoreCase(title);
        if (books.isEmpty()) {
            throw new BookNotFoundException("Book with title " + title + " not found");
        }
        return books;
    }


    @Override
    public Book getBookByIsbn(String isbn) {
        Optional<Book> book = bookRepository.findBookByIsbn(isbn);
        if (!book.isPresent()) {
            throw new BookNotFoundException("Book with isbn " + isbn + " not found");
        }
        return book.get();
    }

    @Override
    public List<Book> getBooksByAuthor(String author) {
        List<Book> books = bookRepository.findBooksByAuthorsFirstNameContainingIgnoreCase(author);
        if (books.isEmpty()) {
            throw new BookNotFoundException("Book with author " + author + " not found");
        }
        return books;
    }

    @Override
    public List<Book> getBooksByGenre(String genre) {
        List<Book> books = bookRepository.findBooksByGenreContainingIgnoreCase(genre);
        if (books.isEmpty()) {
            throw new BookNotFoundException("Book with genre " + genre + " not found");
        }
        return books;
    }

    @Override
    public List<Book> getBooksByPublisherName(String publisher) {
        List<Book> books = bookRepository.findBooksByPublisherName(publisher);
        if (books.isEmpty()) {
            throw new BookNotFoundException("Book with publisher " + publisher + " not found");
        }
        return books;
    }

    @Override
    public List<Book> searchBooks(String searchTerm) {

        List<Book> booksByTitle = bookRepository.findBooksByTitleContainingIgnoreCase(searchTerm);
        List<Book> booksByPublisher = bookRepository.findBooksByPublisherName(searchTerm);
        List<Book> booksByGenre = bookRepository.findBooksByGenreContainingIgnoreCase(searchTerm);
        List<Book> booksByIsbn = bookRepository.findBooksByIsbnContainingIgnoreCase(searchTerm);

        if (searchTerm.length() < 3) {
            throw new IllegalArgumentException("Search term must be at least 3 characters long");
        }

        if (!booksByIsbn.isEmpty()) {
            return booksByIsbn;
        }

        List<Book> combinedResults = new ArrayList<>();

        combinedResults.addAll(booksByTitle);
        combinedResults.addAll(booksByPublisher);
        combinedResults.addAll(booksByGenre);

        combinedResults = new ArrayList<>(new HashSet<>(combinedResults));

        return combinedResults;
    }


}
