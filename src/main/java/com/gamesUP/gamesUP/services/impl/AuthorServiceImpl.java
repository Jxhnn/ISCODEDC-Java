package com.gamesUP.gamesUP.services.impl;


import com.gamesUP.gamesUP.model.Author;
import com.gamesUP.gamesUP.repositories.AuthorRepository;
import com.gamesUP.gamesUP.services.AuthorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> getAll() {
        return (List<Author>) authorRepository.findAll();
    }

    public Author getById(Integer id) {
        return authorRepository.findById(id).orElseThrow();
    }

    public Author save(Author author) {
        return authorRepository.save(author);
    }

    public Author update(Integer id, Author author) {
        Author existing = getById(id);
        existing.setGames(author.getGames());
        existing.setName(author.getName());
        return authorRepository.save(existing);
    }

    public void delete(Integer id) {
        authorRepository.deleteById(id);
    }
}

