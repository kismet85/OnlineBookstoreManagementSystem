package com.example.bookdbbackend.service;

import com.example.bookdbbackend.exception.BookAlreadyExistsException;
import com.example.bookdbbackend.exception.BookNotFoundException;
import com.example.bookdbbackend.exception.PublisherNotFoundException;
import com.example.bookdbbackend.exception.UserNotFoundException;
import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.model.Publisher;
import com.example.bookdbbackend.repository.PublisherRepository;
import com.example.bookdbbackend.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PublisherService implements IPublisherService{

    @Autowired
    private final PublisherRepository publisherRepository;

    @Autowired
    private final BookRepository bookRepository;

    @Override
    public Publisher addPublisher(Publisher publisher) {
        if (publisherAlreadyExists(publisher.getPublisher_id()))
        {
            throw new BookAlreadyExistsException("Publisher with id " + publisher.getPublisher_id() + " already exists");
        }
        return publisherRepository.save(publisher);
    }
    private boolean publisherAlreadyExists(Long id) {
        return publisherRepository.findById(id).isPresent();
    }
    @Override
    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    @Override
    public Publisher getPublisherById(Long id) {
        if (!publisherRepository.findById(id).isPresent()) {
            throw new PublisherNotFoundException("Publisher not found with id: " + id);
        }
        return publisherRepository.findById(id).get();
    }

    @Override
    public List<Book> getBooksByPublisherName(String name) {
        List<Publisher> publishers = publisherRepository.findPublisherByName(name);
        if (publishers.isEmpty()) {
            throw new PublisherNotFoundException("Publisher not found with name: " + name);
        }

        List<Book> books = new ArrayList<>();
        for (Publisher publisher : publishers) {
            books.addAll(bookRepository.findBooksByPublisherId(publisher.getPublisher_id()));
        }

        return books;
    }


    @Override
    public List<Book> getBooksByPublisherCountry(String country) {
        List<Publisher> publishers = publisherRepository.findPublisherByCountry(country);
        if (publishers.isEmpty()) {
            throw new PublisherNotFoundException("Publisher not found from country: " + country);
        }

        List<Book> books = new ArrayList<>();
        for (Publisher publisher : publishers) {
            books.addAll(bookRepository.findBooksByPublisherId(publisher.getPublisher_id()));
        }

        return books;
    }

    @Override
    public Publisher updatePublisher(Publisher publisher, Long id) {
        if (!publisherAlreadyExists(id)){
            throw new BookNotFoundException("Publisher with id " + id + " not found");
        }
        publisher.setPublisher_id(id);
        return publisherRepository.save(publisher);
    }

    @Override
    public void deletePublisher(Long id) {
        if (!publisherAlreadyExists(id)){
            throw new BookNotFoundException("Publisher with id " + id + " not found");
        }
        publisherRepository.deleteById(id);
    }
}
