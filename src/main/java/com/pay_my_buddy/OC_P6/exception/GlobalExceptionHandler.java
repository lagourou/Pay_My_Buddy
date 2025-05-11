package com.pay_my_buddy.OC_P6.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleResourceNotFoundException(EntityNotFoundException ex,
            RedirectAttributes redirectAttributes) {
        log.warn("Élément introuvable : {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/login";
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public String handleInsufficientBalanceException(InsufficientBalanceException ex,
            RedirectAttributes redirectAttributes) {
        log.warn("Solde insuffisant : {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/transaction";
    }

    @ExceptionHandler(SelfTransferredAmountException.class)
    public String handleSelfTransferredAmountException(SelfTransferredAmountException ex,
            RedirectAttributes redirectAttributes) {
        log.warn("Transfert à soi-même non autorisé : {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/transaction";
    }

    @ExceptionHandler(ContactAlreadyExistException.class)
    public String handleContactAlreadyExistException(ContactAlreadyExistException ex,
            RedirectAttributes redirectAttributes) {
        log.warn("Contact déjà existant : {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/userConnections";
    }

    @ExceptionHandler(FriendAlreadyExistsException.class)
    public String handleFriendAlreadyExistsException(FriendAlreadyExistsException ex,
            RedirectAttributes redirectAttributes) {
        log.warn("Amitié déjà existante : {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/userConnections";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(UserNotFoundException ex, RedirectAttributes redirectAttributes) {
        log.warn("Utilisateur introuvable : {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/userConnections";
    }
}
