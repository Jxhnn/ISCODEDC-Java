package com.gamesUP.gamesUP.controllers;

import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.services.GameService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public List<Game> getAll() {
        return gameService.getAll();
    }

    @GetMapping("/{id}")
    public Game getById(@PathVariable Integer id) {
        return gameService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Game create(@RequestBody Game game) {
        return gameService.save(game);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Game update(@PathVariable Integer id, @RequestBody Game game) {
        return gameService.update(id, game);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Integer id) {
        gameService.delete(id);
    }
}
