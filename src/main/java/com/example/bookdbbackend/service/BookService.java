package com.example.bookdbbackend.service;

import com.example.bookdbbackend.dtos.AuthorRequest;
import com.example.bookdbbackend.dtos.BookRequest;
import com.example.bookdbbackend.dtos.InventoryRequest;
import com.example.bookdbbackend.dtos.PublisherRequest;
import com.example.bookdbbackend.exception.*;
import com.example.bookdbbackend.model.Author;
import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.model.Inventory;
import com.example.bookdbbackend.model.Publisher;
import com.example.bookdbbackend.repository.*;
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
    private final BookJpRepository bookJpRepository;
    @Autowired
    private final BookFaRepository bookFaRepository;
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
    public List<?> getAllBooks(String language) {
        return switch (language) {
            case "ja" -> bookJpRepository.findAll();
            case "fa" -> bookFaRepository.findAll();
            default -> bookRepository.findAll();
        };
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
        try {
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
                    case "reserved":
                        book.setReserved((Boolean) entry.getValue());
                        break;
                    case "inventory":
                        Map<String, Integer> inventoryUpdates = (Map<String, Integer>) entry.getValue();
                        logger.info("Inventory updates: " + inventoryUpdates.toString());
                        Inventory inventory = book.getInventory();
                        if (inventoryUpdates.containsKey("stockLevelUsed")) {
                            inventory.setStock_level_used(inventoryUpdates.get("stockLevelUsed"));
                        }
                        if (inventoryUpdates.containsKey("stockLevelNew")) {
                            inventory.setStock_level_new(inventoryUpdates.get("stockLevelNew"));
                        }
                        if (inventoryUpdates.containsKey("reservedStock")) {
                            inventory.setReserved_stock(inventoryUpdates.get("reservedStock"));
                        }
                        inventoryRepository.save(inventory);
                        break;
                    case "publisher":
                        Map<String, String> publisherUpdates = (Map<String, String>) entry.getValue();
                        Publisher publisher = book.getPublisher();
                        if (publisherUpdates.containsKey("name")) {
                            publisher.setName(publisherUpdates.get("name"));
                        }
                        if (publisherUpdates.containsKey("country")) {
                            publisher.setCountry(publisherUpdates.get("country"));
                        }
                        publisherRepository.save(publisher);
                        break;
                    case "authors":
                        logger.info("Updating authors");
                        logger.info("Full updates map: " + updates);

                        if (entry.getValue() instanceof List) {
                            List<?> authorUpdatesList = (List<?>) entry.getValue();
                            if (!authorUpdatesList.isEmpty() && authorUpdatesList.get(0) instanceof Map) {
                                List<Map<String, Object>> authorUpdates = (List<Map<String, Object>>) authorUpdatesList; // Change to Map<String, Object> to allow for any type
                                logger.info("Author updates: " + authorUpdates.toString());

                                for (Map<String, Object> authorUpdate : authorUpdates) {
                                    // Make sure to handle potential type issues with author_id
                                    Object authorIdObj = authorUpdate.get("author_id");
                                    if (authorIdObj instanceof Number) {
                                        Long authorId = ((Number) authorIdObj).longValue(); // Use longValue() to convert Number to Long
                                        logger.info("Author ID: " + authorId);

                                        Author author = authorRepository.findById(authorId)
                                                .orElseThrow(() -> new AuthorNotFoundException("Author with id " + authorId + " not found"));

                                        // Check for firstName and lastName with safe casting
                                        if (authorUpdate.containsKey("firstName")) {
                                            Object firstNameObj = authorUpdate.get("firstName");
                                            if (firstNameObj instanceof String) {
                                                author.setFirstName((String) firstNameObj);
                                            } else {
                                                logger.error("Expected a String for firstName but found: " + firstNameObj.getClass());
                                            }
                                        }

                                        if (authorUpdate.containsKey("lastName")) {
                                            Object lastNameObj = authorUpdate.get("lastName");
                                            if (lastNameObj instanceof String) {
                                                author.setLastName((String) lastNameObj);
                                            } else {
                                                logger.error("Expected a String for lastName but found: " + lastNameObj.getClass());
                                            }
                                        }

                                        authorRepository.save(author);
                                        logger.info("Successfully updated author with ID: " + authorId);
                                    } else {
                                        logger.error("Expected author_id to be a Number but found: " + authorIdObj.getClass());
                                        throw new RuntimeException("Invalid author ID format.");
                                    }
                                }
                            } else {
                                logger.error("Author updates list is empty or not a valid list of maps.");
                                throw new RuntimeException("Invalid author updates format.");
                            }
                        } else {
                            logger.error("Expected authors to be a List, but found: " + entry.getValue().getClass());
                            throw new RuntimeException("Invalid author updates format.");
                        }
                        break;

                    default:
                        logger.warn("No matching case for key: " + entry.getKey());
                        break;
                }

            return bookRepository.save(book);
        } catch (Exception e) {
              logger.error("Error updating book with ID " + id, e);
                throw new InvalidDataException(e.getMessage());
        }
    }

    @Override
    public BookRequest createDummyBook(){
        BookRequest bookRequest = new BookRequest();
        bookRequest.setTitle("");
        bookRequest.setIsbn("");
        bookRequest.setGenre("");
        bookRequest.setType("");
        bookRequest.setPublicationYear(0);
        bookRequest.setPrice(BigDecimal.valueOf(0));
        bookRequest.setBookCondition("");
        bookRequest.setReserved(false);
        bookRequest.setImageUrl("");

        PublisherRequest publisherRequest = new PublisherRequest();
        publisherRequest.setCountry("");
        publisherRequest.setName("");
        bookRequest.setPublisher(publisherRequest);

        bookRequest.setAuthors(new ArrayList<>());

        InventoryRequest inventoryRequest = new InventoryRequest();
        inventoryRequest.setStockLevelUsed(0);
        inventoryRequest.setStockLevelNew(0);
        inventoryRequest.setReservedStock(0);
        bookRequest.setInventory(inventoryRequest);

        return bookRequest;
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

    public List<Book> searchBooks(String searchTerm) {
        if (searchTerm.length() < 3) {
            throw new IllegalArgumentException("Search term must be at least 3 characters long");
        }

        List<Book> booksByTitle = bookRepository.findBooksByTitleContainingIgnoreCase(searchTerm);
        List<Book> booksByPublisher = bookRepository.findBooksByPublisherName(searchTerm);
        List<Book> booksByGenre = bookRepository.findBooksByGenreContainingIgnoreCase(searchTerm);
        List<Book> booksByIsbn = bookRepository.findBooksByIsbnContainingIgnoreCase(searchTerm);
        List<Book> booksByAuthor = bookRepository.findBooksByAuthorsFirstNameContainingIgnoreCase(searchTerm);
        List<Book> booksByAuthorLastName = bookRepository.findBooksByAuthorsLastNameContainingIgnoreCase(searchTerm);

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
