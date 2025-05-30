package org.oc.poseidon.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.oc.poseidon.domain.BidList;
import org.oc.poseidon.service.BidListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BidListController.class)
@WithMockUser(username = "user", roles = {"USER"})
class BidListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BidListService bidListService;

    private BidList bid;

    @BeforeEach
    void setUp() {
        bid = new BidList("Account Test", "Type Test", 10.0);
        Mockito.when(bidListService.bidListById(1)).thenReturn(bid);
    }

    @Test
    void testHome() throws Exception {
        Mockito.when(bidListService.bidListAll()).thenReturn(Arrays.asList(bid));

        mockMvc.perform(get("/bidList/list").with(request -> {
                    request.setRemoteUser("testuser");
                    return request;
                }))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bidLists"))
                .andExpect(model().attributeExists("remoteUser"))
                .andExpect(view().name("bidList/list"));
    }

    @Test
    void testAddBidForm() throws Exception {
        mockMvc.perform(get("/bidList/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"));
    }

    @Test
    void testValidateBidValid() throws Exception {
        Mockito.when(bidListService.addBidList(any())).thenReturn(true);

        mockMvc.perform(post("/bidList/validate")
                        .with(csrf())
                        .param("account", "TestAccount")
                        .param("type", "TestType")
                        .param("bidQuantity", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }

    @Test
    void testValidateBidInvalid() throws Exception {
        mockMvc.perform(post("/bidList/validate")
                        .with(csrf())
                        .param("account", "")
                        .param("type", "TestType")
                        .param("bidQuantity", "-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"));
    }

    @Test
    void testShowUpdateForm() throws Exception {
        Mockito.when(bidListService.bidListById(1)).thenReturn(bid);

        mockMvc.perform(get("/bidList/update/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bidList"))
                .andExpect(view().name("bidList/update"));
    }

    @Test
    void testUpdateBidValid() throws Exception {
        Mockito.when(bidListService.updateBidList(any(), eq(1))).thenReturn(true);

        mockMvc.perform(post("/bidList/update/1")
                        .with(csrf())
                        .param("account", "UpdatedAccount")
                        .param("type", "UpdatedType")
                        .param("bidQuantity", "456.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }

    @Test
    void testUpdateBidInvalid() throws Exception {
        mockMvc.perform(post("/bidList/update/1")
                        .with(csrf())
                        .param("account", "")
                        .param("type", "")
                        .param("bidQuantity", "-1.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"));
    }

    @Test
    void testDeleteBid() throws Exception {
        mockMvc.perform(delete("/bidList/delete/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }
}
