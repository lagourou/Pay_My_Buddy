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

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") RegisterRequestDTO userDto, Model model,
            BindingResult result) {

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
        }

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {

            model.addAttribute("error", "Email déjà utilisé");
            return "register";
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);

        model.addAttribute("success", "Inscription réussie");
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String register(Model model) {

        model.addAttribute("user", new RegisterRequestDTO());
        return "register";
    }
}
