package com.pay_my_buddy.OC_P6.unitaire.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import com.pay_my_buddy.OC_P6.controller.RegisterController;
import com.pay_my_buddy.OC_P6.dto.RegisterRequestDTO;
import com.pay_my_buddy.OC_P6.model.User;
import com.pay_my_buddy.OC_P6.repository.UserRepository;
import com.pay_my_buddy.OC_P6.unitaire.configuration.SecurityConfigTest;

@WebMvcTest(RegisterController.class)
@Import(SecurityConfigTest.class)
public class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    private RegisterRequestDTO validUser;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        // Initialise un utilisateur fictif
        validUser = new RegisterRequestDTO();
        validUser.setUsername("testuser");
        validUser.setEmail("test@example.com");
        validUser.setPassword("password");
    }

    @Test
    @DisplayName("GET /register doit être accessible (status 200)")
    void testGetRegisterAccessible() throws Exception {
        // Vérifie que la page d'inscription est accessible
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /register avec des infos valides doit rediriger (3xx)")
    void testPostRegisterAccessible() throws Exception {
        // Simule un utilisateur non existant et un mot de passe encodé
        when(userRepository.findByEmail(validUser.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(validUser.getPassword())).thenReturn("encodedPassword");

        // Vérifie que la soumission du formulaire redirige bien
        mockMvc.perform(post("/register")
                .param("username", validUser.getUsername())
                .param("email", validUser.getEmail())
                .param("password", validUser.getPassword())
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("POST /register avec email déjà utilisé doit rester sur la page (200)")
    void testPostRegisterEmailExistantAccessible() throws Exception {
        // Simule un utilisateur déjà existant
        when(userRepository.findByEmail(validUser.getEmail())).thenReturn(Optional.of(new User()));

        // Vérifie que la soumission du formulaire ne redirige pas et reste sur la page
        mockMvc.perform(post("/register")
                .param("username", validUser.getUsername())
                .param("email", validUser.getEmail())
                .param("password", validUser.getPassword())
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());
    }
}