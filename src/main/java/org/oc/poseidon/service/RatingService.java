package org.oc.poseidon.service;

import org.oc.poseidon.domain.Rating;
import org.oc.poseidon.repositories.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service de gestion des entités {@link Rating}.
 * Fournit les opérations métier liées aux notations (ratings),
 * telles que l'ajout, la mise à jour, la suppression et la validation.
 */
@Service
public class RatingService {

    private final RatingRepository repo;

    /**
     * Constructeur avec injection du dépôt {@link RatingRepository}.
     *
     * @param repo le dépôt utilisé pour accéder aux données de Rating
     */
    public RatingService(RatingRepository repo) {
        this.repo = repo;
    }

    /**
     * Récupère toutes les notations enregistrées.
     *
     * @return liste de tous les objets {@link Rating}
     */
    public List<Rating> ratingAll() {
        return repo.findAll();
    }

    /**
     * Vérifie si une entité {@link Rating} est valide.
     * Au moins un champ (fitch, Sand, Moody ou ordre) doit être non nul.
     *
     * @param rating l'entité à valider
     * @return true si l'entité est considérée comme valide, false sinon
     */
    public boolean validRating(Rating rating) {
        return (rating.getFitchRating() != null ||
                rating.getSandPRating() != null ||
                rating.getMoodysRating() != null ||
                rating.getOrderNumber() != null);
    }

    /**
     * Ajoute une entité {@link Rating} si elle est valide.
     *
     * @param rating l'entité à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addRating(Rating rating) {
        boolean result = false;

        if (validRating(rating)) {
            repo.save(rating);
            result = true;
        }

        return result;
    }

    /**
     * Récupère une entité {@link Rating} par son identifiant.
     *
     * @param id l'identifiant du rating
     * @return l'entité {@link Rating} correspondante
     */
    public Rating ratingById(int id) {
        return repo.findById(id);
    }

    /**
     * Met à jour une entité {@link Rating} existante avec les nouvelles données.
     *
     * @param formRating les nouvelles données
     * @param id         l'identifiant de l'entité à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateRating(Rating formRating, int id) {
        boolean result = false;

        Rating rating = repo.findById(id);

        if (validRating(formRating)) {
            rating.setFitchRating(formRating.getFitchRating());
            rating.setMoodysRating(formRating.getMoodysRating());
            rating.setSandPRating(formRating.getSandPRating());
            rating.setOrderNumber(formRating.getOrderNumber());

            repo.save(rating);

            result = true;
        }

        return result;
    }

    /**
     * Supprime une entité {@link Rating} par son identifiant.
     *
     * @param id l'identifiant du rating à supprimer
     */
    public void deleteRating(int id) {
        Rating rating = ratingById(id);
        repo.delete(rating);
    }
}
