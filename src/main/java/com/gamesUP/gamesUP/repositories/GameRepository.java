package com.gamesUP.gamesUP.repositories;

import com.gamesUP.gamesUP.model.Game;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, Integer> {  }
