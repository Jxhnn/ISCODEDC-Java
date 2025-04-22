package com.gamesUP.gamesUP.dto.response;

import com.gamesUP.gamesUP.model.Game;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class AuthorResponse {
    private Integer id;
    private String name;
    private List<Game> games;
}
