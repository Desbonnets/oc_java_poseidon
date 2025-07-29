package org.oc.poseidon.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Contrôleur principal gérant les routes d'accueil selon les rôles de l'utilisateur.
 * <p>
 * Contient les routes publiques et sécurisées pour les utilisateurs connectés avec les rôles {@code ADMIN} ou {@code USER}.
 */
@Controller
public class HomeController {

	/**
	 * Affiche la page d'accueil publique.
	 *
	 * @return le nom de la vue "home"
	 */
	@RequestMapping("/")
	public String home() {
		return "home";
	}

	/**
	 * Redirige les administrateurs connectés vers la liste des {@code BidList}.
	 * Accessible uniquement aux utilisateurs ayant le rôle {@code ADMIN}.
	 *
	 * @return redirection vers "/bidList/list"
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping("/admin/home")
	public String adminHome() {
		return "redirect:/bidList/list";
	}

	/**
	 * Affiche la page d'accueil des utilisateurs connectés avec le rôle {@code USER}.
	 * <p>
	 * Le nom d'utilisateur est ajouté au modèle via {@code request.getRemoteUser()}.
	 *
	 * @param request requête HTTP pour récupérer l'utilisateur connecté
	 * @param model   modèle de données pour la vue
	 * @return le nom de la vue "userHome"
	 */
	@PreAuthorize("hasRole('USER')")
	@RequestMapping("/user/home")
	public String userHome(HttpServletRequest request, Model model) {
		model.addAttribute("remoteUser", request.getRemoteUser());
		return "userHome";
	}
}
