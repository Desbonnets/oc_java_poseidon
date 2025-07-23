package org.oc.poseidon.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.oc.poseidon.domain.User;
import org.oc.poseidon.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    private UserRepository userRepository;
    private CustomUserDetailsService userDetailsService;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        userDetailsService = new CustomUserDetailsService(userRepository);
    }

    @Test
    @DisplayName("loadUserByUsername retourne un UserDetails si l'utilisateur existe")
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        User user = new User();
        user.setUsername("johndoe");
        user.setPassword("encrypted");
        user.setRole("ROLE_USER");

        when(userRepository.findByUsername("johndoe")).thenReturn(user);

        UserDetails result = userDetailsService.loadUserByUsername("johndoe");

        assertThat(result.getUsername()).isEqualTo("johndoe");
        assertThat(result.getPassword()).isEqualTo("encrypted");
        assertThat(result.getAuthorities()).anyMatch(a -> a.getAuthority().equals("ROLE_USER"));
    }

    @Test
    @DisplayName("loadUserByUsername lève une exception si l'utilisateur n'existe pas")
    void loadUserByUsername_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(null);

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("unknown"))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
