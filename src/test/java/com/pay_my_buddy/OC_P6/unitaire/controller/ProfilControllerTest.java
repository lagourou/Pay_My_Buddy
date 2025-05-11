package com.pay_my_buddy.OC_P6.unitaire.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.pay_my_buddy.OC_P6.configuration.UserDetailsImplements;
import com.pay_my_buddy.OC_P6.controller.ProfilController;
import com.pay_my_buddy.OC_P6.dto.RegisterRequestDTO;
import com.pay_my_buddy.OC_P6.mapper.UserMapper;
import com.pay_my_buddy.OC_P6.model.User;
import com.pay_my_buddy.OC_P6.service.UserService;
import com.pay_my_buddy.OC_P6.unitaire.configuration.SecurityConfigTest;

@WebMvcTest(ProfilController.class)
@Import(SecurityConfigTest.class)
public class ProfilControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserMapper userMapper;

    private User testUser;
    private RegisterRequestDTO testUserFormDTO;
    private UserDetailsImplements userDetails;

    @BeforeEach
    public void setup() throws Exception {
        // Création d'un utilisateur de test
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setAccountBalance(BigDecimal.valueOf(100.0));

        // Création d'un DTO correspondant
        testUserFormDTO = new RegisterRequestDTO();
        testUserFormDTO.setEmail("test@example.com");
        testUserFormDTO.setUsername("testuser");

        // Création d'un UserDetailsImpl pour simuler l'authentification
        userDetails = new UserDetailsImplements(testUser);

        // Configuration des mocks
        when(userService.getUserById(1L)).thenReturn(testUser);
        when(userMapper.toRegisterRequestDTO(testUser)).thenReturn(testUserFormDTO);
        doNothing().when(userService).updateUser(any(RegisterRequestDTO.class), anyLong());
    }

    @Test
    @DisplayName("Devrait afficher la page de profil pour un utilisateur authentifié")
    @WithMockUser
    public void testGetProfilPage() throws Exception {
        // Test de l'accès à la page de profil avec un utilisateur authentifié
        mockMvc.perform(get("/profil")
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profil"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", testUserFormDTO));

        // Vérification que les méthodes du service ont été appelées
        verify(userService, times(1)).getUserById(1L);
        verify(userMapper, times(1)).toRegisterRequestDTO(testUser);
    }

    @Test
    @DisplayName("Devrait rediriger vers la page de connexion si l'utilisateur n'est pas authentifié")
    public void testGetProfilPageWithoutAuthentication() throws Exception {
        // Test direct du contrôleur avec userDetails null
        ProfilController controller = new ProfilController(userService, userMapper);
        String viewName = controller.profil(null, null);

        // Vérification que la vue retournée est bien une redirection vers /login
        assert viewName.equals("redirect:/login");
    }

    @Test
    @DisplayName("Devrait mettre à jour le profil avec succès")
    public void testUpdateProfilSuccess() throws Exception {
        mockMvc.perform(post("/modify-profil")
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .param("email", "updated@example.com")
                .param("username", "updateduser")
                .param("password", "Password2025"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil"))
                .andExpect(flash().attributeExists("success"));

        verify(userService, times(1)).updateUser(any(RegisterRequestDTO.class), eq(1L));
    }

    @Test
    @DisplayName("Devrait rester sur la page de profil en cas d'erreurs de validation")
    @WithMockUser
    public void testUpdateProfilWithValidationErrors() throws Exception {
        // Test de la mise à jour du profil avec des erreurs de validation
        mockMvc.perform(post("/modify-profil")
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .param("email", "") // Email vide pour provoquer une erreur de validation
                .param("username", "updateduser"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profil"));
    }

    @Test
    @DisplayName("Devrait mettre à jour le mot de passe si fourni")
    @WithMockUser
    public void testUpdateProfilWithPassword() throws Exception {
        // Test de la mise à jour du profil avec un nouveau mot de passe
        mockMvc.perform(post("/modify-profil")
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .param("email", "test@example.com")
                .param("username", "testuser")
                .param("password", "newpassword"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profil"))
                .andExpect(flash().attributeExists("success"));

        // Vérification que la méthode de mise à jour a été appelée
        verify(userService, times(1)).updateUser(any(RegisterRequestDTO.class), eq(1L));
    }
}