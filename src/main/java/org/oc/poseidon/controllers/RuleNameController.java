package org.oc.poseidon.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.oc.poseidon.domain.RuleName;
import org.oc.poseidon.service.RuleNameService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

/**
 * Contrôleur Spring MVC pour la gestion des entités {@link RuleName}.
 * <p>
 * Toutes les actions de ce contrôleur nécessitent que l'utilisateur ayant le rôle {@code ADMIN}.
 * <p>
 * Gère les routes suivantes :
 * <ul>
 *     <li>GET /ruleName/list</li>
 *     <li>GET /ruleName/add</li>
 *     <li>POST /ruleName/validate</li>
 *     <li>GET /ruleName/update/{id}</li>
 *     <li>POST /ruleName/update/{id}</li>
 *     <li>GET /ruleName/delete/{id}</li>
 * </ul>
 */
@PreAuthorize("hasRole('ADMIN')")
@Controller
public class RuleNameController {

    private final RuleNameService ruleNameService;
    private static final String REDIRECT_RULE_NAME = "redirect:/ruleName/list";

    /**
     * Constructeur avec injection du service {@link RuleNameService}.
     *
     * @param ruleNameService le service de gestion des RuleName
     */
    public RuleNameController(RuleNameService ruleNameService) {
        this.ruleNameService = ruleNameService;
    }

    /**
     * Affiche la liste des règles (RuleName).
     *
     * @param request la requête HTTP permettant de récupérer l'utilisateur connecté
     * @param model   le modèle utilisé pour passer les données à la vue
     * @return le nom de la vue "ruleName/list"
     */
    @RequestMapping("/ruleName/list")
    public String home(HttpServletRequest request, Model model) {
        model.addAttribute("remoteUser", request.getRemoteUser());
        model.addAttribute("ruleNames", ruleNameService.ruleNameAll());
        return "ruleName/list";
    }

    /**
     * Affiche le formulaire d'ajout d'une règle.
     *
     * @param ruleName un objet RuleName vide injecté dans le modèle
     * @param model    le modèle pour transmettre l'objet à la vue
     * @return le nom de la vue "ruleName/add"
     */
    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleName ruleName, Model model) {
        model.addAttribute("ruleName", ruleName);
        return "ruleName/add";
    }

    /**
     * Valide et enregistre une nouvelle règle, après soumission du formulaire.
     *
     * @param ruleName l'objet RuleName à valider et enregistrer
     * @param result   le résultat de la validation
     * @return redirection vers la liste si succès, sinon retour à "ruleName/add"
     */
    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result) {
        if (!result.hasErrors() && ruleNameService.addRuleName(ruleName)) {
            return REDIRECT_RULE_NAME;
        }
        return "ruleName/add";
    }

    /**
     * Affiche le formulaire de mise à jour d'une règle existante.
     *
     * @param id    l'identifiant de la règle à modifier
     * @param model le modèle contenant l'objet RuleName
     * @return le nom de la vue "ruleName/update"
     */
    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        RuleName ruleName = ruleNameService.ruleNameById(id);
        model.addAttribute("ruleName", ruleName);
        return "ruleName/update";
    }

    /**
     * Met à jour une règle après validation.
     *
     * @param id       l'identifiant de la règle à mettre à jour
     * @param ruleName l'objet RuleName modifié
     * @param result   les résultats de validation
     * @return redirection vers la liste si succès, sinon retour à "ruleName/update"
     */
    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                                 BindingResult result) {
        if (!result.hasErrors() && ruleNameService.updateRuleName(ruleName, id)) {
            return REDIRECT_RULE_NAME;
        }
        return "ruleName/update";
    }

    /**
     * Supprime une règle existante.
     *
     * @param id    l'identifiant de la règle à supprimer
     * @param model le modèle (non utilisé ici, mais requis par la signature)
     * @return redirection vers la liste des règles
     */
    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        ruleNameService.deleteRuleName(id);
        return REDIRECT_RULE_NAME;
    }
}
