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

import com.pay_my_buddy.OC_P6.service.UserConnectionsService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserConnectionsController {

    private final UserConnectionsService userConnectionsService;

    @PostMapping("/add")
    public String addConnection(@RequestParam String email,
            @AuthenticationPrincipal UserDetailsImplements userDetails,
            RedirectAttributes redirectAttributes) {
        try {
            userConnectionsService.addConnections(userDetails.getId(), email);
            redirectAttributes.addFlashAttribute("success", "Connexion ajoutée avec succès");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Erreur : " + e.getMessage());
        }
        return "redirect:/userConnections?add";
    }

    @GetMapping("/del")
    public String deleteConnection(@RequestParam Long userId, @RequestParam String email,
            RedirectAttributes redirectAttributes) {
        try {
            userConnectionsService.deleteConnection(userId, email);
            redirectAttributes.addFlashAttribute("success", "Connexion supprimée avec succès");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Erreur : " + e.getMessage());
        }
        return "redirect:/userConnections?del";
    }

    @GetMapping("/userConnections")
    public String showConnections(@ModelAttribute("success") String success, @ModelAttribute("error") String error,
            Model model) {
        model.addAttribute("success", success);
        model.addAttribute("error", error);
        return "userConnections";
    }

}
