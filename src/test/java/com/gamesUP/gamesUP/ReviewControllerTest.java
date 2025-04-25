package com.gamesUP.gamesUP;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesUP.gamesUP.config.SecurityConfig;
import com.gamesUP.gamesUP.dto.CustomUserDetails;
import com.gamesUP.gamesUP.enums.Role;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Review;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.services.GameService;
import com.gamesUP.gamesUP.services.ReviewService;
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


import java.util.Map;

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
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReviewService reviewService;

    @MockitoBean
    private GameService gameService;

    private CustomUserDetails clientUserDetails;
    private CustomUserDetails adminUserDetails;
    private User clientUser;
    private User adminUser;
    private Game testGame;
    private Review clientReview;

    @BeforeEach
    void setUp() {
        clientUser = new User();
        clientUser.setId(1);
        clientUser.setUsername("client");
        clientUser.setPassword("password");
        clientUser.setRole(Role.CLIENT);
        clientUserDetails = new CustomUserDetails(clientUser);

        adminUser = new User();
        adminUser.setId(2);
        adminUser.setUsername("admin");
        adminUser.setPassword("adminpass");
        adminUser.setRole(Role.ADMIN);
        adminUserDetails = new CustomUserDetails(adminUser);


        testGame = new Game();
        testGame.setId(10);
        testGame.setTitle("Test Board Game");

        clientReview = new Review();
        clientReview.setId(100);
        clientReview.setComment("Great game!");
        clientReview.setRating(5);
        clientReview.setUser(clientUser);
        clientReview.setGame(testGame);
    }

    @Test
    void clientCanCreateReview() throws Exception {
        Map<String, Object> reviewInput = Map.of(
                "comment", "Awesome!",
                "rating", 5,
                "gameId", testGame.getId()
        );

        Review savedReview = new Review();
        savedReview.setId(101);
        savedReview.setComment("Awesome!");
        savedReview.setRating(5);
        savedReview.setUser(clientUser);
        savedReview.setGame(testGame);

        when(gameService.getById(testGame.getId())).thenReturn(testGame);
        when(reviewService.save(any(Review.class))).thenReturn(savedReview);


        mockMvc.perform(post("/api/reviews")
                        .with(user(clientUserDetails))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewInput)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101))
                .andExpect(jsonPath("$.comment").value("Awesome!"))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.user.id").value(clientUser.getId()))
                .andExpect(jsonPath("$.game.id").value(testGame.getId()));
    }

    @Test
    @WithMockUser(username="admin", roles={"ADMIN"})
    void adminCanCreateReview() throws Exception {
        Map<String, Object> reviewInput = Map.of(
                "comment", "Admin review",
                "rating", 3,
                "gameId", testGame.getId()
        );

        Review savedReview = new Review();
        savedReview.setId(102);
        savedReview.setComment("Admin review");
        savedReview.setRating(3);

        User currentlyAuthenticatedAdmin = new User();
        currentlyAuthenticatedAdmin.setId(adminUserDetails.getId());
        currentlyAuthenticatedAdmin.setUsername(adminUserDetails.getUsername());
        currentlyAuthenticatedAdmin.setRole(Role.ADMIN);

        savedReview.setUser(currentlyAuthenticatedAdmin);
        savedReview.setGame(testGame);

        when(gameService.getById(testGame.getId())).thenReturn(testGame);
        when(reviewService.save(any(Review.class))).thenReturn(savedReview);


        mockMvc.perform(post("/api/reviews")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewInput)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(102))
                .andExpect(jsonPath("$.comment").value("Admin review"))
                .andExpect(jsonPath("$.user.username").value("admin"));
    }

    @Test
    void unauthenticatedCannotCreateReview() throws Exception {
        Map<String, Object> reviewInput = Map.of(
                "comment", "Trying without login",
                "rating", 1,
                "gameId", testGame.getId()
        );

        mockMvc.perform(post("/api/reviews")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewInput)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void clientCanUpdateOwnReview() throws Exception {
        Map<String, Object> reviewUpdates = Map.of(
                "comment", "Updated comment!",
                "rating", 4
        );

        Review updatedReview = new Review();
        updatedReview.setId(clientReview.getId());
        updatedReview.setComment("Updated comment!");
        updatedReview.setRating(4);
        updatedReview.setUser(clientUser);
        updatedReview.setGame(testGame);

        when(reviewService.update(eq(clientReview.getId()), any(Review.class)))
                .thenReturn(updatedReview);

        when(reviewService.getById(clientReview.getId())).thenReturn(clientReview);


        mockMvc.perform(put("/api/reviews/" + clientReview.getId())
                        .with(user(clientUserDetails))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewUpdates)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clientReview.getId()))
                .andExpect(jsonPath("$.comment").value("Updated comment!"))
                .andExpect(jsonPath("$.rating").value(4));
    }

    @Test
    void clientCannotUpdateOthersReview() throws Exception {
        User otherUser = new User();
        otherUser.setId(99);
        otherUser.setUsername("other");
        otherUser.setRole(Role.CLIENT);

        Review otherReview = new Review();
        otherReview.setId(105);
        otherReview.setComment("Someone else's review");
        otherReview.setRating(2);
        otherReview.setUser(otherUser);
        otherReview.setGame(testGame);

        Map<String, Object> reviewUpdates = Map.of(
                "comment", "Trying to hack",
                "rating", 1
        );

        when(reviewService.getById(otherReview.getId())).thenReturn(otherReview);
        when(reviewService.update(eq(otherReview.getId()), any(Review.class)))
                .thenThrow(new org.springframework.security.access.AccessDeniedException("User cannot update this review"));


        mockMvc.perform(put("/api/reviews/" + otherReview.getId())
                        .with(user(clientUserDetails))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewUpdates)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username="admin", roles={"ADMIN"})
    void adminCanUpdateAnyReview() throws Exception {
        Map<String, Object> reviewUpdates = Map.of(
                "comment", "Moderated comment by admin",
                "rating", 3
        );

        Review updatedByAdmin = new Review();
        updatedByAdmin.setId(clientReview.getId());
        updatedByAdmin.setComment("Moderated comment by admin");
        updatedByAdmin.setRating(3);
        updatedByAdmin.setUser(clientUser);
        updatedByAdmin.setGame(testGame);

        when(reviewService.getById(clientReview.getId())).thenReturn(clientReview);
        when(reviewService.update(eq(clientReview.getId()), any(Review.class)))
                .thenReturn(updatedByAdmin);


        mockMvc.perform(put("/api/reviews/" + clientReview.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewUpdates)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value("Moderated comment by admin"))
                .andExpect(jsonPath("$.rating").value(3));
    }

    @Test
    void clientCannotDeleteReview() throws Exception {
        when(reviewService.getById(clientReview.getId())).thenReturn(clientReview);

        mockMvc.perform(delete("/api/reviews/" + clientReview.getId())
                        .with(user(clientUserDetails))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username="admin", roles={"ADMIN"})
    void adminCanDeleteAnyReview() throws Exception {
        when(reviewService.getById(clientReview.getId())).thenReturn(clientReview);
        doNothing().when(reviewService).delete(clientReview.getId());

        mockMvc.perform(delete("/api/reviews/" + clientReview.getId())
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());

        verify(reviewService, times(1)).delete(clientReview.getId());
    }
}