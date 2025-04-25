package com.gamesUP.gamesUP.controllers;

import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public User getById(@PathVariable Integer id) {
        return userService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public User create(@RequestBody User user) {
        return userService.save(user);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public User update(@PathVariable Integer id, @RequestBody User user) {
        return userService.update(id, user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public void delete(@PathVariable Integer id) {
        userService.delete(id);
    }
}
