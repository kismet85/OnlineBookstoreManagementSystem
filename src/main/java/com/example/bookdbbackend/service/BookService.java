package com.example.bookdbbackend.service;

import com.example.bookdbbackend.dtos.AuthorRequest;
import com.example.bookdbbackend.dtos.BookRequest;
import com.example.bookdbbackend.dtos.InventoryRequest;
import com.example.bookdbbackend.exception.BookAlreadyExistsException;
import com.example.bookdbbackend.exception.BookNotFoundException;
import com.example.bookdbbackend.model.Author;
import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.model.Inventory;
import com.example.bookdbbackend.model.Publisher;
import com.example.bookdbbackend.repository.AuthorRepository;
import com.example.bookdbbackend.repository.BookRepository;
import com.example.bookdbbackend.repository.InventoryRepository;
import com.example.bookdbbackend.repository.PublisherRepository;
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
    @Autowired
    private final AuthorRepository authorRepository;
    @Autowired
    private final PublisherRepository publisherRepository;
    @Autowired
    private final InventoryRepository inventoryRepository;



    @Override
    public Book createBook(BookRequest bookRequest) {
        Inventory inventory = new Inventory();
        inventory.setStock_level_used(bookRequest.getInventory().getStockLevelUsed());
        inventory.setStock_level_new(bookRequest.getInventory().getStockLevelNew());
        inventory.setReserved_stock(bookRequest.getInventory().getReservedStock());
        inventoryRepository.save(inventory);

        Book book = new Book();
        book.setTitle(bookRequest.getTitle());
        book.setIsbn(bookRequest.getIsbn());
        book.setGenre(bookRequest.getGenre());
        book.setType(bookRequest.getType());
        book.setPublication_year(bookRequest.getPublicationYear());
        book.setPrice(bookRequest.getPrice());
        book.setBook_condition(bookRequest.getBookCondition());
        book.setReserved(bookRequest.isReserved());
        book.setImage_url(bookRequest.getImageUrl());
        book.setInventory(inventory);

        // Set the publisher
        if (bookRequest.getPublisherId() != null) {
            // Existing publisher
            Publisher publisher = publisherRepository.findById(bookRequest.getPublisherId())
                    .orElseThrow(() -> new RuntimeException("Publisher not found"));
            book.setPublisher(publisher);
        } else if (bookRequest.getPublisher() != null) {
            // New publisher
            Publisher publisher = new Publisher();
            publisher.setName(bookRequest.getPublisher().getName());
            publisher.setCountry(bookRequest.getPublisher().getCountry());
            publisher = publisherRepository.save(publisher);
            book.setPublisher(publisher);
        }


        Set<Author> authors = new HashSet<>();
        for (AuthorRequest authorRequest : bookRequest.getAuthors()) {
            Author author = new Author();
            author.setFirstName(authorRequest.getFirstName());
            author.setLastName(authorRequest.getLastName());

            // Check if the author already exists (to avoid duplicates)
            Optional<Author> existingAuthor = authorRepository
                    .findByFirstNameAndLastName(authorRequest.getFirstName(), authorRequest.getLastName());
            if (existingAuthor.isPresent()) {
                authors.add(existingAuthor.get());
            } else {
                authors.add(authorRepository.save(author));
            }
        }
        book.setAuthors(authors);

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
        List<Book> booksByAuthor = bookRepository.findBooksByAuthorsFirstNameContainingIgnoreCase(searchTerm);
        List<Book> booksByAuthorLastName = bookRepository.findBooksByAuthorsLastNameContainingIgnoreCase(searchTerm);

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
        combinedResults.addAll(booksByAuthor);
        combinedResults.addAll(booksByAuthorLastName);

        combinedResults = new ArrayList<>(new HashSet<>(combinedResults));

        return combinedResults;
    }


}
