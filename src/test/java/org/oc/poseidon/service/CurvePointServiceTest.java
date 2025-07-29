package org.oc.poseidon.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.oc.poseidon.domain.CurvePoint;
import org.oc.poseidon.repositories.CurvePointRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CurvePointServiceTest {

    private CurvePointRepository repo;
    private CurvePointService service;

    @BeforeEach
    void setUp() {
        repo = mock(CurvePointRepository.class);
        service = new CurvePointService(repo);
    }

    @Test
    @DisplayName("curveAll() retourne la liste complète")
    void curveAll_ShouldReturnAll() {
        when(repo.findAll()).thenReturn(List.of(new CurvePoint(), new CurvePoint()));

        List<CurvePoint> result = service.curveAll();

        assertThat(result).hasSize(2);
        verify(repo).findAll();
    }

    @Test
    @DisplayName("validCurvePoint retourne true pour un objet valide")
    void validCurvePoint_ShouldReturnTrue() {
        CurvePoint curve = new CurvePoint();
        curve.setCurveId(1);
        curve.setTerm(5.0);
        curve.setValue(10.0);

        assertThat(service.validCurvePoint(curve)).isTrue();
    }

    @Test
    @DisplayName("validCurvePoint retourne false si curveId est null")
    void validCurvePoint_ShouldReturnFalse_WhenCurveIdNull() {
        CurvePoint curve = new CurvePoint();
        curve.setTerm(1.0);
        curve.setValue(2.0);

        assertThat(service.validCurvePoint(curve)).isFalse();
    }

    @Test
    @DisplayName("addCurvePoint sauvegarde si valide")
    void addCurvePoint_ShouldSave_WhenValid() {
        CurvePoint curve = new CurvePoint();
        curve.setCurveId(1);
        curve.setTerm(1.0);
        curve.setValue(2.0);

        boolean result = service.addCurvePoint(curve);

        assertThat(result).isTrue();
        verify(repo).save(curve);
    }

    @Test
    @DisplayName("addCurvePoint ne sauvegarde pas si invalide")
    void addCurvePoint_ShouldNotSave_WhenInvalid() {
        CurvePoint curve = new CurvePoint(); // tous les champs null

        boolean result = service.addCurvePoint(curve);

        assertThat(result).isFalse();
        verifyNoInteractions(repo);
    }

    @Test
    @DisplayName("curvePointById retourne bien le CurvePoint")
    void curvePointById_ShouldReturnCurvePoint() {
        CurvePoint c = new CurvePoint();
        when(repo.findById(1)).thenReturn(c);

        CurvePoint result = service.curvePointById(1);

        assertThat(result).isEqualTo(c);
        verify(repo).findById(1);
    }

    @Test
    @DisplayName("updateCurvePoint modifie les champs et sauvegarde")
    void updateCurvePoint_ShouldUpdateAndSave() {
        CurvePoint existing = new CurvePoint();
        existing.setCurveId(1);
        existing.setTerm(1.0);
        existing.setValue(1.0);

        CurvePoint form = new CurvePoint();
        form.setCurveId(2);
        form.setTerm(5.5);
        form.setValue(10.0);

        when(repo.findById(1)).thenReturn(existing);

        boolean result = service.updateCurvePoint(form, 1);

        assertThat(result).isTrue();
        assertThat(existing.getCurveId()).isEqualTo(2);
        assertThat(existing.getTerm()).isEqualTo(5.5);
        assertThat(existing.getValue()).isEqualTo(10.0);
        verify(repo).save(existing);
    }

    @Test
    @DisplayName("updateCurvePoint ne fait rien si invalide")
    void updateCurvePoint_ShouldNotUpdate_WhenInvalid() {
        CurvePoint existing = new CurvePoint();
        CurvePoint form = new CurvePoint(); // invalide : curveId null

        when(repo.findById(1)).thenReturn(existing);

        boolean result = service.updateCurvePoint(form, 1);

        assertThat(result).isFalse();
        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("deleteCurvePoint appelle bien le repo")
    void deleteCurvePoint_ShouldDelete() {
        CurvePoint c = new CurvePoint();
        when(repo.findById(1)).thenReturn(c);

        service.deleteCurvePoint(1);

        verify(repo).delete(c);
    }
}
