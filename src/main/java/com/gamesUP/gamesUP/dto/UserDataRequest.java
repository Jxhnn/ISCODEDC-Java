package com.gamesUP.gamesUP.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class UserDataRequest {

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("purchases")
    private List<UserPurchase> purchases;
}
