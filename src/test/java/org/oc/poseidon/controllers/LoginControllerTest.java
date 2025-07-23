package org.oc.poseidon.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.oc.poseidon.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.Collections.emptyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    @DisplayName("GET /login doit retourner la vue de login")
    void login_ShouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login/login"));
    }

    @Test
    @DisplayName("GET /secure/article-details avec ADMIN retourne la liste des utilisateurs")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllUserArticles_AsAdmin_ShouldReturnUserList() throws Exception {
        when(userRepository.findAll()).thenReturn(emptyList());

        mockMvc.perform(get("/secure/article-details"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    @DisplayName("GET /secure/article-details sans ADMIN retourne 403")
    @WithMockUser(username = "user", roles = {"USER"})
    void getAllUserArticles_AsUser_ShouldReturn403() throws Exception {
        mockMvc.perform(get("/secure/article-details"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /access-denied retourne la page 403 avec le message d'erreur")
    void error_ShouldReturn403ViewWithMessage() throws Exception {
        mockMvc.perform(get("/access-denied"))
                .andExpect(status().isOk())
                .andExpect(view().name("403"))
                .andExpect(model().attribute("errorMsg", "You are not authorized for the requested data."));
    }
}

