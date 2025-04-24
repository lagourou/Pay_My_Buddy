package com.pay_my_buddy.OC_P6.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pay_my_buddy.OC_P6.configuration.JwtUtils;
import com.pay_my_buddy.OC_P6.model.User;
import com.pay_my_buddy.OC_P6.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class RegisterLoginController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("Email obligatoire");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email déjà utilisé");
        }
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Identifiant déjà existant");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getAccount_balance() == null) {
            user.setAccount_balance(BigDecimal.ZERO);
        }

        log.info("Utilisateur enregistré");
        return ResponseEntity.ok(userRepository.save(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            if (authentication.isAuthenticated()) {
                Map<String, Object> authData = new HashMap<>();
                authData.put("token", jwtUtils.generateToken(user.getUsername()));
                authData.put("type", "Bearer");

                log.info("Identifiant validé");
                return ResponseEntity.ok(authData);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Identifiant ou mot de passe invalide");
        } catch (AuthenticationException e) {

            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Identifiant ou mot de passe invalide");
        }
    }

}
