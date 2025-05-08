package com.pay_my_buddy.OC_P6.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pay_my_buddy.OC_P6.configuration.UserDetailsImplements;
import com.pay_my_buddy.OC_P6.dto.AccountBalanceRequestDTO;
import com.pay_my_buddy.OC_P6.dto.TransactionRequestDTO;
import com.pay_my_buddy.OC_P6.service.TransactionService;
import com.pay_my_buddy.OC_P6.service.UserConnectionsService;
import com.pay_my_buddy.OC_P6.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TransactionController {

    private final UserService userService;
    private final TransactionService transactionService;
    private final UserConnectionsService userConnectionsService;

    @GetMapping("/transaction")
    public String transaction(Model model, @AuthenticationPrincipal UserDetailsImplements userDetails,
            @ModelAttribute TransactionRequestDTO transactionRequest,
            @ModelAttribute AccountBalanceRequestDTO balanceRequest) throws Exception {

        model.addAttribute("balanceResponse", balanceRequest);
        model.addAttribute("transactionResponse", transactionRequest);
        model.addAttribute("friends", userConnectionsService.getUserConnections(userDetails.getId()));
        model.addAttribute("transactionsList", transactionService.getUserTransactionHistory(userDetails.getId()));
        model.addAttribute("userBalance", userService.getUserById(userDetails.getId()).getAccountBalance());
        model.addAttribute("transactionRequest", new TransactionRequestDTO());

        return "transaction";
    }

    @PostMapping("/pay")
    public String pay(@ModelAttribute TransactionRequestDTO transactionRequest,
            @AuthenticationPrincipal UserDetailsImplements userDetails, RedirectAttributes redirectAttributes) {

        try {
            transactionService.addNewTransaction(userDetails.getId(), transactionRequest.getEmail(),
                    transactionRequest.getAmount(), transactionRequest.getDescription());

            redirectAttributes.addFlashAttribute("success", "Paiement réussi");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Erreur : " + e.getMessage());
        }
        return "redirect:/transaction";
    }

    @PostMapping("/payment")
    public String payment(@ModelAttribute AccountBalanceRequestDTO balanceRequest,
            @AuthenticationPrincipal UserDetailsImplements userDetails, RedirectAttributes redirectAttributes)
            throws Exception {

        userService.addBalance(userService.getUserById(userDetails.getId()), balanceRequest.getAmount());

        redirectAttributes.addFlashAttribute("success", "Dépôt réussi");

        return "redirect:/transaction";
    }

}