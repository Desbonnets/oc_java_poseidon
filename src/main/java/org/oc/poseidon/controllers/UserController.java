package org.oc.poseidon.controllers;

import org.oc.poseidon.domain.User;
import org.oc.poseidon.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Contrôleur Spring MVC pour la gestion des utilisateurs.
 * <p>
 * Toutes les actions sont restreintes aux utilisateurs ayant le rôle {@code ADMIN}.
 * <p>
 * Gère les routes suivantes :
 * <ul>
 *     <li>GET /user/list</li>
 *     <li>GET /user/add</li>
 *     <li>POST /user/validate</li>
 *     <li>GET /user/update/{id}</li>
 *     <li>POST /user/update/{id}</li>
 *     <li>GET /user/delete/{id}</li>
 * </ul>
 */
@PreAuthorize("hasRole('ADMIN')")
@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Affiche la liste de tous les utilisateurs.
     *
     * @param model le modèle contenant la liste des utilisateurs
     * @return la vue "user/list"
     */
    @RequestMapping("/user/list")
    public String home(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user/list";
    }

    /**
     * Affiche le formulaire d'ajout d'un nouvel utilisateur.
     *
     * @param user un objet {@link User} vide injecté automatiquement
     * @return la vue "user/add"
     */
    @GetMapping("/user/add")
    public String addUser(User user) {
        return "user/add";
    }

    /**
     * Valide et enregistre un utilisateur après soumission du formulaire.
     * Le mot de passe est encodé avant la sauvegarde.
     *
     * @param user   l'utilisateur à valider
     * @param result les erreurs de validation
     * @param model  le modèle contenant les données
     * @return redirection vers la liste ou retour à la vue d'ajout
     */
    @PostMapping("/user/validate")
    public String validate(@Valid User user, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
            userRepository.save(user);
            model.addAttribute("users", userRepository.findAll());
            return "redirect:/user/list";
        }
        return "user/add";
    }

    /**
     * Affiche le formulaire de mise à jour d'un utilisateur.
     * Le champ mot de passe est vidé pour éviter de pré-remplir l'encodage.
     *
     * @param id    l'identifiant de l'utilisateur
     * @param model le modèle contenant l'utilisateur
     * @return la vue "user/update"
     */
    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        user.setPassword(""); // éviter de montrer le hash
        model.addAttribute("user", user);
        return "user/update";
    }

    /**
     * Met à jour les données d'un utilisateur existant.
     * Le mot de passe est ré-encodé.
     *
     * @param id     l'identifiant de l'utilisateur
     * @param user   les données mises à jour
     * @param result les erreurs de validation
     * @param model  le modèle
     * @return redirection vers la liste ou retour à la vue d'édition
     */
    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/update";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setId(id);
        userRepository.save(user);
        model.addAttribute("users", userRepository.findAll());
        return "redirect:/user/list";
    }

    /**
     * Supprime un utilisateur à partir de son identifiant.
     *
     * @param id    l'identifiant de l'utilisateur
     * @param model le modèle
     * @return redirection vers la liste des utilisateurs
     */
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userRepository.delete(user);
        model.addAttribute("users", userRepository.findAll());
        return "redirect:/user/list";
    }
}
