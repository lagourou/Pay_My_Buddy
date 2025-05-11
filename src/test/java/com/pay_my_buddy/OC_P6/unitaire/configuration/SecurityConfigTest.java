package com.pay_my_buddy.OC_P6.unitaire.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
@EnableWebSecurity
public class SecurityConfigTest {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Nouvelle syntaxe fonctionnelle sans authorizeRequests() et permitAll()
        http
                .csrf(csrf -> csrf.disable()) // Désactive la protection CSRF pour les tests
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().permitAll() // Permet l'accès à toutes les URL
                );
        return http.build();
    }
}
