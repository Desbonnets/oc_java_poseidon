package org.oc.poseidon.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.oc.poseidon.domain.Trade;
import org.oc.poseidon.repositories.TradeRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TradeServiceTest {

    private TradeRepository repo;
    private TradeService service;

    @BeforeEach
    void setUp() {
        repo = mock(TradeRepository.class);
        service = new TradeService(repo);
    }

    @Test
    @DisplayName("tradeAll retourne tous les éléments")
    void tradeAll_ShouldReturnAll() {
        when(repo.findAll()).thenReturn(List.of(new Trade(), new Trade()));

        List<Trade> result = service.tradeAll();

        assertThat(result).hasSize(2);
        verify(repo).findAll();
    }

    @Test
    @DisplayName("validTrade retourne true si au moins un champ est non-null")
    void validTrade_ShouldReturnTrue() {
        Trade trade = new Trade();
        trade.setAccount("Account");

        assertThat(service.validTrade(trade)).isTrue();
    }

    @Test
    @DisplayName("validTrade retourne false si tous les champs sont null")
    void validTrade_ShouldReturnFalse() {
        Trade trade = new Trade();

        assertThat(service.validTrade(trade)).isFalse();
    }

    @Test
    @DisplayName("addTrade sauvegarde si trade valide")
    void addTrade_ShouldSave_WhenValid() {
        Trade trade = new Trade();
        trade.setType("Type");

        boolean result = service.addTrade(trade);

        assertThat(result).isTrue();
        verify(repo).save(trade);
    }

    @Test
    @DisplayName("addTrade ne sauvegarde pas si trade invalide")
    void addTrade_ShouldNotSave_WhenInvalid() {
        Trade trade = new Trade(); // tout est null

        boolean result = service.addTrade(trade);

        assertThat(result).isFalse();
        verifyNoInteractions(repo);
    }

    @Test
    @DisplayName("tradeById retourne le trade")
    void tradeById_ShouldReturnTrade() {
        Trade trade = new Trade();
        when(repo.findByTradeId(1)).thenReturn(trade);

        Trade result = service.tradeById(1);

        assertThat(result).isEqualTo(trade);
        verify(repo).findByTradeId(1);
    }

    @Test
    @DisplayName("updateTrade met à jour et sauvegarde un trade valide")
    void updateTrade_ShouldUpdateAndSave_WhenValid() {
        Trade existing = new Trade();
        existing.setAccount("Old");

        Trade form = new Trade();
        form.setAccount("New");
        form.setType("Type");
        form.setBuyQuantity(100.0);

        when(repo.findByTradeId(1)).thenReturn(existing);

        boolean result = service.updateTrade(form, 1);

        assertThat(result).isTrue();
        assertThat(existing.getAccount()).isEqualTo("New");
        assertThat(existing.getBuyQuantity()).isEqualTo(100.0);
        verify(repo).save(existing);
    }

    @Test
    @DisplayName("updateTrade ne fait rien si invalide")
    void updateTrade_ShouldNotUpdate_WhenInvalid() {
        Trade existing = new Trade();
        Trade form = new Trade(); // tout est null

        when(repo.findByTradeId(1)).thenReturn(existing);

        boolean result = service.updateTrade(form, 1);

        assertThat(result).isFalse();
        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("deleteTrade supprime le trade par ID")
    void deleteTrade_ShouldDelete() {
        Trade trade = new Trade();
        when(repo.findByTradeId(1)).thenReturn(trade);

        service.deleteTrade(1);

        verify(repo).delete(trade);
    }
}
