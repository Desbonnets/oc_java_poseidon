package org.oc.poseidon.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.oc.poseidon.domain.RuleName;
import org.oc.poseidon.service.RuleNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RuleNameController.class)
@WithMockUser(username = "user", roles = {"USER"})
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
    void testAddRuleNameForm() throws Exception {
        mockMvc.perform(get("/ruleName/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"));
    }

    @Test
    void testValidateRuleNameValid() throws Exception {
        Mockito.when(ruleNameService.addRuleName(any())).thenReturn(true);

        mockMvc.perform(post("/ruleName/validate")
                        .with(csrf())
                        .param("nameRuleName", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
    }

    @Test
    void testShowUpdateForm() throws Exception {
        Mockito.when(ruleNameService.ruleNameById(1)).thenReturn(ruleName);

        mockMvc.perform(get("/ruleName/update/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ruleName"))
                .andExpect(view().name("ruleName/update"));
    }

    @Test
    void testUpdateCRuleNameValid() throws Exception {
        Mockito.when(ruleNameService.updateRuleName(any(), eq(1))).thenReturn(true);

        mockMvc.perform(post("/ruleName/update/1")
                        .with(csrf())
                        .param("nameRuleName", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
    }

    @Test
    void testDeleteRuleName() throws Exception {
        mockMvc.perform(get("/ruleName/delete/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
    }
}
