package com.pay_my_buddy.OC_P6.unitaire.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration // Indique que cette classe est une configuration spécifique aux tests
@EnableWebSecurity // Active la sécurité web
public class SecurityConfigTest {

    // Initialise un encodeur de mot de passe avec BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configure la sécurité HTTP pour les tests
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Désactive CSRF pour éviter les blocages en test
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().permitAll() // Autorise l'accès à toutes les URLs sans restriction
                );
        return http.build();
    }
}
