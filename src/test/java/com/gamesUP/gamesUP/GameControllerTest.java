package com.gamesUP.gamesUP;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesUP.gamesUP.config.SecurityConfig;
import com.gamesUP.gamesUP.dto.CustomUserDetails;
import com.gamesUP.gamesUP.enums.Role;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.services.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GameService gameService;

    private CustomUserDetails clientUserDetails;
    private Game testGame;

    @BeforeEach
    void setUp() {
        User testClientUser = new User();
        testClientUser.setId(1);
        testClientUser.setUsername("client");
        testClientUser.setPassword("pass");
        testClientUser.setRole(Role.CLIENT);
        clientUserDetails = new CustomUserDetails(testClientUser);

        testGame = new Game();
        testGame.setId(1);
        testGame.setTitle("Test Game");
        testGame.setPrice(100.00);
    }

    @Test
    void clientCanViewGamesList() throws Exception {
        when(gameService.getAll()).thenReturn(Collections.singletonList(testGame));

        mockMvc.perform(get("/api/games")
                        .with(user(clientUserDetails))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Game"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminCanViewGamesList() throws Exception {
        when(gameService.getAll()).thenReturn(Collections.singletonList(testGame));

        mockMvc.perform(get("/api/games")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Game"));
    }

    @Test
    void clientCanViewSingleGame() throws Exception {
        when(gameService.getById(1)).thenReturn(testGame);

        mockMvc.perform(get("/api/games/1")
                        .with(user(clientUserDetails))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Game"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminCanViewSingleGame() throws Exception {
        when(gameService.getById(1)).thenReturn(testGame);

        mockMvc.perform(get("/api/games/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Game"));
    }

    @Test
    void clientCannotCreateGame() throws Exception {
        Game newGame = new Game();
        newGame.setTitle("Client Created Game");

        mockMvc.perform(post("/api/games")
                        .with(user(clientUserDetails))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newGame)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminCanCreateGame() throws Exception {
        Game newGame = new Game();
        newGame.setTitle("Admin Created Game");

        Game savedGame = new Game();
        savedGame.setId(2);
        savedGame.setTitle("Admin Created Game");

        when(gameService.save(any(Game.class))).thenReturn(savedGame);

        mockMvc.perform(post("/api/games")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newGame)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.title").value("Admin Created Game"));
    }

    @Test
    void clientCannotUpdateGame() throws Exception {
        Game updatedGame = new Game();
        updatedGame.setTitle("Updated By Client");

        mockMvc.perform(put("/api/games/1")
                        .with(user(clientUserDetails))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedGame)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminCanUpdateGame() throws Exception {
        Game gameUpdates = new Game();
        gameUpdates.setTitle("Updated Game Name");

        Game updatedGameResult = new Game();
        updatedGameResult.setId(1);
        updatedGameResult.setTitle("Updated Game Name");

        when(gameService.update(eq(1), any(Game.class))).thenReturn(updatedGameResult);

        mockMvc.perform(put("/api/games/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gameUpdates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Updated Game Name"));
    }


    @Test
    void clientCannotDeleteGame() throws Exception {
        mockMvc.perform(delete("/api/games/1")
                        .with(user(clientUserDetails))
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminCanDeleteGame() throws Exception {
        doNothing().when(gameService).delete(1);

        mockMvc.perform(delete("/api/games/1")
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(gameService, times(1)).delete(1);
    }
}