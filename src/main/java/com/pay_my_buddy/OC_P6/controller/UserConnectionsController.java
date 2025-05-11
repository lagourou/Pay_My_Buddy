package com.pay_my_buddy.OC_P6.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.pay_my_buddy.OC_P6.configuration.UserDetailsImplements;
import com.pay_my_buddy.OC_P6.model.User;
import com.pay_my_buddy.OC_P6.service.UserConnectionsService;
import com.pay_my_buddy.OC_P6.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserConnectionsController {

    private final UserConnectionsService userConnectionsService;
    private final UserService userService;

    @PostMapping("/add")
    public String addConnection(@RequestParam("email") String email,
            @AuthenticationPrincipal UserDetailsImplements userDetails,
            RedirectAttributes redirectAttributes) {
        try {
            User friend = userService.getUserByEmail(email);
            if (friend == null) {
                throw new IllegalArgumentException("Cet utilisateur n'existe pas.");
            }

            userConnectionsService.addConnections(userDetails.getId(), friend.getEmail());
            redirectAttributes.addFlashAttribute("success", "Connexion ajoutée avec succès");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Erreur : " + e.getMessage());
        }

        return "redirect:/userConnections?add";
    }

    @GetMapping("/userConnections")
    public String showConnections(@AuthenticationPrincipal UserDetailsImplements userDetails,
            @ModelAttribute("success") String success, @ModelAttribute("error") String error,
            Model model) {
        System.out.println("Appel du service : " + userConnectionsService.getUserConnections(userDetails.getId()));

        model.addAttribute("connections", userConnectionsService.getUserConnections(userDetails.getId()));

        model.addAttribute("success", success);
        model.addAttribute("error", error);
        return "userConnections";
    }

}
