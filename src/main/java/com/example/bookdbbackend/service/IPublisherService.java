package com.example.bookdbbackend.service;

import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.model.Publisher;

import java.util.List;

public interface IPublisherService {

    Publisher addPublisher(Publisher publisher);
    List<Publisher> getAllPublishers();
    Publisher getPublisherById(Long id);

    List<Book> getBooksByPublisherCountry(String country);
    List<Book> getBooksByPublisherName(String name);
    Publisher updatePublisher(Publisher publisher, Long id);
    void deletePublisher(Long id);
}
