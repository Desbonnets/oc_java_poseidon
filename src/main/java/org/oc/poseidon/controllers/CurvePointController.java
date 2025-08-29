package org.oc.poseidon.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.oc.poseidon.domain.CurvePoint;
import org.oc.poseidon.service.CurvePointService;
import org.oc.poseidon.validation.FlexibleDoubleEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Contrôleur Spring MVC pour gérer les opérations CRUD sur l'entité {@link CurvePoint}.
 * <p>
 * Accessible uniquement aux utilisateurs ayant le rôle {@code ADMIN}.
 * <p>
 * Gère les routes suivantes :
 * <ul>
 *     <li>GET /curvePoint/list</li>
 *     <li>GET /curvePoint/add</li>
 *     <li>POST /curvePoint/validate</li>
 *     <li>GET /curvePoint/update/{id}</li>
 *     <li>POST /curvePoint/update/{id}</li>
 *     <li>GET /curvePoint/delete/{id}</li>
 * </ul>
 */
@PreAuthorize("hasRole('ADMIN')")
@Controller
public class CurvePointController {

    private final CurvePointService curvePointService;
    private static final String REDIRECT_CURVEPOINTLIST = "redirect:/curvePoint/list";

    /**
     * Constructeur injectant le service {@link CurvePointService}.
     *
     * @param curvePointService service métier pour les CurvePoint
     */
    public CurvePointController(CurvePointService curvePointService) {
        this.curvePointService = curvePointService;
    }

    /**
     * Affiche la liste des {@link CurvePoint}.
     *
     * @param request la requête HTTP (pour obtenir l'utilisateur connecté)
     * @param model   modèle de données pour la vue
     * @return la vue "curvePoint/list"
     */
    @RequestMapping("/curvePoint/list")
    public String home(HttpServletRequest request, Model model) {
        model.addAttribute("remoteUser", request.getRemoteUser());
        model.addAttribute("curvePoints", curvePointService.curveAll());
        return "curvePoint/list";
    }

    /**
     * Affiche le formulaire d’ajout d’un nouveau {@link CurvePoint}.
     *
     * @param curvePoint instance vide liée au formulaire
     * @param model      modèle de données pour la vue
     * @return la vue "curvePoint/add"
     */
    @GetMapping("/curvePoint/add")
    public String addCurvePointForm(CurvePoint curvePoint, Model model) {
        model.addAttribute("curvePoint", curvePoint);
        return "curvePoint/add";
    }

    /**
     * Valide et enregistre un nouveau {@link CurvePoint}.
     *
     * @param curvePoint l'objet à valider
     * @param result     résultat de la validation
     * @return redirection vers la liste si succès, sinon vue "curvePoint/add"
     */
    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result) {
        if (!result.hasErrors() && curvePointService.addCurvePoint(curvePoint)) {
            return REDIRECT_CURVEPOINTLIST;
        }
        return "curvePoint/add";
    }

    /**
     * Affiche le formulaire de mise à jour pour un {@link CurvePoint}.
     *
     * @param id    identifiant du point à modifier
     * @param model modèle de données pour la vue
     * @return la vue "curvePoint/update"
     */
    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        CurvePoint curvePoint = curvePointService.curvePointById(id);
        model.addAttribute("curvePoint", curvePoint);
        return "curvePoint/update";
    }

    /**
     * Traite la soumission du formulaire de mise à jour d’un {@link CurvePoint}.
     *
     * @param id          identifiant du point à modifier
     * @param curvePoint  l'objet mis à jour
     * @param result      résultat de la validation
     * @param model       modèle de données pour la vue
     * @return redirection vers la liste si succès, sinon vue "curvePoint/update"
     */
    @PostMapping("/curvePoint/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                            BindingResult result, Model model) {
        if (!result.hasErrors() && curvePointService.updateCurvePoint(curvePoint, id)) {
            return REDIRECT_CURVEPOINTLIST;
        }
        return "curvePoint/update";
    }

    /**
     * Supprime un {@link CurvePoint} par son identifiant.
     *
     * @param id identifiant du point à supprimer
     * @return redirection vers la liste
     */
    @GetMapping("/curvePoint/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id) {
        curvePointService.deleteCurvePoint(id);
        return REDIRECT_CURVEPOINTLIST;
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
