package com.pay_my_buddy.OC_P6.unitaire.exception;

import com.pay_my_buddy.OC_P6.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;
    private RedirectAttributes redirectAttributes;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
        redirectAttributes = new RedirectAttributesModelMap(); // ✅ Simulation de `RedirectAttributes`
    }

    @Test
    void testHandleResourceNotFoundException() {
        EntityNotFoundException exception = new EntityNotFoundException("Utilisateur introuvable");

        String result = globalExceptionHandler.handleResourceNotFoundException(exception, redirectAttributes);

        assertEquals("redirect:/login", result);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("error"));
        assertEquals("Utilisateur introuvable", redirectAttributes.getFlashAttributes().get("error"));
    }

    @Test
    void testHandleInsufficientBalanceException() {
        InsufficientBalanceException exception = new InsufficientBalanceException("Solde insuffisant");

        String result = globalExceptionHandler.handleInsufficientBalanceException(exception, redirectAttributes);

        assertEquals("redirect:/transaction", result);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("error"));
        assertEquals("Solde insuffisant", redirectAttributes.getFlashAttributes().get("error"));
    }

    @Test
    void testHandleSelfTransferredAmountException() {
        SelfTransferredAmountException exception = new SelfTransferredAmountException("Transfert à soi-même interdit");

        String result = globalExceptionHandler.handleSelfTransferredAmountException(exception, redirectAttributes);

        assertEquals("redirect:/transaction", result);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("error"));
        assertEquals("Transfert à soi-même interdit", redirectAttributes.getFlashAttributes().get("error"));
    }

    @Test
    void testHandleContactAlreadyExistException() {
        ContactAlreadyExistException exception = new ContactAlreadyExistException("Le contact existe déjà");

        String result = globalExceptionHandler.handleContactAlreadyExistException(exception, redirectAttributes);

        assertEquals("redirect:/userConnections", result);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("error"));
        assertEquals("Le contact existe déjà", redirectAttributes.getFlashAttributes().get("error"));
    }

    @Test
    void testHandleFriendAlreadyExistsException() {
        FriendAlreadyExistsException exception = new FriendAlreadyExistsException("L'ami est déjà enregistré");

        String result = globalExceptionHandler.handleFriendAlreadyExistsException(exception, redirectAttributes);

        assertEquals("redirect:/userConnections", result);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("error"));
        assertEquals("L'ami est déjà enregistré", redirectAttributes.getFlashAttributes().get("error"));
    }

    @Test
    void testHandleUserNotFoundException() {
        UserNotFoundException exception = new UserNotFoundException("Utilisateur non trouvé");

        String result = globalExceptionHandler.handleUserNotFoundException(exception, redirectAttributes);

        assertEquals("redirect:/userConnections", result);
        assertTrue(redirectAttributes.getFlashAttributes().containsKey("error"));
        assertEquals("Utilisateur non trouvé", redirectAttributes.getFlashAttributes().get("error"));
    }
}
