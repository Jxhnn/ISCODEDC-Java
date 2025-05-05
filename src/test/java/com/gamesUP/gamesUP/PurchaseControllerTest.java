package com.gamesUP.gamesUP;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesUP.gamesUP.config.SecurityConfig;
import com.gamesUP.gamesUP.dto.CustomUserDetails;
import com.gamesUP.gamesUP.enums.Role;
import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.model.PurchaseItem;
import com.gamesUP.gamesUP.model.User;
import com.gamesUP.gamesUP.services.PurchaseService;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
public class PurchaseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PurchaseService purchaseService;

    private CustomUserDetails clientUserDetails;
    private CustomUserDetails adminUserDetails;
    private User clientUser;
    private User adminUser;
    private Game game1;
    private Game game2;
    private Purchase clientPurchase1;
    private Purchase adminPurchase;

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
        game1.setTitle("Game Alpha");

        game2 = new Game();
        game2.setId(11);
        game2.setTitle("Game Beta");

        clientPurchase1 = new Purchase();
        clientPurchase1.setId(100);
        clientPurchase1.setDate(LocalDate.now());
        clientPurchase1.setPaid(true);
        clientPurchase1.setDelivered(false);
        clientPurchase1.setArchived(false);
        clientPurchase1.setUser(clientUser);

        PurchaseItem item1 = new PurchaseItem();
        item1.setId(200);
        item1.setPurchase(clientPurchase1);
        item1.setGame(game1);
        item1.setQuantity(1);
        item1.setPrice(25.50);

        clientPurchase1.setPurchaseLines(List.of(item1));

        adminPurchase = new Purchase();
        adminPurchase.setId(101);
        adminPurchase.setDate(LocalDate.now().minusDays(1));
        adminPurchase.setPaid(true);
        adminPurchase.setDelivered(true);
        adminPurchase.setArchived(false);
        User anotherUser = new User();
        anotherUser.setId(3);
        anotherUser.setUsername("anotherClient");
        adminPurchase.setUser(anotherUser);

        PurchaseItem item2 = new PurchaseItem();
        item2.setId(201);
        item2.setPurchase(adminPurchase);
        item2.setGame(game2);
        item2.setQuantity(2);
        item2.setPrice(30.00);
        adminPurchase.setPurchaseLines(List.of(item2));

    }

    @Test
    void clientCanCreatePurchase() throws Exception {

        List<Map<String, Object>> itemsInput = List.of(
                Map.of("gameId", game1.getId(), "quantity", 1),
                Map.of("gameId", game2.getId(), "quantity", 2)
        );
        Map<String, Object> purchaseInput = Map.of("items", itemsInput);

        Purchase savedPurchase = new Purchase();
        savedPurchase.setId(102);
        savedPurchase.setDate(LocalDate.now());
        savedPurchase.setUser(clientUser);
        savedPurchase.setPaid(false);
        savedPurchase.setDelivered(false);
        savedPurchase.setArchived(false);

        PurchaseItem savedItem1 = new PurchaseItem(); savedItem1.setId(202); savedItem1.setGame(game1); savedItem1.setQuantity(1); savedItem1.setPrice(25.50); savedItem1.setPurchase(savedPurchase);
        PurchaseItem savedItem2 = new PurchaseItem(); savedItem2.setId(203); savedItem2.setGame(game2); savedItem2.setQuantity(2); savedItem2.setPrice(30.00); savedItem2.setPurchase(savedPurchase);
        savedPurchase.setPurchaseLines(List.of(savedItem1, savedItem2));

        when(purchaseService.save(any(Purchase.class))).thenReturn(savedPurchase);

        mockMvc.perform(post("/api/purchases")
                        .with(user(clientUserDetails))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchaseInput)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(102))
                .andExpect(jsonPath("$.user.id").value(clientUser.getId()))
                .andExpect(jsonPath("$.paid").value(false))
                .andExpect(jsonPath("$.purchaseLines", hasSize(2)))
                .andExpect(jsonPath("$.purchaseLines[0].game.id").value(game1.getId()))
                .andExpect(jsonPath("$.purchaseLines[1].game.id").value(game2.getId()));
    }

    @Test
    @WithMockUser(username="admin", roles={"ADMIN"})
    void adminCanCreatePurchase() throws Exception {
        List<Map<String, Object>> itemsInput = List.of(Map.of("gameId", game1.getId(), "quantity", 1));
        Map<String, Object> purchaseInput = Map.of("items", itemsInput);

        Purchase savedPurchase = new Purchase();
        savedPurchase.setId(103);
        savedPurchase.setDate(LocalDate.now());
        savedPurchase.setUser(adminUser);

        when(purchaseService.save(any(Purchase.class))).thenReturn(savedPurchase);

        mockMvc.perform(post("/api/purchases")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchaseInput)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void unauthenticatedCannotCreatePurchase() throws Exception {
        List<Map<String, Object>> itemsInput = List.of(Map.of("gameId", game1.getId(), "quantity", 1));
        Map<String, Object> purchaseInput = Map.of("items", itemsInput);

        mockMvc.perform(post("/api/purchases")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchaseInput)))
                .andExpect(status().isForbidden());
    }

    @Test
    void clientCanGetOwnPurchases() throws Exception {
        Integer clientUserId = clientUser.getId();

        when(purchaseService.getByUserId(eq(clientUserId))).thenReturn(List.of(clientPurchase1));

        mockMvc.perform(get("/api/purchases")
                        .with(user(clientUserDetails))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(clientPurchase1.getId()))
                .andExpect(jsonPath("$[0].user.id").value(clientUserId));

        verify(purchaseService, times(1)).getByUserId(eq(clientUserId));
        verify(purchaseService, never()).getAll();
    }


    @Test
    @WithMockUser(username="admin", roles={"ADMIN"})
    void adminCanGetAllPurchases() throws Exception {

        when(purchaseService.getAll()).thenReturn(List.of(clientPurchase1, adminPurchase));

        mockMvc.perform(get("/api/purchases")
                        .with(user(adminUserDetails))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].user.id").value(clientUser.getId()))
                .andExpect(jsonPath("$[1].user.id").value(adminPurchase.getUser().getId()));
    }

    @Test
    void clientCanGetOwnPurchaseById() throws Exception {

        when(purchaseService.getById(eq(clientPurchase1.getId()))).thenReturn(clientPurchase1);

        mockMvc.perform(get("/api/purchases/" + clientPurchase1.getId())
                        .with(user(clientUserDetails))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clientPurchase1.getId()))
                .andExpect(jsonPath("$.user.id").value(clientUser.getId()));
    }

    @Test
    void clientCannotGetOthersPurchaseById() throws Exception {
        Integer otherPurchaseId = adminPurchase.getId();

        when(purchaseService.getById(eq(otherPurchaseId))).thenReturn(adminPurchase);

        mockMvc.perform(get("/api/purchases/" + otherPurchaseId)
                        .with(user(clientUserDetails))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(username="admin", roles={"ADMIN"})
    void adminCanGetAnyPurchaseById() throws Exception {
        Integer purchaseId = clientPurchase1.getId();
        when(purchaseService.getById(eq(purchaseId))).thenReturn(clientPurchase1);

        mockMvc.perform(get("/api/purchases/" + purchaseId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(purchaseId))
                .andExpect(jsonPath("$.user.id").value(clientUser.getId()));
    }

    @Test
    @WithMockUser(username="admin", roles={"ADMIN"})
    void adminCanUpdatePurchase() throws Exception {
        Integer purchaseIdToUpdate = clientPurchase1.getId();
        Map<String, Object> updates = Map.of(
                "paid", true,
                "delivered", true,
                "archived", false
        );

        Purchase updatedPurchase = new Purchase();
        updatedPurchase.setId(purchaseIdToUpdate);
        updatedPurchase.setUser(clientUser);
        updatedPurchase.setDate(clientPurchase1.getDate());
        updatedPurchase.setPaid(true);
        updatedPurchase.setDelivered(true);
        updatedPurchase.setArchived(false);
        updatedPurchase.setPurchaseLines(clientPurchase1.getPurchaseLines());

        when(purchaseService.getById(purchaseIdToUpdate)).thenReturn(clientPurchase1);
        when(purchaseService.update(eq(purchaseIdToUpdate), any(Purchase.class))).thenReturn(updatedPurchase);

        mockMvc.perform(put("/api/purchases/" + purchaseIdToUpdate)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.id").value(purchaseIdToUpdate))
                .andExpect(jsonPath("$.paid").value(true))
                .andExpect(jsonPath("$.delivered").value(true));
    }

    @Test
    void clientCannotUpdatePurchase() throws Exception {
        Integer purchaseIdToUpdate = clientPurchase1.getId();
        Map<String, Object> updates = Map.of("delivered", true);

        mockMvc.perform(put("/api/purchases/" + purchaseIdToUpdate)
                        .with(user(clientUserDetails))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(username="admin", roles={"ADMIN"})
    void adminCanDeletePurchase() throws Exception {
        Integer purchaseIdToDelete = clientPurchase1.getId();

        when(purchaseService.getById(purchaseIdToDelete)).thenReturn(clientPurchase1);
        doNothing().when(purchaseService).delete(purchaseIdToDelete);

        mockMvc.perform(delete("/api/purchases/" + purchaseIdToDelete)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(purchaseService, times(1)).delete(purchaseIdToDelete);
    }

    @Test
    void clientCannotDeletePurchase() throws Exception {
        Integer purchaseIdToDelete = clientPurchase1.getId();

        mockMvc.perform(delete("/api/purchases/" + purchaseIdToDelete)
                        .with(user(clientUserDetails))
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }
}
