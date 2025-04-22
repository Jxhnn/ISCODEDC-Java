package com.gamesUP.gamesUP.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AuthorRequest {
    @NotBlank
    private String name;
}
