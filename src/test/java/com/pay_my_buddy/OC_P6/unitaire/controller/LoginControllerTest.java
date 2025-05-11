package com.pay_my_buddy.OC_P6.unitaire.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest // Charge le contexte Spring pour les tests
@AutoConfigureMockMvc // Configure automatiquement MockMvc pour simuler des requêtes HTTP
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc; // Simule des requêtes HTTP pour tester les contrôleurs

    @Test
    public void testLoginPage() throws Exception {
        // Envoie une requête GET à /login et vérifie que la réponse est correcte
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk()) // Vérifie que le statut HTTP est 200 (OK)
                .andExpect(view().name("login")); // Vérifie que la vue rendue est "login"
    }
}
