package com.gamesUP.gamesUP.services.impl;

import com.gamesUP.gamesUP.model.Review;
import com.gamesUP.gamesUP.repositories.ReviewRepository;
import com.gamesUP.gamesUP.services.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getAll() {
        return (List<Review>) reviewRepository.findAll();
    }

    public Review getById(Integer id) {
        return reviewRepository.findById(id).orElseThrow();
    }

    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    public Review update(Integer id, Review review) {
        Review existing = getById(id);
        existing.setUser(review.getUser());
        existing.setGame(review.getGame());
        existing.setRating(review.getRating());
        existing.setComment(review.getComment());
        return reviewRepository.save(existing);
    }

    public void delete(Integer id) {
        reviewRepository.deleteById(id);
    }
}

