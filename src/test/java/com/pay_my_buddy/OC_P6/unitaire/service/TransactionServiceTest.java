package com.pay_my_buddy.OC_P6.unitaire.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.pay_my_buddy.OC_P6.dto.TransactionResponseDTO;
import com.pay_my_buddy.OC_P6.mapper.TransactionMapper;
import com.pay_my_buddy.OC_P6.model.Transaction;
import com.pay_my_buddy.OC_P6.model.User;
import com.pay_my_buddy.OC_P6.repository.TransactionRepository;
import com.pay_my_buddy.OC_P6.repository.UserRepository;
import com.pay_my_buddy.OC_P6.service.TransactionService;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionService transactionService;

    private User sender;
    private User receiver;
    private Transaction transaction;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialise un expéditeur avec un solde de 100
        sender = new User();
        sender.setId(1L);
        sender.setUsername("Laurent");
        sender.setEmail("laurent@example.com");
        sender.setAccountBalance(BigDecimal.valueOf(100));
        // Initialise un destinataire avec un solde de 50
        receiver = new User();
        receiver.setId(2L);
        receiver.setUsername("Jean");
        receiver.setEmail("jean@example.com");
        receiver.setAccountBalance(BigDecimal.valueOf(50));
        // Initialise une transaction fictive de 20
        transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(BigDecimal.valueOf(20));
        transaction.setDescription("Paiement");
    }

    @Test
    void testAddNewTransactionSuccess() {

        // Simule l'ajout d'une transaction réussie
        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findByEmail("jean@example.com")).thenReturn(Optional.of(receiver));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(transactionMapper.toTransactionResponseDTO(any(Transaction.class)))
                .thenReturn(new TransactionResponseDTO());

        // Vérifie que la transaction est ajoutée correctement
        TransactionResponseDTO result = transactionService.addNewTransaction(1L, "jean@example.com",
                BigDecimal.valueOf(20), "Paiement");

        assertNotNull(result);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testAddNewTransactionInsufficientBalance() {

        // Simule un solde insuffisant
        sender.setAccountBalance(BigDecimal.valueOf(5));

        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findByEmail("jean@example.com")).thenReturn(Optional.of(receiver));
        // Vérifie que l'exception est levée
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.addNewTransaction(1L, "jean@example.com", BigDecimal.valueOf(20), "Paiement"));
        assertEquals("Le solde de l'expéditeur est insuffisant.", exception.getMessage());
    }

    @Test
    void testAddNewTransactionNegativeAmount() {

        // Vérifie qu'une transaction avec un montant négatif est rejetée
        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findByEmail("jean@example.com")).thenReturn(Optional.of(receiver));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.addNewTransaction(1L, "jean@example.com", BigDecimal.valueOf(-10),
                        "Paiement"));
        assertEquals("Le montant doit être supérieur à zéro.", exception.getMessage());
    }

    @Test
    void testGetUserTransactionHistorySuccess() {

        // Simule la récupération de l'historique des transactions
        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(transactionRepository.findBySenderId(1L)).thenReturn(Arrays.asList(transaction));
        when(transactionRepository.findByReceiverId(1L)).thenReturn(Arrays.asList());
        when(transactionMapper.toTransactionResponseDTO(any(Transaction.class)))
                .thenReturn(new TransactionResponseDTO());

        List<TransactionResponseDTO> result = transactionService.getUserTransactionHistory(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetUserTransactionHistoryUserNotFound() {

        // Vérifie qu'une exception est levée si l'utilisateur n'existe pas
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.getUserTransactionHistory(1L));
        assertEquals("L'utilisateur n'existe pas", exception.getMessage());
    }

    @Test
    void testFeedAccountSuccess() {

        // Simule une recharge de compte réussie
        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(transactionMapper.toTransactionResponseDTO(any(Transaction.class)))
                .thenReturn(new TransactionResponseDTO());

        TransactionResponseDTO result = transactionService.feedAccount(1L, BigDecimal.valueOf(50), "Recharge");

        assertNotNull(result);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testFeedAccountNegativeAmount() {

        // Vérifie qu'une recharge avec un montant négatif est rejetée
        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.feedAccount(1L, BigDecimal.valueOf(-10), "Recharge"));
        assertEquals("Le montant du dépôt doit être supérieur à zéro.", exception.getMessage());
    }
}
