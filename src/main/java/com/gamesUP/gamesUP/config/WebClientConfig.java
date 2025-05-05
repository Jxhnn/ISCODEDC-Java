package com.gamesUP.gamesUP.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${python.recommendation.api.url}")
    private String pythonRecommendationApiUrl;

    @Bean
    public WebClient pythonRecommendationWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(pythonRecommendationApiUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
