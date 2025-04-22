package com.gamesUP.gamesUP.dto.response;

import com.gamesUP.gamesUP.model.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
public class GameResponse {
    private Integer id;
    private String title;
    private double price;
    private LocalDate releaseDate;
    private PublisherResponse publisher;
    private AuthorResponse author;
    private List<CategoryResponse> categories;
    private int numEdition;
    private String type;
    private List<Review> reviews;
}
