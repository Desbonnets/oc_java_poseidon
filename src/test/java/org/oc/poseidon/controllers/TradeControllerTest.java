package org.oc.poseidon.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.oc.poseidon.domain.Trade;
import org.oc.poseidon.service.TradeService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "user", roles = {"ADMIN"})
class TradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeService tradeService;

    private Trade trade;

    @BeforeEach
    void setUp() {
        trade = new Trade("Trade Account", "Type");
        Mockito.when(tradeService.tradeById(1)).thenReturn(trade);
    }

    @Test
    void testHome() throws Exception {
        Mockito.when(tradeService.tradeAll()).thenReturn(Collections.singletonList(trade));

        mockMvc.perform(get("/trade/list").with(request -> {
                    request.setRemoteUser("testuser");
                    return request;
                }))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("trades"))
                .andExpect(model().attributeExists("remoteUser"))
                .andExpect(view().name("trade/list"));
    }

    @Test
    void testAddTradeForm() throws Exception {
        mockMvc.perform(get("/trade/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"));
    }

    @Test
    void testValidateTradeValid() throws Exception {
        Mockito.when(tradeService.addTrade(any())).thenReturn(true);

        mockMvc.perform(post("/trade/validate")
                        .with(csrf())
                        .param("account", "Trade Account")
                        .param("type", "Type")
                        .param("buyQuantity", "12"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
    }

    @Test
    void testValidateTradeInvalid() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .with(csrf())
                        .param("account", "Trade Account")
                        .param("type", "Type")
                        .param("buyQuantity", "test"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"));
    }

    @Test
    void testShowUpdateForm() throws Exception {
        Mockito.when(tradeService.tradeById(1)).thenReturn(trade);

        mockMvc.perform(get("/trade/update/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("trade"))
                .andExpect(view().name("trade/update"));
    }

    @Test
    void testUpdateCTradeValid() throws Exception {
        Mockito.when(tradeService.updateTrade(any(), eq(1))).thenReturn(true);

        mockMvc.perform(post("/trade/update/1")
                        .with(csrf())
                        .param("account", "Trade Account")
                        .param("type", "Type")
                        .param("buyQuantity", "12"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
    }

    @Test
    void testUpdateTradeInvalid() throws Exception {
        mockMvc.perform(post("/trade/update/1")
                        .with(csrf())
                        .param("account", "Trade Account")
                        .param("type", "Type")
                        .param("buyQuantity", "test"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"));
    }

    @Test
    void testDeleteTrade() throws Exception {
        mockMvc.perform(get("/trade/delete/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
    }
}
