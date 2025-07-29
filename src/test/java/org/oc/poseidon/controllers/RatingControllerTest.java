package org.oc.poseidon.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.oc.poseidon.domain.Rating;
import org.oc.poseidon.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "user", roles = {"ADMIN"})
class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatingService ratingService;

    private Rating rating;

    @BeforeEach
    void setUp() {
        rating = new Rating("12", "14", "40", 1);
        Mockito.when(ratingService.ratingById(1)).thenReturn(rating);
    }

    @Test
    @DisplayName("GET /rating/list - Affiche la liste des notations")
    void testHome() throws Exception {
        Mockito.when(ratingService.ratingAll()).thenReturn(Collections.singletonList(rating));

        mockMvc.perform(get("/rating/list").with(request -> {
                    request.setRemoteUser("testuser");
                    return request;
                }))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ratings"))
                .andExpect(model().attributeExists("remoteUser"))
                .andExpect(view().name("rating/list"));
    }

    @Test
    @DisplayName("GET /rating/add - Affiche le formulaire d'ajout de notation")
    void testAddRatingForm() throws Exception {
        mockMvc.perform(get("/rating/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"));
    }

    @Test
    @DisplayName("POST /rating/validate - Validation d'une notation valide, redirection vers la liste")
    void testValidateRatingValid() throws Exception {
        Mockito.when(ratingService.addRating(any())).thenReturn(true);

        mockMvc.perform(post("/rating/validate")
                        .with(csrf())
                        .param("moodysRating", "1")
                        .param("SandPRating", "1")
                        .param("fitchRating", "12")
                        .param("orderNumber", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
    }

    @Test
    @DisplayName("POST /rating/validate - Soumission invalide, retour au formulaire d'ajout")
    void testValidateRatingInvalid() throws Exception {
        mockMvc.perform(post("/rating/validate")
                        .with(csrf())
                        .param("moodysRating", "")
                        .param("SandPRating", "")
                        .param("fitchRating", "")
                        .param("orderNumber", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"));
    }

    @Test
    @DisplayName("GET /rating/update/1 - Affiche le formulaire de mise à jour")
    void testShowUpdateForm() throws Exception {
        mockMvc.perform(get("/rating/update/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("rating"))
                .andExpect(view().name("rating/update"));
    }

    @Test
    @DisplayName("POST /rating/update/1 - Mise à jour valide d'une notation, redirection vers la liste")
    void testUpdateCRatingValid() throws Exception {
        Mockito.when(ratingService.updateRating(any(), eq(1))).thenReturn(true);

        mockMvc.perform(post("/rating/update/1")
                        .with(csrf())
                        .param("moodysRating", "1")
                        .param("SandPRating", "12")
                        .param("fitchRating", "456")
                        .param("orderNumber", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
    }

    @Test
    @DisplayName("POST /rating/update/1 - Mise à jour invalide, retour au formulaire")
    void testUpdateRatingInvalid() throws Exception {
        mockMvc.perform(post("/rating/update/1")
                        .with(csrf())
                        .param("moodysRating", "")
                        .param("SandPRating", "")
                        .param("fitchRating", "")
                        .param("orderNumber", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"));
    }

    @Test
    @DisplayName("GET /rating/delete/1 - Supprime une notation et redirige vers la liste")
    void testDeleteRating() throws Exception {
        mockMvc.perform(get("/rating/delete/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
    }
}
