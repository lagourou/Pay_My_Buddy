package com.pay_my_buddy.OC_P6.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pay_my_buddy.OC_P6.dto.UserConnectionsResponseDTO;
import com.pay_my_buddy.OC_P6.service.UserConnectionsService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserConnectionsController {

    private final UserConnectionsService userConnectionsService;

    @PostMapping("/add")
    public String addConnection(@RequestParam Long userId, @RequestParam String email,
            RedirectAttributes redirectAttributes) {

        userConnectionsService.addConnections(userId, email);
        redirectAttributes.addFlashAttribute("success", "Connexion ajoutée avec succès");

        return "redirect:/friends?add";

    }

    @GetMapping("/del")
    public String deleteConnection(@RequestParam Long userId, @RequestParam String email,
            RedirectAttributes redirectAttributes) {

        userConnectionsService.deleteConnection(userId, email);
        redirectAttributes.addFlashAttribute("success", "Connexion supprimée avec succès");
        return "redirect:/friends?del";

    }

    @GetMapping("/friends")
    public String showFriends(@RequestParam Long userId, Model model) {

        List<UserConnectionsResponseDTO> connections = userConnectionsService.getUserConnections(userId);
        model.addAttribute("friends", connections);

        return "friends";
    }

}
