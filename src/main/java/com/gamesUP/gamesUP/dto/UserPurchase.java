package com.gamesUP.gamesUP.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPurchase {

    @JsonProperty("game_id")
    private int gameId;

    @JsonProperty("rating")
    private double rating;
}
