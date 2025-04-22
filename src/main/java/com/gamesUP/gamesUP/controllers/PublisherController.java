package com.gamesUP.gamesUP.controllers;

import com.gamesUP.gamesUP.model.Publisher;
import com.gamesUP.gamesUP.services.PublisherService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping
    public List<Publisher> getAll() {
        return publisherService.getAll();
    }

    @GetMapping("/{id}") public Publisher getById(@PathVariable Integer id) {
        return publisherService.getById(id);
    }

    @PostMapping
    public Publisher create(@RequestBody Publisher publisher) {
        return publisherService.save(publisher);
    }

    @PutMapping("/{id}")
    public Publisher update(@PathVariable Integer id, @RequestBody Publisher publisher) {
        return publisherService.update(id, publisher);
    }

    @DeleteMapping("/{id}") public void delete(@PathVariable Integer id) {
        publisherService.delete(id);
    }
}
