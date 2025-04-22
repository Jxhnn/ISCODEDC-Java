package com.gamesUP.gamesUP.services;

import com.gamesUP.gamesUP.model.Author;

import java.util.List;

public interface AuthorService {
    List<Author> getAll();
    Author getById(Integer id);
    Author save(Author author);
    Author update(Integer id, Author author);
    void delete(Integer id);
}
