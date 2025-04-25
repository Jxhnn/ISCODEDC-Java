package com.gamesUP.gamesUP;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesUP.gamesUP.dto.CustomUserDetails;
import com.gamesUP.gamesUP.enums.Role;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.services.UserService;
import com.gamesUP.gamesUP.config.SecurityConfig;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    private CustomUserDetails clientUserDetails;

    @BeforeEach
    void setUp() {
        User testUser = new User();
        testUser.setId(1);
        testUser.setUsername("client");
        testUser.setPassword("pass");
        testUser.setRole(Role.CLIENT);

        clientUserDetails = new CustomUserDetails(testUser);
    }

    @Test
    @WithMockUser(username = "admin", roles = {
            "ADMIN"
    })
    void adminCanAccessUserList() throws Exception {
        mockMvc.perform(get("/api/users")
                .with(csrf()
                )
        ).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "client", roles = {
            "CLIENT"
    })
    void clientCannotAccessAdminEndpoint() throws Exception {
        mockMvc.perform(get("/api/users")
                .with(csrf()
                )
        ).andExpect(status().isForbidden());
    }

    @Test
    void clientCanAccessOwnProfile() throws Exception {
        User user = new User();
        user.setId(1);
        user.setUsername("client");

        when(userService.getById(1)).thenReturn(user);

        mockMvc.perform(get("/api/users/1")
                .with(csrf())
                .with(user(clientUserDetails)
                )
        ).andExpect(status().isOk());
    }

    @Test
    void clientCannotModifyAnotherUser() throws Exception {
        mockMvc.perform(put("/api/users/99")
                .with(csrf())
                .with(user(clientUserDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"hacker\"}")
        ).andExpect(status().isForbidden());
    }

    @Test
    void clientCannotUpgradeOwnRoleToAdmin() throws Exception {
        User clientBefore = new User();
        clientBefore.setId(1);
        clientBefore.setUsername("client");
        clientBefore.setRole(Role.CLIENT);

        User roleUpdateAttempt = new User();
        roleUpdateAttempt.setUsername("client");
        roleUpdateAttempt.setEmail("client@example.com");
        roleUpdateAttempt.setPassword("pass");
        roleUpdateAttempt.setRole(Role.ADMIN);

        when(userService.getById(1)).thenReturn(clientBefore);
        when(userService.update(eq(1), any(User.class))).then(invocation -> {
            User updatedUser = invocation.getArgument(1);
            updatedUser.setRole(Role.CLIENT);
            return updatedUser;
        });

        mockMvc.perform(put("/api/users/1")
                .with(user(clientUserDetails))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleUpdateAttempt))
        ).andExpect(status().isOk()).andExpect(jsonPath("$.role").value("CLIENT"));
    }

    @Test
    void clientCannotCreateUser() throws Exception {
        User newUser = new User();
        newUser.setUsername("testUsername");
        newUser.setPassword("passtest");
        newUser.setRole(Role.CLIENT);

        mockMvc.perform(post("/api/users").with(user(clientUserDetails))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser))
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {
            "ADMIN"
    })
    void adminCanCreateUser() throws Exception {
        User userToCreate = new User();
        userToCreate.setUsername("newUser");
        userToCreate.setPassword("pass");
        userToCreate.setRole(Role.CLIENT);

        when(userService.save(any(User.class))).thenReturn(userToCreate);

        mockMvc.perform(post("/api/users")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToCreate))
        ).andExpect(status().isOk()).andExpect(jsonPath("$.username").value("newUser"));
    }

    @Test
    void clientCannotDeleteOtherUser() throws Exception {
        mockMvc.perform(delete("/api/users/2").with(csrf()).with(user(clientUserDetails))).andExpect(status().isForbidden());
    }

}