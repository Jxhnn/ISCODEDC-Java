package com.gamesUP.gamesUP.controllers;

import com.gamesUP.gamesUP.dto.CustomUserDetails;
import com.gamesUP.gamesUP.model.Wishlist;
import com.gamesUP.gamesUP.services.WishlistService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlists")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping
    public List<Wishlist> getAll(@AuthenticationPrincipal CustomUserDetails currentUserDetails) {

        boolean isAdmin = currentUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);

        if (isAdmin) return wishlistService.getAll();
        else return wishlistService.getByUserId(currentUserDetails.getId());
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasRole('ADMIN') or (returnObject != null and returnObject.user != null and returnObject.user.id == authentication.principal.id)")
    public Wishlist getById(@PathVariable Integer id) {
        return wishlistService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Wishlist create(@RequestBody Wishlist wishlist) {
        return wishlistService.save(wishlist);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostAuthorize("hasRole('ADMIN') or (returnObject != null and returnObject.user != null and returnObject.user.id == authentication.principal.id)")
    public Wishlist update(@PathVariable Integer id, @RequestBody Wishlist user) {
        return wishlistService.update(id, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostAuthorize("hasRole('ADMIN') or (returnObject != null and returnObject.user != null and returnObject.user.id == authentication.principal.id)")
    public void delete(@PathVariable Integer id) {
        wishlistService.delete(id);
    }
}
