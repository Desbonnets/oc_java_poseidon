package org.oc.poseidon.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.oc.poseidon.domain.BidList;
import org.oc.poseidon.service.BidListService;
import org.oc.poseidon.validation.FlexibleDoubleEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Contrôleur Spring MVC pour gérer les opérations CRUD sur l'entité {@link BidList}.
 * <p>
 * Accessible uniquement aux utilisateurs avec le rôle ADMIN grâce à {@code @PreAuthorize("hasRole('ADMIN')")}.
 * <p>
 * Les vues associées sont :
 * <ul>
 *     <li>GET /bidList/list</li>
 *     <li>GET /bidList/add</li>
 *     <li>POST /bidList/validate</li>
 *     <li>GET /bidList/update/{id}</li>
 *     <li>POST /bidList/update/{id}</li>
 *     <li>GET /bidList/delete/{id}</li>
 * </ul>
 */
@PreAuthorize("hasRole('ADMIN')")
@Controller
public class BidListController {

    private final BidListService bidListService;
    private static final String REDIRECT_BIDLIST = "redirect:/bidList/list";

    /**
     * Constructeur avec injection du service métier.
     *
     * @param bidListService service pour gérer les bidLists
     */
    public BidListController(BidListService bidListService) {
        this.bidListService = bidListService;
    }

    /**
     * Affiche la liste des {@link BidList}.
     *
     * @param request la requête HTTP (utilisée pour obtenir l'utilisateur connecté)
     * @param model   le modèle envoyé à la vue
     * @return la vue "bidList/list"
     */
    @RequestMapping("/bidList/list")
    public String home(HttpServletRequest request, Model model) {
        model.addAttribute("remoteUser", request.getRemoteUser());
        model.addAttribute("bidLists", bidListService.bidListAll());
        return "bidList/list";
    }

    /**
     * Affiche le formulaire d’ajout d’un nouveau {@link BidList}.
     *
     * @param bid   objet vide à lier au formulaire
     * @param model le modèle envoyé à la vue
     * @return la vue "bidList/add"
     */
    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid, Model model) {
        model.addAttribute("bidList", bid);
        return "bidList/add";
    }

    /**
     * Valide et enregistre un nouveau {@link BidList}.
     *
     * @param bid    le bidList à valider
     * @param result résultat de la validation
     * @return redirection vers la liste si succès, sinon vue "bidList/add"
     */
    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result) {
        if (!result.hasErrors() && bidListService.addBidList(bid)) {
            return REDIRECT_BIDLIST;
        }
        return "bidList/add";
    }

    /**
     * Affiche le formulaire de modification d’un {@link BidList}.
     *
     * @param id    l’identifiant du bid à modifier
     * @param model le modèle envoyé à la vue
     * @return la vue "bidList/update"
     */
    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        BidList bid = bidListService.bidListById(id);
        model.addAttribute("bidList", bid);
        return "bidList/update";
    }

    /**
     * Traite la soumission du formulaire de modification.
     *
     * @param id       l’identifiant du bid à modifier
     * @param bidList  l’objet bidList modifié
     * @param result   résultat de la validation
     * @return redirection vers la liste si succès, sinon vue "bidList/update"
     */
    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList, BindingResult result) {
        if (!result.hasErrors() && bidListService.updateBidList(bidList, id)) {
            return REDIRECT_BIDLIST;
        }
        return "bidList/update";
    }

    /**
     * Supprime un {@link BidList} par son ID.
     *
     * @param id identifiant du bid à supprimer
     * @return redirection vers la liste après suppression
     */
    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id) {
        bidListService.deleteBidList(id);
        return REDIRECT_BIDLIST;
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
