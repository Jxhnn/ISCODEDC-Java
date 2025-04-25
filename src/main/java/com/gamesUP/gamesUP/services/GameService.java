package com.gamesUP.gamesUP.services;

import com.gamesUP.gamesUP.model.Game;

import java.util.List;

public interface GameService {
    List<Game> getAll();
    Game getById(Integer id);
    Game save(Game game);
    Game update(Integer id, Game game);
    void delete(Integer id);
}