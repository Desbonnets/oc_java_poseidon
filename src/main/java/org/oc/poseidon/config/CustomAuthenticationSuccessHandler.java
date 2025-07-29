package org.oc.poseidon.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

/**
 * Gestionnaire personnalisé appelé après une authentification réussie.
 * <p>
 * Ce composant permet de rediriger l'utilisateur vers une page spécifique
 * en fonction de son rôle. Par exemple :
 * <ul>
 *     <li>ROLE_ADMIN ⟶ /admin/home</li>
 *     <li>ROLE_USER ⟶ /user/home</li>
 *     <li>Autres rôles ⟶ /</li>
 * </ul>
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * Méthode appelée automatiquement après une authentification réussie.
     *
     * @param request        la requête HTTP
     * @param response       la réponse HTTP
     * @param authentication les informations d'authentification de l'utilisateur connecté
     * @throws IOException      en cas d'erreur d'entrée/sortie
     * @throws ServletException en cas d'erreur liée au traitement de la requête
     */
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

            if (role.equals("ROLE_ADMIN")) {
                response.sendRedirect("/admin/home");
                return;
            } else if (role.equals("ROLE_USER")) {
                response.sendRedirect("/user/home");
                return;
            }
        }

        // Redirection par défaut si aucun rôle attendu n’est trouvé
        response.sendRedirect("/");
    }
}
