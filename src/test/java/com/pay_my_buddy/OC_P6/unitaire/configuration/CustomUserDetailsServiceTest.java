package com.pay_my_buddy.OC_P6.unitaire.configuration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.pay_my_buddy.OC_P6.configuration.CustomUserDetailsService;
import com.pay_my_buddy.OC_P6.configuration.UserDetailsImplements;
import com.pay_my_buddy.OC_P6.model.User;
import com.pay_my_buddy.OC_P6.repository.UserRepository;

class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;// Simule la base de données des utilisateurs

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;// Service testé

    private User user;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        MockitoAnnotations.openMocks(this);// Initialise les mocks

        // Création d'un utilisateur fictif pour les tests
        user = new User();
        user.setId(1L);
        user.setUsername("Laurent");
        user.setEmail("laurent@example.com");
        user.setPassword("securepassword");
    }

    @Test
    void testLoadUserByUsernameSuccess() {

        // Simule un utilisateur existant en base
        when(userRepository.findByEmail("laurent@example.com")).thenReturn(Optional.of(user));

        // Appelle le service pour récupérer l'utilisateur
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("laurent@example.com");

        // Vérifie que l'utilisateur est bien trouvé
        assertNotNull(userDetails);
        assertEquals("laurent@example.com", ((UserDetailsImplements) userDetails).getEmail());
    }

    @Test
    void testLoadUserByUsernameNotFound() {

        // Simule un utilisateur introuvable en base
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        // Vérifie qu'une exception est bien levée
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("notfound@example.com"));

        assertEquals("L'utilisateur avec l'email notfound@example.com n'existe pas", exception.getMessage());
    }
}
