package org.oc.poseidon.service;

import org.oc.poseidon.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service personnalisé implémentant {@link UserDetailsService}
 * utilisé par Spring Security pour l'authentification des utilisateurs.
 * <p>
 * Cette classe permet de charger un utilisateur à partir de son nom d'utilisateur.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructeur injectant le dépôt utilisateur {@link UserRepository}.
     *
     * @param userRepository le repository d'accès aux utilisateurs
     */
    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Charge un utilisateur par son nom d'utilisateur.
     *
     * @param username le nom d'utilisateur fourni lors de l'authentification
     * @return un objet {@link UserDetails} correspondant à l'utilisateur
     * @throws UsernameNotFoundException si l'utilisateur n'existe pas
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Utilisateur non trouvé : " + username);
        }
        return user;
    }
}
