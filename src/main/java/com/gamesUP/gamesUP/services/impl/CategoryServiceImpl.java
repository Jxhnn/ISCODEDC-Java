package com.gamesUP.gamesUP.services.impl;

import com.gamesUP.gamesUP.model.Category;
import com.gamesUP.gamesUP.repositories.CategoryRepository;
import com.gamesUP.gamesUP.services.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAll() {
        return (List<Category>) categoryRepository.findAll();
    }

    public Category getById(Integer id) {
        return categoryRepository.findById(id).orElseThrow();
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public Category update(Integer id, Category category) {
        Category existing = getById(id);
        existing.setGames(category.getGames());
        existing.setName(category.getName());
        return categoryRepository.save(existing);
    }

    public void delete(Integer id) {
        categoryRepository.deleteById(id);
    }
}

