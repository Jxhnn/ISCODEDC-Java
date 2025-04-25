package com.gamesUP.gamesUP.model;

import com.gamesUP.gamesUP.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String username;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Wishlist> wishlists;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Purchase> purchases;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> avis;
}
