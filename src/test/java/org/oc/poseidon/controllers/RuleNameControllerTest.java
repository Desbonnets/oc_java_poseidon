package org.oc.poseidon.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.oc.poseidon.domain.RuleName;
import org.oc.poseidon.service.RuleNameService;
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
class RuleNameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RuleNameService ruleNameService;

    private RuleName ruleName;

    @BeforeEach
    void setUp() {
        ruleName = new RuleName(
                "name",
                "description",
                "json",
                "template",
                "sqlStr",
                "sqlPart"
        );
        Mockito.when(ruleNameService.ruleNameById(1)).thenReturn(ruleName);
    }

    @Test
    @DisplayName("GET /ruleName/list - Affiche la liste des règles")
    void testHome() throws Exception {
        Mockito.when(ruleNameService.ruleNameAll()).thenReturn(Collections.singletonList(ruleName));

        mockMvc.perform(get("/ruleName/list").with(request -> {
                    request.setRemoteUser("testuser");
                    return request;
                }))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ruleNames"))
                .andExpect(model().attributeExists("remoteUser"))
                .andExpect(view().name("ruleName/list"));
    }

    @Test
    @DisplayName("GET /ruleName/add - Affiche le formulaire d'ajout d'une règle")
    void testAddRuleNameForm() throws Exception {
        mockMvc.perform(get("/ruleName/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"));
    }

    @Test
    @DisplayName("POST /ruleName/validate - Valide une règle correcte, redirection attendue")
    void testValidateRuleNameValid() throws Exception {
        Mockito.when(ruleNameService.addRuleName(any())).thenReturn(true);

        mockMvc.perform(post("/ruleName/validate")
                        .with(csrf())
                        .param("nameRuleName", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
    }

    @Test
    @DisplayName("GET /ruleName/update/1 - Affiche le formulaire de mise à jour")
    void testShowUpdateForm() throws Exception {
        mockMvc.perform(get("/ruleName/update/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ruleName"))
                .andExpect(view().name("ruleName/update"));
    }

    @Test
    @DisplayName("POST /ruleName/update/1 - Met à jour une règle valide")
    void testUpdateCRuleNameValid() throws Exception {
        Mockito.when(ruleNameService.updateRuleName(any(), eq(1))).thenReturn(true);

        mockMvc.perform(post("/ruleName/update/1")
                        .with(csrf())
                        .param("nameRuleName", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
    }

    @Test
    @DisplayName("GET /ruleName/delete/1 - Supprime une règle et redirige vers la liste")
    void testDeleteRuleName() throws Exception {
        mockMvc.perform(get("/ruleName/delete/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
    }
}
