package com.pay_my_buddy.OC_P6.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.pay_my_buddy.OC_P6.filter.JwtFilter;
import com.pay_my_buddy.OC_P6.service.UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final UserService userService;
        private final JwtUtils jwtUtils;

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder)
                        throws Exception {

                AuthenticationManagerBuilder authenticationManagerBuilder = http
                                .getSharedObject(AuthenticationManagerBuilder.class);
                authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder);

                return authenticationManagerBuilder.build();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                return http.csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(auth -> auth.requestMatchers("/api/**", "/error").permitAll()
                                                .anyRequest().authenticated())
                                .addFilterBefore(new JwtFilter(userService, jwtUtils),
                                                UsernamePasswordAuthenticationFilter.class)
                                .build();

        }

}
