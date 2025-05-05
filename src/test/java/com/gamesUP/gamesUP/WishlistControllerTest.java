package com.gamesUP.gamesUP;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesUP.gamesUP.config.SecurityConfig;
import com.gamesUP.gamesUP.dto.CustomUserDetails;
import com.gamesUP.gamesUP.enums.Role;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.model.Wishlist;
import com.gamesUP.gamesUP.services.GameService;
import com.gamesUP.gamesUP.services.WishlistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
public class WishlistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private WishlistService wishlistService;

    @MockitoBean
    private GameService gameService;

    private CustomUserDetails clientUserDetails;
    private CustomUserDetails adminUserDetails;
    private User clientUser;
    private User adminUser;
    private Game game1;
    private Game game2;
    private Wishlist clientWishlist;
    private Wishlist adminWishlist;

    @BeforeEach
    void setUp() {
        clientUser = new User();
        clientUser.setId(1);
        clientUser.setUsername("client");
        clientUser.setPassword("pass");
        clientUser.setRole(Role.CLIENT);
        clientUserDetails = new CustomUserDetails(clientUser);

        adminUser = new User();
        adminUser.setId(2);
        adminUser.setUsername("admin");
        adminUser.setPassword("adminpass");
        adminUser.setRole(Role.ADMIN);
        adminUserDetails = new CustomUserDetails(adminUser);

        game1 = new Game();
        game1.setId(10);
        game1.setTitle("Wish Game 1");

        game2 = new Game();
        game2.setId(11);
        game2.setTitle("Wish Game 2");

        clientWishlist = new Wishlist();
        clientWishlist.setId(50);
        clientWishlist.setUser(clientUser);
        clientWishlist.setGames(new ArrayList<>(List.of(game1)));

        adminWishlist = new Wishlist();
        adminWishlist.setId(51);
        adminWishlist.setUser(adminUser);
        adminWishlist.setGames(List.of(game2));
    }


    @Test
    void clientCanGetOwnWishlist() throws Exception {

        when(wishlistService.getByUserId(eq(clientUser.getId()))).thenReturn(List.of(clientWishlist));

        mockMvc.perform(get("/api/wishlists")
                        .with(user(clientUserDetails))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(clientWishlist.getId()))
                .andExpect(jsonPath("$[0].user.id").value(clientUser.getId()))
                .andExpect(jsonPath("$[0].games", hasSize(1)))
                .andExpect(jsonPath("$[0].games[0].id").value(game1.getId()));

    }

    @Test
    void unauthenticatedCannotGetWishlist() throws Exception {
        mockMvc.perform(get("/api/wishlists")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void clientCanUpdateWishlistToAddGame() throws Exception {

        Wishlist desiredState = new Wishlist();
        List<Map<String, Integer>> targetGames = List.of(
                Map.of("id", game1.getId()),
                Map.of("id", game2.getId())
        );

        Map<String, Object> requestBody = Map.of("games", targetGames);

        Wishlist finalWishlistState = new Wishlist();
        finalWishlistState.setId(clientWishlist.getId());
        finalWishlistState.setUser(clientUser);
        finalWishlistState.setGames(List.of(game1, game2));

        when(wishlistService.getByUserId(eq(clientUser.getId()))).thenReturn(List.of(clientWishlist));

        ArgumentCaptor<Wishlist> wishlistCaptor = ArgumentCaptor.forClass(Wishlist.class);
        when(wishlistService.update(eq(clientWishlist.getId()), wishlistCaptor.capture())).thenReturn(finalWishlistState);
        when(gameService.getById(eq(game1.getId()))).thenReturn(game1);
        when(gameService.getById(eq(game2.getId()))).thenReturn(game2);

        mockMvc.perform(put("/api/wishlists/" + clientWishlist.getId())
                        .with(user(clientUserDetails))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.id").value(clientWishlist.getId()))
                .andExpect(jsonPath("$.games", hasSize(2)))
                .andExpect(jsonPath("$.games[0].id").value(game1.getId()))
                .andExpect(jsonPath("$.games[1].id").value(game2.getId()));

        verify(wishlistService).update(eq(clientWishlist.getId()), any(Wishlist.class));
        Wishlist capturedWishlist = wishlistCaptor.getValue();

        assertEquals(2, capturedWishlist.getGames().size());
        assertTrue(capturedWishlist.getGames().stream().anyMatch(g -> g.getId().equals(game1.getId())));
        assertTrue(capturedWishlist.getGames().stream().anyMatch(g -> g.getId().equals(game2.getId())));
    }

    @Test
    void clientCanUpdateWishlistToRemoveGame() throws Exception {
        clientWishlist.getGames().add(game2);
        Integer wishlistId = clientWishlist.getId();

        List<Map<String, Integer>> targetGames = List.of(Map.of("id", game2.getId()));
        Map<String, Object> requestBody = Map.of("games", targetGames);

        Wishlist finalWishlistState = new Wishlist();
        finalWishlistState.setId(wishlistId);
        finalWishlistState.setUser(clientUser);
        finalWishlistState.setGames(List.of(game2));

        when(wishlistService.getByUserId(eq(clientUser.getId()))).thenReturn(List.of(clientWishlist));
        ArgumentCaptor<Wishlist> wishlistCaptor = ArgumentCaptor.forClass(Wishlist.class);
        when(wishlistService.update(eq(wishlistId), wishlistCaptor.capture())).thenReturn(finalWishlistState);
        when(gameService.getById(eq(game2.getId()))).thenReturn(game2);

        mockMvc.perform(put("/api/wishlists/" + wishlistId)
                        .with(user(clientUserDetails))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.id").value(wishlistId))
                .andExpect(jsonPath("$.games", hasSize(1)))
                .andExpect(jsonPath("$.games[0].id").value(game2.getId()));

        verify(wishlistService).update(eq(wishlistId), any(Wishlist.class));
    }


    @Test
    @WithMockUser(username="admin", roles={"ADMIN"})
    void adminCanUpdateOwnWishlist() throws Exception {

        Wishlist adminExistingWishlist = new Wishlist();
        adminExistingWishlist.setId(51);
        adminExistingWishlist.setUser(adminUser);
        adminExistingWishlist.setGames(new ArrayList<>(List.of(game2)));
        Integer adminWishlistId = adminExistingWishlist.getId();

        List<Map<String, Integer>> targetGames = List.of(
                Map.of("id", game1.getId()),
                Map.of("id", game2.getId())
        );
        Map<String, Object> requestBody = Map.of("games", targetGames);

        Wishlist finalWishlistState = new Wishlist();
        finalWishlistState.setId(adminWishlistId);
        finalWishlistState.setUser(adminUser);
        finalWishlistState.setGames(List.of(game1, game2));

        when(wishlistService.getByUserId(eq(adminUser.getId()))).thenReturn(List.of(adminExistingWishlist));
        when(wishlistService.update(eq(adminWishlistId), any(Wishlist.class))).thenReturn(finalWishlistState);
        when(gameService.getById(eq(game1.getId()))).thenReturn(game1);
        when(gameService.getById(eq(game2.getId()))).thenReturn(game2);

        mockMvc.perform(put("/api/wishlists/" + adminWishlistId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.id").value(adminWishlistId))
                .andExpect(jsonPath("$.user.id").value(adminUser.getId()))
                .andExpect(jsonPath("$.games", hasSize(2)));

        verify(wishlistService).update(eq(adminWishlistId), any(Wishlist.class));
    }

    @Test
    void unauthenticatedCannotUpdateWishlist() throws Exception {
        List<Integer> targetGameIds = List.of(game1.getId());
        Map<String, List<Integer>> requestBody = Map.of("gameIds", targetGameIds);

        mockMvc.perform(put("/api/wishlists")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}