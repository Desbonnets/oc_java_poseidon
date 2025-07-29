package org.oc.poseidon.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET / - Retourne la vue 'home' pour un utilisateur non authentifié")
    void home_ShouldReturnHomeView() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    @Test
    @DisplayName("GET /admin/home - Redirige vers /bidList/list pour un utilisateur avec rôle ADMIN")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminHome_WithAdminRole_ShouldRedirect() throws Exception {
        mockMvc.perform(get("/admin/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }

    @Test
    @DisplayName("GET /user/home - Affiche la vue 'userHome' pour un utilisateur avec rôle USER")
    @WithMockUser(username = "user", roles = {"USER"})
    void userHome_WithUserRole_ShouldReturnUserHome() throws Exception {
        mockMvc.perform(get("/user/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("userHome"))
                .andExpect(model().attribute("remoteUser", "user"));
    }

    @Test
    @DisplayName("GET /admin/home - Renvoie une erreur 403 pour un utilisateur avec rôle USER")
    @WithMockUser(username = "user", roles = {"USER"})
    void adminHome_WithUserRole_ShouldReturn403() throws Exception {
        mockMvc.perform(get("/admin/home"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /user/home - Redirige vers /login si l'utilisateur n'est pas authentifié")
    void userHome_WithoutAuth_ShouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/user/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}
