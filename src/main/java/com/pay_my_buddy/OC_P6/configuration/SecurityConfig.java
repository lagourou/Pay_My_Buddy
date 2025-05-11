package com.pay_my_buddy.OC_P6.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final CustomUserDetailsService customUserDetailsService;

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder)
                        throws Exception {

                AuthenticationManagerBuilder authenticationManagerBuilder = http
                                .getSharedObject(AuthenticationManagerBuilder.class);
                authenticationManagerBuilder.userDetailsService(customUserDetailsService)
                                .passwordEncoder(passwordEncoder);

                return authenticationManagerBuilder.build();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                return http.authorizeHttpRequests(auth -> auth.requestMatchers("/", "/login", "/register").permitAll()
                                .requestMatchers("/css/**").permitAll()
                                .requestMatchers("/add", "/userConnections", "/profil", "/transaction", "/pay")
                                .authenticated()
                                .anyRequest().authenticated())
                                .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/transaction", true)
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                                                .logoutSuccessUrl("/login?logout")
                                                .permitAll())

                                .build();
        }
}
