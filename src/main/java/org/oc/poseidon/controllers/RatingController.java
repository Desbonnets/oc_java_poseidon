package org.oc.poseidon.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.oc.poseidon.domain.Rating;
import org.oc.poseidon.service.RatingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Contrôleur Spring MVC pour la gestion des objets {@link Rating}.
 * <p>
 * Accessible uniquement aux utilisateurs avec le rôle ADMIN grâce à l'annotation {@code @PreAuthorize}.
 * <p>
 * Gère les routes suivantes :
 * <ul>
 *     <li>GET /rating/list</li>
 *     <li>GET /rating/add</li>
 *     <li>POST /rating/validate</li>
 *     <li>GET /rating/update/{id}</li>
 *     <li>POST /rating/update/{id}</li>
 *     <li>GET /rating/delete/{id}</li>
 * </ul>
 */
@PreAuthorize("hasRole('ADMIN')")
@Controller
public class RatingController {

    private final RatingService ratingService;
    private static final String REDIRECT_RATING = "redirect:/rating/list";

    /**
     * Constructeur injectant le service {@link RatingService}.
     *
     * @param ratingService le service de gestion des objets Rating
     */
    public RatingController(final RatingService ratingService) {
        this.ratingService = ratingService;
    }

    /**
     * Affiche la liste des ratings.
     *
     * @param request la requête HTTP pour obtenir l'utilisateur connecté
     * @param model   le modèle utilisé pour passer les attributs à la vue
     * @return le nom de la vue "rating/list"
     */
    @RequestMapping("/rating/list")
    public String home(HttpServletRequest request, Model model) {
        model.addAttribute("remoteUser", request.getRemoteUser());
        model.addAttribute("ratings", ratingService.ratingAll());
        return "rating/list";
    }

    /**
     * Affiche le formulaire d'ajout d'un nouveau rating.
     *
     * @param rating un objet {@link Rating} vide injecté par Spring
     * @param model  le modèle utilisé pour passer les attributs à la vue
     * @return le nom de la vue "rating/add"
     */
    @GetMapping("/rating/add")
    public String addRatingForm(Rating rating, Model model) {
        model.addAttribute("rating", rating);
        return "rating/add";
    }

    /**
     * Valide et enregistre un rating soumis via le formulaire.
     *
     * @param rating l'objet {@link Rating} à valider
     * @param result les résultats de validation
     * @param model  le modèle utilisé pour afficher à nouveau le formulaire en cas d’erreur
     * @return redirection vers la liste si succès, sinon retourne la vue "rating/add"
     */
    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result, Model model) {
        if (!result.hasErrors() && ratingService.addRating(rating)) {
            return REDIRECT_RATING;
        }
        return "rating/add";
    }

    /**
     * Affiche le formulaire de mise à jour pour un rating existant.
     *
     * @param id    l'identifiant du rating à mettre à jour
     * @param model le modèle contenant l'objet rating
     * @return le nom de la vue "rating/update"
     */
    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Rating rating = ratingService.ratingById(id);
        model.addAttribute("rating", rating);
        return "rating/update";
    }

    /**
     * Met à jour un rating existant après validation.
     *
     * @param id     l'identifiant du rating
     * @param rating l'objet {@link Rating} mis à jour
     * @param result les erreurs de validation éventuelles
     * @param model  le modèle pour réafficher le formulaire en cas d'erreur
     * @return redirection vers la liste si succès, sinon retourne la vue "rating/update"
     */
    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                               BindingResult result, Model model) {
        if (!result.hasErrors() && ratingService.updateRating(rating, id)) {
            return REDIRECT_RATING;
        }
        return "rating/update";
    }

    /**
     * Supprime un rating existant.
     *
     * @param id    l'identifiant du rating à supprimer
     * @param model le modèle (non utilisé ici mais inclus pour cohérence)
     * @return redirection vers la liste des ratings
     */
    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        ratingService.deleteRating(id);
        return REDIRECT_RATING;
    }
}
