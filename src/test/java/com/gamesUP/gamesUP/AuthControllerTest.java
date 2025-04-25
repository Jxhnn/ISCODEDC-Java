package com.gamesUP.gamesUP;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesUP.gamesUP.config.SecurityConfig;
import com.gamesUP.gamesUP.controllers.AuthController;
import com.gamesUP.gamesUP.dto.LoginRequest;
import com.gamesUP.gamesUP.dto.RegisterRequest;
import com.gamesUP.gamesUP.enums.Role;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.repositories.UserRepository; // Needed by SecurityConfig
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

// Import static helpers
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private ObjectMapper objectMapper;

    private User existingUser;
    private final String rawPassword = "password123";
    private final String username = "existinguser";

    @BeforeEach
    void setUp() {
        String encodedPassword = passwordEncoder.encode(rawPassword);

        existingUser = new User();
        existingUser.setId(1);
        existingUser.setUsername(username);
        existingUser.setPassword(encodedPassword);
        existingUser.setEmail("existing@example.com");
        existingUser.setRole(Role.CLIENT);

        User newUserInput = new User();
        newUserInput.setUsername("newuser");
        newUserInput.setPassword(rawPassword);
        newUserInput.setEmail("new@example.com");
    }

    @Test
    void shouldRegisterNewUser() throws Exception {

        RegisterRequest request = new RegisterRequest("testuser", "password123");
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    void shouldNotRegisterExistingUser() throws Exception {
        User duplicateUserInput = new User();
        duplicateUserInput.setUsername(existingUser.getUsername()); // Existing username
        duplicateUserInput.setPassword("some_password");
        duplicateUserInput.setEmail("duplicate@example.com");
        String requestJson = objectMapper.writeValueAsString(duplicateUserInput);

        when(userRepository.findByUsername(existingUser.getUsername()))
                .thenReturn(Optional.of(existingUser));

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isConflict()) // Expect 409 Conflict
                .andExpect(content().string("Username already exists"));
    }

    @Test
    void shouldLogin() throws Exception {

        // Simuler l'utilisateur en base avec le bon mot de passe
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(rawPassword, existingUser.getPassword())).thenReturn(true);

        // Préparer la requête de login (suppose un objet LoginRequest similaire au RegisterRequest)
        LoginRequest loginRequest = new LoginRequest(username, rawPassword);
        String loginJson = objectMapper.writeValueAsString(loginRequest);

        // Simuler un AuthenticationManager qui authentifie correctement (selon votre logique)
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, rawPassword);

        when(authenticationManager.authenticate(authenticationToken))
                .thenReturn(authenticationToken);

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }
}

