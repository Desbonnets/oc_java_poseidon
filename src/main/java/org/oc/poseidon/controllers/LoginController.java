package org.oc.poseidon.controllers;

import org.oc.poseidon.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Contrôleur gérant les routes d'authentification et les accès sécurisés.
 * <p>
 * Ce contrôleur permet l'affichage de la page de login, la gestion des accès refusés,
 * ainsi que l'affichage de certaines ressources sécurisées réservées aux administrateurs.
 */
@Controller
@RequestMapping()
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Affiche la page de connexion.
     *
     * @return une {@link ModelAndView} vers la vue "login/login"
     */
    @GetMapping("login")
    public ModelAndView login() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login/login");
        return mav;
    }

    /**
     * Affiche une page listant tous les utilisateurs (réservée aux administrateurs).
     *
     * @return une {@link ModelAndView} vers la vue "user/list" contenant tous les utilisateurs
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("secure/article-details")
    public ModelAndView getAllUserArticles() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("users", userRepository.findAll());
        mav.setViewName("user/list");
        return mav;
    }

    /**
     * Affiche la page d'erreur d'accès refusé (403).
     *
     * @return une {@link ModelAndView} vers la vue "403" avec un message d'erreur
     */
    @GetMapping("access-denied")
    public ModelAndView error() {
        ModelAndView mav = new ModelAndView();
        String errorMessage = "You are not authorized for the requested data.";
        mav.addObject("errorMsg", errorMessage);
        mav.setViewName("403");
        return mav;
    }
}
