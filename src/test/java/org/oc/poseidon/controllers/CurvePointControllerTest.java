package org.oc.poseidon.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.oc.poseidon.domain.CurvePoint;
import org.oc.poseidon.service.CurvePointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CurvePointController.class)
@WithMockUser(username = "user", roles = {"USER"})
class CurvePointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurvePointService curvePointService;

    private CurvePoint curvePoint;

    @BeforeEach
    void setUp() {
        curvePoint = new CurvePoint(1, 1.2, 10.0);
        Mockito.when(curvePointService.curvePointById(1)).thenReturn(curvePoint);
    }

    @Test
    void testHome() throws Exception {
        Mockito.when(curvePointService.curveAll()).thenReturn(Collections.singletonList(curvePoint));

        mockMvc.perform(get("/curvePoint/list").with(request -> {
                    request.setRemoteUser("testuser");
                    return request;
                }))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("curvePoints"))
                .andExpect(model().attributeExists("remoteUser"))
                .andExpect(view().name("curvePoint/list"));
    }

    @Test
    void testAddCurvePointForm() throws Exception {
        mockMvc.perform(get("/curvePoint/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"));
    }

    @Test
    void testValidateCurvePointValid() throws Exception {
        Mockito.when(curvePointService.addCurvePoint(any())).thenReturn(true);

        mockMvc.perform(post("/curvePoint/validate")
                        .with(csrf())
                        .param("curveId", "1")
                        .param("term", "1.2")
                        .param("value", "12.3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
    }

    @Test
    void testValidateCurvePointInvalid() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                        .with(csrf())
                        .param("curveId", "")
                        .param("term", "1.2")
                        .param("value", "invalid"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"));
    }

    @Test
    void testShowUpdateForm() throws Exception {
        Mockito.when(curvePointService.curvePointById(1)).thenReturn(curvePoint);

        mockMvc.perform(get("/curvePoint/update/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("curvePoint"))
                .andExpect(view().name("curvePoint/update"));
    }

    @Test
    void testUpdateCurvePointValid() throws Exception {
        Mockito.when(curvePointService.updateCurvePoint(any(), eq(1))).thenReturn(true);

        mockMvc.perform(post("/curvePoint/update/1")
                        .with(csrf())
                        .param("curveId", "1")
                        .param("term", "12")
                        .param("value", "456.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
    }

    @Test
    void testUpdateCurvePointInvalid() throws Exception {
        mockMvc.perform(post("/curvePoint/update/1")
                        .with(csrf())
                        .param("curveId", "")
                        .param("term", "")
                        .param("value", "po"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"));
    }

    @Test
    void testDeleteCurvePoint() throws Exception {
        mockMvc.perform(get("/curvePoint/delete/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
    }
}
