package com.gamesUP.gamesUP.controllers;

import com.gamesUP.gamesUP.dto.CustomUserDetails;
import com.gamesUP.gamesUP.dto.UserDataRequest;
import com.gamesUP.gamesUP.services.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Autowired
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/for-me")
    public ResponseEntity<List<Integer>> getRecommendationsForAuthenticatedUser(Authentication authentication) {

        Object principal = authentication.getPrincipal();

        Integer userId = null;
        if (principal instanceof CustomUserDetails) {

            String username = ((UserDetails) principal).getUsername();
            userId = ((CustomUserDetails) principal).getId();
        } else {
            System.err.println("Authenticated principal is not UserDetails. Cannot get user ID.");
            return ResponseEntity.status(500).body(Collections.emptyList());
        }

        if (userId == null) {
            System.err.println("Could not determine user ID from authenticated principal.");
            return ResponseEntity.status(500).body(Collections.emptyList());
        }

        System.out.println("Generating recommendations for authenticated user ID: " + userId);
        List<Integer> recommendedGameIds = recommendationService.getGameRecommendationsForUser(userId);

        return ResponseEntity.ok(recommendedGameIds);
    }
}
