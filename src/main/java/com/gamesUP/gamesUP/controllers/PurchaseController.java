package com.gamesUP.gamesUP.controllers;

import com.gamesUP.gamesUP.dto.CustomUserDetails;
import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.services.PurchaseService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping

    public List<Purchase> getAll(@AuthenticationPrincipal CustomUserDetails currentUserDetails) {


        boolean isAdmin = currentUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);

        if (isAdmin) return purchaseService.getAll();
        else return purchaseService.getByUserId(currentUserDetails.getId());
    }

    @PostAuthorize("hasRole('ADMIN') or (returnObject != null and returnObject.user != null and returnObject.user.id == authentication.principal.id)")
    @GetMapping("/{id}") public Purchase getById(@PathVariable Integer id) {
        return purchaseService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Purchase create(@RequestBody Purchase purchase) {
        return purchaseService.save(purchase);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public Purchase update(@PathVariable Integer id, @RequestBody Purchase purchase) {
        return purchaseService.update(id, purchase);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}") public void delete(@PathVariable Integer id) {
        purchaseService.delete(id);
    }
}
