package com.pay_my_buddy.OC_P6.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.pay_my_buddy.OC_P6.dto.RegisterRequestDTO;
import com.pay_my_buddy.OC_P6.model.User;
import com.pay_my_buddy.OC_P6.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RegisterController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String register(Model model) {

        log.info("Accès à la page d'inscription");
        model.addAttribute("user", new RegisterRequestDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") RegisterRequestDTO userDto, Model model,
            BindingResult result) {

        if (result.hasErrors()) {
            log.warn("Erreur dans le formulaire d'inscription");
            model.addAttribute("user", userDto);
            return "register";
        }

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {

            log.warn("Email déjà utilisé : {}", userDto.getEmail());
            model.addAttribute("error", "Email déjà utilisé");
            return "register";
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);

        log.info("Nouvel utilisateur inscrit : {}", userDto.getEmail());
        model.addAttribute("success", "Inscription réussie");
        return "redirect:/login";
    }

}
