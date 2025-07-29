package org.oc.poseidon.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.oc.poseidon.domain.Rating;
import org.oc.poseidon.repositories.RatingRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RatingServiceTest {

    private RatingRepository repo;
    private RatingService service;

    @BeforeEach
    void setUp() {
        repo = mock(RatingRepository.class);
        service = new RatingService(repo);
    }

    @Test
    @DisplayName("ratingAll retourne tous les éléments")
    void ratingAll_ShouldReturnAll() {
        when(repo.findAll()).thenReturn(List.of(new Rating(), new Rating()));

        List<Rating> result = service.ratingAll();

        assertThat(result).hasSize(2);
        verify(repo).findAll();
    }

    @Test
    @DisplayName("validRating retourne true si au moins un champ est rempli")
    void validRating_ShouldReturnTrue() {
        Rating rating = new Rating();
        rating.setFitchRating("Fitch A");

        boolean result = service.validRating(rating);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("validRating retourne false si tout est null")
    void validRating_ShouldReturnFalse() {
        Rating rating = new Rating();

        boolean result = service.validRating(rating);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("addRating sauvegarde si rating valide")
    void addRating_ShouldSave_WhenValid() {
        Rating rating = new Rating();
        rating.setMoodysRating("Moody A");

        boolean result = service.addRating(rating);

        assertThat(result).isTrue();
        verify(repo).save(rating);
    }

    @Test
    @DisplayName("addRating ne sauvegarde pas si invalide")
    void addRating_ShouldNotSave_WhenInvalid() {
        Rating rating = new Rating(); // tous les champs null

        boolean result = service.addRating(rating);

        assertThat(result).isFalse();
        verifyNoInteractions(repo);
    }

    @Test
    @DisplayName("ratingById retourne le rating")
    void ratingById_ShouldReturnRating() {
        Rating rating = new Rating();
        when(repo.findById(1)).thenReturn(rating);

        Rating result = service.ratingById(1);

        assertThat(result).isEqualTo(rating);
        verify(repo).findById(1);
    }

    @Test
    @DisplayName("updateRating modifie et sauvegarde un rating existant")
    void updateRating_ShouldUpdateAndSave() {
        Rating existing = new Rating();
        existing.setMoodysRating("Old");

        Rating form = new Rating();
        form.setMoodysRating("New");
        form.setFitchRating("Fitch A");
        form.setSandPRating("S&P B");
        form.setOrderNumber(5);

        when(repo.findById(1)).thenReturn(existing);

        boolean result = service.updateRating(form, 1);

        assertThat(result).isTrue();
        assertThat(existing.getMoodysRating()).isEqualTo("New");
        assertThat(existing.getFitchRating()).isEqualTo("Fitch A");
        verify(repo).save(existing);
    }

    @Test
    @DisplayName("updateRating ne fait rien si rating invalide")
    void updateRating_ShouldNotUpdate_WhenInvalid() {
        Rating existing = new Rating();
        Rating form = new Rating(); // tous les champs null

        when(repo.findById(1)).thenReturn(existing);

        boolean result = service.updateRating(form, 1);

        assertThat(result).isFalse();
        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("deleteRating supprime un rating existant")
    void deleteRating_ShouldDelete() {
        Rating rating = new Rating();
        when(repo.findById(1)).thenReturn(rating);

        service.deleteRating(1);

        verify(repo).delete(rating);
    }
}
