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
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User user;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("Laurent");
        user.setEmail("laurent@example.com");
        user.setPassword("securepassword");
    }

    @Test
    void testLoadUserByUsernameSuccess() {
        when(userRepository.findByEmail("laurent@example.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("laurent@example.com");

        assertNotNull(userDetails);
        assertEquals("laurent@example.com", ((UserDetailsImplements) userDetails).getEmail()); // ✅ Vérification
                                                                                               // correcte
    }

    @Test
    void testLoadUserByUsernameNotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("notfound@example.com"));

        assertEquals("L'utilisateur avec l'email notfound@example.com n'existe pas", exception.getMessage());
    }
}
