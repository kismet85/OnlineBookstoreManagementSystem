package com.example.bookdbbackend.service;

import com.example.bookdbbackend.exception.BookAlreadyExistsException;
import com.example.bookdbbackend.exception.BookNotFoundException;
import com.example.bookdbbackend.exception.PublisherNotFoundException;
import com.example.bookdbbackend.exception.UserNotFoundException;
import com.example.bookdbbackend.model.Publisher;
import com.example.bookdbbackend.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PublisherService implements IPublisherService{

    @Autowired
    private final PublisherRepository publisherRepository;

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
            throw new UserNotFoundException("Publisher not found with id: " + id);
        }
        return publisherRepository.findById(id).get();
    }

    @Override
    public Publisher getPublisherByCountry(String country) {
        Optional<Publisher> publisher = publisherRepository.findPublisherByCountry(country);
        if (!publisher.isPresent()) {
            throw new PublisherNotFoundException("Publisher not found from: " + country);
        }
        return publisher.get();
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
