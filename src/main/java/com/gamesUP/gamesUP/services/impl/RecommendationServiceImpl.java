package com.gamesUP.gamesUP.services.impl;

import com.gamesUP.gamesUP.dto.RecommendationResponse;
import com.gamesUP.gamesUP.dto.UserDataRequest;
import com.gamesUP.gamesUP.dto.UserPurchase;
import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.model.PurchaseItem;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.repositories.UserRepository;
import com.gamesUP.gamesUP.services.RecommendationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final WebClient pythonRecommendationWebClient;
    private final UserRepository userRepository;

    public RecommendationServiceImpl(
            @Qualifier("pythonRecommendationWebClient") WebClient pythonRecommendationWebClient,
            UserRepository userRepository
    ) {
        this.pythonRecommendationWebClient = pythonRecommendationWebClient;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<Integer> getGameRecommendationsForUser(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            System.err.println("User not found with ID: " + userId);
            return Collections.emptyList();
        }

        User user = userOptional.get();

        List<UserPurchase> userPurchases = new ArrayList<>();
        if (user.getPurchases() != null) {
            for (Purchase purchase : user.getPurchases()) {
                if (purchase.isPaid() && !purchase.isArchived() && purchase.getPurchaseLines() != null) {
                    for (PurchaseItem item : purchase.getPurchaseLines()) {
                        if (item.getGame() != null && item.getGame().getId() != null) {
                            UserPurchase purchaseDto = new UserPurchase();
                            purchaseDto.setGameId(item.getGame().getId());
                            purchaseDto.setRating(5.0);
                            userPurchases.add(purchaseDto);
                        } else {
                            System.err.println("Skipping purchase item with null game or game ID for user " + userId);
                        }
                    }
                }
            }
        } else {
            System.out.println("User " + userId + " has no purchase history.");
        }


        UserDataRequest requestDto = new UserDataRequest();
        requestDto.setUserId(user.getId());
        requestDto.setPurchases(userPurchases);

        return callPythonRecommendationApi(requestDto);
    }

    public List<Integer> callPythonRecommendationApi(UserDataRequest userData) {
        try {
            System.out.println("Calling Python API for user " + userData.getUserId() + " with " + userData.getPurchases().size() + " purchases.");

            Mono<RecommendationResponse> responseMono = pythonRecommendationWebClient.post()
                    .uri("/recommendations/")
                    .bodyValue(userData)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class).map(body -> new RuntimeException("Python API error: " + response.statusCode() + " - " + body)))
                    .bodyToMono(RecommendationResponse.class);

            RecommendationResponse responseDto = responseMono.block();

            if (responseDto != null && responseDto.getRecommendations() != null) {
                System.out.println("Received " + responseDto.getRecommendations().size() + " recommendations from Python API.");
                return responseDto.getRecommendations();
            } else {
                System.err.println("Python API returned null response or recommendations list.");
                return Collections.emptyList();
            }

        } catch (Exception e) {
            System.err.println("Error calling Python Recommendation API: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
