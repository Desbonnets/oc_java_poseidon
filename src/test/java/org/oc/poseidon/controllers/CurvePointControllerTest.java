package org.oc.poseidon.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.oc.poseidon.domain.CurvePoint;
import org.oc.poseidon.service.CurvePointService;
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
    @DisplayName("GET /curvePoint/list - Affiche la liste des points de courbe avec succès")
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
    @DisplayName("GET /curvePoint/add - Affiche le formulaire d'ajout d'un CurvePoint")
    void testAddCurvePointForm() throws Exception {
        mockMvc.perform(get("/curvePoint/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"));
    }

    @Test
    @DisplayName("POST /curvePoint/validate - Courbe valide, redirige vers la liste")
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
    @DisplayName("POST /curvePoint/validate - Courbe invalide, renvoie le formulaire avec erreurs")
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
    @DisplayName("GET /curvePoint/update/1 - Affiche le formulaire de mise à jour d'une courbe")
    void testShowUpdateForm() throws Exception {
        mockMvc.perform(get("/curvePoint/update/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("curvePoint"))
                .andExpect(view().name("curvePoint/update"));
    }

    @Test
    @DisplayName("POST /curvePoint/update/1 - Mise à jour valide, redirige vers la liste")
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
    @DisplayName("POST /curvePoint/update/1 - Mise à jour invalide, renvoie le formulaire avec erreurs")
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
    @DisplayName("GET /curvePoint/delete/1 - Supprime une courbe et redirige vers la liste")
    void testDeleteCurvePoint() throws Exception {
        mockMvc.perform(get("/curvePoint/delete/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
    }
}
