package com.pay_my_buddy.OC_P6.unitaire.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pay_my_buddy.OC_P6.controller.UserConnectionsController;
import com.pay_my_buddy.OC_P6.model.User;
import com.pay_my_buddy.OC_P6.model.UserConnections;
import com.pay_my_buddy.OC_P6.service.UserConnectionsService;
import com.pay_my_buddy.OC_P6.service.UserService;
import com.pay_my_buddy.OC_P6.configuration.UserDetailsImplements;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.lang.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

class UserConnectionsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserConnectionsService userConnectionsService;

    @Mock
    private UserService userService;

    private UserConnectionsController userConnectionsController;

    @BeforeEach
    @SuppressWarnings("unused")
    void setup() {
        MockitoAnnotations.openMocks(this);

        userConnectionsController = new UserConnectionsController(userConnectionsService, userService);
        // Simule la récupération de l'utilisateur authentifié
        HandlerMethodArgumentResolver mockPrincipalResolver = new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return parameter.getParameterAnnotation(AuthenticationPrincipal.class) != null;
            }

            @Override
            public Object resolveArgument(MethodParameter parameter,
                    @Nullable ModelAndViewContainer mavContainer,
                    NativeWebRequest webRequest,
                    @Nullable WebDataBinderFactory binderFactory) {
                UserDetailsImplements userDetails = mock(UserDetailsImplements.class);
                when(userDetails.getId()).thenReturn(1L);
                return userDetails;
            }
        };
        // Spécifie le début et la fin des noms de fichiers HTML
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".html");

        // Configure le contrôleur avec la gestion des vues et des paramètres
        mockMvc = MockMvcBuilders.standaloneSetup(userConnectionsController)
                .setViewResolvers(viewResolver)
                .setCustomArgumentResolvers(mockPrincipalResolver)
                .build();
    }

    @Test
    void testAddConnection_Success() throws Exception {

        // Simule un utilisateur ami
        User friend = new User();
        friend.setId(2L);
        friend.setEmail("friend@test.com");

        when(userService.getUserByEmail("friend@test.com")).thenReturn(friend);
        when(userConnectionsService.addConnections(1L, "friend@test.com"))
                .thenReturn(new UserConnections());
        // Vérifie qu'une connexion utilisateur peut être ajoutée
        mockMvc.perform(post("/add").param("email", "friend@test.com"))
                .andExpect(status().is3xxRedirection())// Vérifie qu'il y a bien une redirection
                .andExpect(redirectedUrl("/userConnections?add"));// Vérifie que la redirection est correcte

        // Vérifie que le service a été appelé
        verify(userConnectionsService).addConnections(1L, "friend@test.com");
    }

    @Test
    void testAddConnection_Error() throws Exception {
        // Simule un utilisateur inexistant
        when(userService.getUserByEmail("invalid@test.com")).thenReturn(null);

        // Vérifie qu'aucune connexion utilisateur ne peut être ajoutée si l'utilisateur
        // est introuvable
        mockMvc.perform(post("/add").param("email", "invalid@test.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/userConnections?add"));

        // Vérifie que le service n'a pas été appelé
        verify(userConnectionsService, never()).addConnections(anyLong(), anyString());
    }

    @Test
    void testShowConnections() throws Exception {

        // Vérifie que la page des connexions utilisateur affiche correctement les
        // messages de succès et d'erreur
        mockMvc.perform(get("/userConnections")
                .param("success", "Opération réussie")
                .param("error", "Une erreur s'est produite"))
                .andExpect(status().isOk())// Vérifie que la requête est traitée avec succès
                .andExpect(model().attribute("success", "Opération réussie"))// Vérifie le message de succès
                .andExpect(model().attribute("error", "Une erreur s'est produite"))// Vérifie le message d'erreur
                .andExpect(view().name("userConnections"));// Vérifie que la vue affichée est correcte
    }
}
