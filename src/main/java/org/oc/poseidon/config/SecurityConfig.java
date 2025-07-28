package org.oc.poseidon.config;

import org.oc.poseidon.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration principale de la sécurité de l'application.
 * <p>
 * Cette classe configure :
 * <ul>
 *     <li>L'encodeur de mot de passe (BCrypt)</li>
 *     <li>Le filtre de sécurité (pages protégées, login, logout, redirection, etc.)</li>
 *     <li>L'authentification personnalisée via {@link CustomUserDetailsService}</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailService;
    private final CustomAuthenticationSuccessHandler successHandler;

    /**
     * Constructeur avec injection des composants nécessaires.
     *
     * @param userDetailService service chargé de récupérer les informations utilisateur
     * @param successHandler    handler appelé après une authentification réussie
     */
    public SecurityConfig(CustomUserDetailsService userDetailService, CustomAuthenticationSuccessHandler successHandler) {
        this.userDetailService = userDetailService;
        this.successHandler = successHandler;
    }

    /**
     * Bean Spring pour encoder les mots de passe avec BCrypt.
     *
     * @return un encodeur BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Chaîne de filtres de sécurité Spring Security.
     * <p>
     * Déclare :
     * <ul>
     *     <li>les routes publiques : /login, /access-denied, /, /static/**</li>
     *     <li>l'authentification requise pour toutes les autres</li>
     *     <li>la page de login personnalisée</li>
     *     <li>la redirection après succès via {@link CustomAuthenticationSuccessHandler}</li>
     *     <li>la gestion de la déconnexion</li>
     *     <li>la page d'accès refusé</li>
     * </ul>
     *
     * @param http l'objet de configuration de la sécurité HTTP
     * @return la configuration finalisée de la sécurité
     * @throws Exception en cas d'erreur de configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**")
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/login", "/access-denied", "/", "/static/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(successHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/access-denied")
                );

        return http.build();
    }

    /**
     * Déclare le gestionnaire d'authentification avec le provider personnalisé.
     *
     * @param passwordEncoder l'encodeur de mot de passe à utiliser
     * @return le {@link AuthenticationManager} configuré
     */
    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(this.userDetailService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }
}
