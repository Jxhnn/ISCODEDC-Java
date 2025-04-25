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

    public List<Game> getAll() {
        return (List<Game>) gameRepository.findAll();
    }

    public Game getById(Integer id) {
        return gameRepository.findById(id).orElseThrow();
    }

    public Game save(Game game) {
        return gameRepository.save(game);
    }

    public Game update(Integer id, Game game) {
        Game existing = getById(id);

        return gameRepository.save(existing);
    }

    public void delete(Integer id) {
        gameRepository.deleteById(id);
    }
}

