package com.gamesUP.gamesUP.repositories;

import com.gamesUP.gamesUP.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> { }
