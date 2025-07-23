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
    @DisplayName("GET / doit retourner la vue 'home'")
    void home_ShouldReturnHomeView() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    @Test
    @DisplayName("GET /admin/home avec rôle ADMIN redirige vers /bidList/list")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminHome_WithAdminRole_ShouldRedirect() throws Exception {
        mockMvc.perform(get("/admin/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }

    @Test
    @DisplayName("GET /user/home avec rôle USER affiche la vue 'userHome'")
    @WithMockUser(username = "user", roles = {"USER"})
    void userHome_WithUserRole_ShouldReturnUserHome() throws Exception {
        mockMvc.perform(get("/user/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("userHome"))
                .andExpect(model().attribute("remoteUser", "user"));
    }

    @Test
    @DisplayName("GET /admin/home sans rôle adéquat renvoie 403")
    @WithMockUser(username = "user", roles = {"USER"})
    void adminHome_WithUserRole_ShouldReturn403() throws Exception {
        mockMvc.perform(get("/admin/home"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /user/home sans authentification renvoie sur login")
    void userHome_WithoutAuth_ShouldReturn403() throws Exception {
        mockMvc.perform(get("/user/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}
