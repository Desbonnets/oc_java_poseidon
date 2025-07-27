package org.oc.poseidon.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.oc.poseidon.domain.RuleName;
import org.oc.poseidon.repositories.RuleNameRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RuleNameServiceTest {

    private RuleNameRepository repo;
    private RuleNameService service;

    @BeforeEach
    void setUp() {
        repo = mock(RuleNameRepository.class);
        service = new RuleNameService(repo);
    }

    @Test
    @DisplayName("ruleNameAll retourne tous les éléments")
    void ruleNameAll_ShouldReturnAll() {
        when(repo.findAll()).thenReturn(List.of(new RuleName(), new RuleName()));

        List<RuleName> result = service.ruleNameAll();

        assertThat(result).hasSize(2);
        verify(repo).findAll();
    }

    @Test
    @DisplayName("validRuleName retourne true si au moins un champ est non-null")
    void validRuleName_ShouldReturnTrue() {
        RuleName rule = new RuleName();
        rule.setName("Test Rule");

        assertThat(service.validRuleName(rule)).isTrue();
    }

    @Test
    @DisplayName("validRuleName retourne false si tous les champs sont null")
    void validRuleName_ShouldReturnFalse() {
        RuleName rule = new RuleName();

        assertThat(service.validRuleName(rule)).isFalse();
    }

    @Test
    @DisplayName("addRuleName sauvegarde si valide")
    void addRuleName_ShouldSave_WhenValid() {
        RuleName rule = new RuleName();
        rule.setDescription("Some description");

        boolean result = service.addRuleName(rule);

        assertThat(result).isTrue();
        verify(repo).save(rule);
    }

    @Test
    @DisplayName("addRuleName ne sauvegarde pas si invalide")
    void addRuleName_ShouldNotSave_WhenInvalid() {
        RuleName rule = new RuleName(); // tous les champs null

        boolean result = service.addRuleName(rule);

        assertThat(result).isFalse();
        verifyNoInteractions(repo);
    }

    @Test
    @DisplayName("ruleNameById retourne l'objet attendu")
    void ruleNameById_ShouldReturnObject() {
        RuleName rule = new RuleName();
        when(repo.findById(1)).thenReturn(rule);

        RuleName result = service.ruleNameById(1);

        assertThat(result).isEqualTo(rule);
        verify(repo).findById(1);
    }

    @Test
    @DisplayName("updateRuleName met à jour les champs et sauvegarde")
    void updateRuleName_ShouldUpdateAndSave() {
        RuleName existing = new RuleName();
        existing.setName("Old");

        RuleName form = new RuleName();
        form.setName("New");
        form.setDescription("New desc");
        form.setJson("{...}");
        form.setTemplate("tpl");
        form.setSqlStr("SQL");
        form.setSqlPart("PART");

        when(repo.findById(1)).thenReturn(existing);

        boolean result = service.updateRuleName(form, 1);

        assertThat(result).isTrue();
        assertThat(existing.getName()).isEqualTo("New");
        assertThat(existing.getDescription()).isEqualTo("New desc");
        verify(repo).save(existing);
    }

    @Test
    @DisplayName("updateRuleName ne fait rien si invalide")
    void updateRuleName_ShouldNotUpdate_WhenInvalid() {
        RuleName existing = new RuleName();
        RuleName form = new RuleName(); // tous les champs null

        when(repo.findById(1)).thenReturn(existing);

        boolean result = service.updateRuleName(form, 1);

        assertThat(result).isFalse();
        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("deleteRuleName supprime le bon objet")
    void deleteRuleName_ShouldDelete() {
        RuleName rule = new RuleName();
        when(repo.findById(1)).thenReturn(rule);

        service.deleteRuleName(1);

        verify(repo).delete(rule);
    }
}
