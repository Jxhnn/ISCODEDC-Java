package com.gamesUP.gamesUP.services;

import com.gamesUP.gamesUP.model.Review;
import java.util.List;

public interface ReviewService {
    List<Review> getAll();
    Review getById(Integer id);
    Review save(Review review);
    Review update(Integer id, Review review);
    void delete(Integer id);
}
