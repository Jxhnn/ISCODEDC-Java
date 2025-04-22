package com.gamesUP.gamesUP.services;

import com.gamesUP.gamesUP.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAll();
    Category getById(Integer id);
    Category save(Category category);
    Category update(Integer id, Category category);
    void delete(Integer id);
}
