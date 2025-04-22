package com.gamesUP.gamesUP.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
public class GameRequest {
    @NotBlank
    private String title;

    @NotNull
    @Positive
    private Double price;

    @NotNull private LocalDate releaseDate;
    @NotNull private Integer publisherId;
    @NotNull private Integer authorId;
    @NotEmpty
    private List<Integer> categoryIds;

    @NotNull
    @Min(1)
    private Integer numEdition;

    @NotBlank
    private String type;
}
