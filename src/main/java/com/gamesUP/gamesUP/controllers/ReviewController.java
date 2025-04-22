package com.gamesUP.gamesUP.controllers;

import com.gamesUP.gamesUP.model.Review;
import com.gamesUP.gamesUP.services.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public List<Review> getAll() {
        return reviewService.getAll();
    }

    @GetMapping("/{id}") public Review getById(@PathVariable Integer id) {
        return reviewService.getById(id);
    }

    @PostMapping
    public Review create(@RequestBody Review review) {
        return reviewService.save(review);
    }

    @PutMapping("/{id}")
    public Review update(@PathVariable Integer id, @RequestBody Review review) {
        return reviewService.update(id, review);
    }

    @DeleteMapping("/{id}") public void delete(@PathVariable Integer id) {
        reviewService.delete(id);
    }
}
