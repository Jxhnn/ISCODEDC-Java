package com.gamesUP.gamesUP.services;


import com.gamesUP.gamesUP.dto.UserDataRequest;

import java.util.List;

public interface RecommendationService {
    List<Integer> getGameRecommendationsForUser(Integer userId);
    List<Integer> callPythonRecommendationApi(UserDataRequest userData);

}
