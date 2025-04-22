package com.gamesUP.gamesUP.services.impl;

import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.repositories.GameRepository;
import com.gamesUP.gamesUP.services.GameService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public List<Game> getAllGames() {
        return (List<Game>) gameRepository.findAll();
    }

    @Override
    public Game getGameById(Integer id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game not found"));
    }

    @Override
    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    @Override
    public Game updateGame(Integer id, Game game) {
        Game existing = getGameById(id);
        existing.setTitle(game.getTitle());
        existing.setPrice(game.getPrice());
        existing.setReleaseDate(game.getReleaseDate());
        existing.setPublisher(game.getPublisher());
        existing.setAuthor(game.getAuthor());
        existing.setCategories(game.getCategories());
        return gameRepository.save(existing);
    }

    @Override
    public void deleteGame(Integer id) {
        gameRepository.deleteById(id);
    }
}
