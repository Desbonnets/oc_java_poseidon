package org.oc.poseidon.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.oc.poseidon.domain.BidList;
import org.oc.poseidon.repositories.BidListRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BidListServiceTest {

    private BidListRepository bidListRepository;
    private BidListService bidListService;

    @BeforeEach
    void setup() {
        bidListRepository = mock(BidListRepository.class);
        bidListService = new BidListService(bidListRepository);
    }

    @Test
    @DisplayName("bidListAll retourne tous les éléments")
    void bidListAll_ShouldReturnAllBids() {
        BidList b1 = new BidList();
        BidList b2 = new BidList();
        when(bidListRepository.findAll()).thenReturn(List.of(b1, b2));

        List<BidList> result = bidListService.bidListAll();

        assertThat(result).hasSize(2);
        verify(bidListRepository).findAll();
    }

    @Test
    @DisplayName("validBidList retourne true pour un bid valide")
    void validBidList_ShouldReturnTrue() {
        BidList bid = new BidList();
        bid.setAccount("Account1");
        bid.setType("Type1");
        bid.setBidQuantity(10.0);

        boolean result = bidListService.validBidList(bid);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("validBidList retourne false si account/type manquant ou vide")
    void validBidList_ShouldReturnFalse_WhenInvalid() {
        BidList bid = new BidList(); // tous les champs null

        boolean result = bidListService.validBidList(bid);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("addBidList sauvegarde si bid est valide")
    void addBidList_ShouldSave_WhenValid() {
        BidList bid = new BidList();
        bid.setAccount("Account1");
        bid.setType("Type1");
        bid.setBidQuantity(10.0);

        boolean result = bidListService.addBidList(bid);

        assertThat(result).isTrue();
        verify(bidListRepository).save(bid);
    }

    @Test
    @DisplayName("addBidList ne sauvegarde pas si bid est invalide")
    void addBidList_ShouldNotSave_WhenInvalid() {
        BidList bid = new BidList(); // champs vides

        boolean result = bidListService.addBidList(bid);

        assertThat(result).isFalse();
        verifyNoInteractions(bidListRepository);
    }

    @Test
    @DisplayName("bidListById retourne le bid avec l'id donné")
    void bidListById_ShouldReturnBid() {
        BidList bid = new BidList();
        when(bidListRepository.findByBidListId(1)).thenReturn(bid);

        BidList result = bidListService.bidListById(1);

        assertThat(result).isEqualTo(bid);
        verify(bidListRepository).findByBidListId(1);
    }

    @Test
    @DisplayName("updateBidList modifie et sauvegarde un bid existant")
    void updateBidList_ShouldUpdateAndSave() {
        BidList existing = new BidList();
        existing.setAccount("Old");
        existing.setType("Old");
        existing.setBidQuantity(1.0);

        BidList form = new BidList();
        form.setAccount("New");
        form.setType("New");
        form.setBidQuantity(99.0);

        when(bidListRepository.findByBidListId(1)).thenReturn(existing);

        boolean result = bidListService.updateBidList(form, 1);

        assertThat(result).isTrue();
        assertThat(existing.getAccount()).isEqualTo("New");
        verify(bidListRepository).save(existing);
    }

    @Test
    @DisplayName("updateBidList ne fait rien si form invalide")
    void updateBidList_ShouldNotSave_WhenInvalid() {
        BidList existing = new BidList();
        BidList form = new BidList(); // invalide

        when(bidListRepository.findByBidListId(1)).thenReturn(existing);

        boolean result = bidListService.updateBidList(form, 1);

        assertThat(result).isFalse();
        verify(bidListRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteBidList supprime un bid existant")
    void deleteBidList_ShouldDelete() {
        BidList bid = new BidList();
        when(bidListRepository.findByBidListId(1)).thenReturn(bid);

        bidListService.deleteBidList(1);

        verify(bidListRepository).delete(bid);
    }
}
