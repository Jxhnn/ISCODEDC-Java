package com.gamesUP.gamesUP.services;

import com.gamesUP.gamesUP.model.Game;

import java.util.List;

public interface GameService {
    List<Game> getAllGames();
    Game getGameById(Integer id);
    Game saveGame(Game game);
    Game updateGame(Integer id, Game game);
    void deleteGame(Integer id);
}
