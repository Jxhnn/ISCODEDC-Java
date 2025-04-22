package com.gamesUP.gamesUP.services;

import com.gamesUP.gamesUP.model.User;

import java.util.List;

public interface UserService {
    List<User> getAll();
    User getById(Integer id);
    User save(User user);
    User update(Integer id, User user);
    void delete(Integer id);
}
