package com.pay_my_buddy.OC_P6.unitaire.configuration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import com.pay_my_buddy.OC_P6.configuration.UserDetailsImplements;
import com.pay_my_buddy.OC_P6.model.User;

class UserDetailsImplementsTest {

    private User user; // Objet utilisateur pour les tests
    private UserDetailsImplements userDetails; // Objet UserDetails basé sur l'utilisateur

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        // Initialise un utilisateur fictif
        user = new User();
        user.setId(1L);
        user.setUsername("Laurent");
        user.setEmail("laurent@example.com");
        user.setPassword("securepassword");

        // Crée un UserDetails à partir de l'utilisateur
        userDetails = new UserDetailsImplements(user);
    }

    @Test
    void testGetUsername() {
        // Vérifie que le username est correct
        assertEquals("Laurent", userDetails.getUsername());
    }

    @Test
    void testGetEmail() {
        // Vérifie que l'email est correct
        assertEquals("laurent@example.com", userDetails.getEmail());
    }

    @Test
    void testGetId() {
        // Vérifie que l'ID est correct
        assertEquals(1L, userDetails.getId());
    }

    @Test
    void testGetPassword() {
        // Vérifie que le mot de passe est correct
        assertEquals("securepassword", userDetails.getPassword());
    }

    @Test
    void testGetAuthorities() {
        // Vérifie que la liste des autorités est bien vide
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    void testIsAccountNonExpired() {
        // Vérifie que le compte n'est pas expiré
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    void testIsCredentialsNonExpired() {
        // Vérifie que les identifiants ne sont pas expirés
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        // Vérifie que le compte est activé
        assertTrue(userDetails.isEnabled());
    }
}
