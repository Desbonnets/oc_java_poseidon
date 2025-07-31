package org.oc.poseidon.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.oc.poseidon.domain.Trade;
import org.oc.poseidon.service.TradeService;
import org.oc.poseidon.validation.FlexibleDoubleEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Contrôleur Spring MVC pour la gestion des entités {@link Trade}.
 * <p>
 * Toutes les actions de ce contrôleur nécessitent que l'utilisateur ayant le rôle {@code ADMIN}.
 * <p>
 * Gère les routes suivantes :
 * <ul>
 *     <li>GET /trade/list</li>
 *     <li>GET /trade/add</li>
 *     <li>POST /trade/validate</li>
 *     <li>GET /trade/update/{id}</li>
 *     <li>POST /trade/update/{id}</li>
 *     <li>GET /trade/delete/{id}</li>
 * </ul>
 */
@PreAuthorize("hasRole('ADMIN')")
@Controller
public class TradeController {

    private final TradeService tradeService;
    private static final String REDIRECT_TRADE = "redirect:/trade/list";

    /**
     * Constructeur avec injection du service {@link TradeService}.
     *
     * @param tradeService le service métier pour la gestion des transactions
     */
    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    /**
     * Affiche la liste de toutes les transactions.
     *
     * @param request la requête HTTP pour obtenir l'utilisateur connecté
     * @param model   le modèle utilisé pour transmettre les données à la vue
     * @return la vue "trade/list"
     */
    @RequestMapping("/trade/list")
    public String home(HttpServletRequest request, Model model) {
        model.addAttribute("remoteUser", request.getRemoteUser());
        model.addAttribute("trades", tradeService.tradeAll());
        return "trade/list";
    }

    /**
     * Affiche le formulaire d'ajout d'une nouvelle transaction.
     *
     * @param trade objet {@link Trade} vide injecté dans le modèle
     * @param model modèle pour la vue
     * @return la vue "trade/add"
     */
    @GetMapping("/trade/add")
    public String addUser(Trade trade, Model model) {
        model.addAttribute("trade", trade);
        return "trade/add";
    }

    /**
     * Valide et enregistre une transaction après soumission du formulaire.
     *
     * @param trade  la transaction à valider
     * @param result résultat de la validation
     * @return redirection vers la liste ou retour à la vue d'ajout en cas d'erreurs
     */
    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result) {
        if (!result.hasErrors() && tradeService.addTrade(trade)) {
            return REDIRECT_TRADE;
        }
        return "trade/add";
    }

    /**
     * Affiche le formulaire de mise à jour d'une transaction existante.
     *
     * @param id    identifiant de la transaction à modifier
     * @param model modèle contenant l'objet {@link Trade}
     * @return la vue "trade/update"
     */
    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Trade trade = tradeService.tradeById(id);
        model.addAttribute("trade", trade);
        return "trade/update";
    }

    /**
     * Met à jour une transaction existante après validation.
     *
     * @param id     identifiant de la transaction
     * @param trade  transaction mise à jour
     * @param result résultat de validation
     * @return redirection vers la liste ou retour à la vue de mise à jour
     */
    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade, BindingResult result) {
        if (!result.hasErrors() && tradeService.updateTrade(trade, id)) {
            return REDIRECT_TRADE;
        }
        return "trade/update";
    }

    /**
     * Supprime une transaction existante.
     *
     * @param id    identifiant de la transaction à supprimer
     * @param model modèle (non utilisé ici)
     * @return redirection vers la liste des transactions
     */
    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        tradeService.deleteTrade(id);
        return REDIRECT_TRADE;
    }

    /**
     * Permet de modifier une value de format double qu'elle soit accepter (12.5 et 12,5)
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Double.class, new FlexibleDoubleEditor());
    }
}
