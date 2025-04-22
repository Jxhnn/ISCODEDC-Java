package com.gamesUP.gamesUP.services.impl;

import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.repositories.PublisherRepository;
import com.gamesUP.gamesUP.services.PublisherService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;

    public PublisherServiceImpl(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public List<Publisher> getAll() {
        return (List<Publisher>) publisherRepository.findAll();
    }

    public Publisher getById(Integer id) {
        return publisherRepository.findById(id).orElseThrow();
    }

    public Publisher save(Publisher publisher) {
        return publisherRepository.save(publisher);
    }

    public Publisher update(Integer id, Publisher publisher) {
        Publisher existing = getById(id);
        existing.setGames(publisher.getGames());
        existing.setName(publisher.getName());
        return publisherRepository.save(existing);
    }

    public void delete(Integer id) {
        publisherRepository.deleteById(id);
    }
}

