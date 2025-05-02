package com.pay_my_buddy.OC_P6.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pay_my_buddy.OC_P6.dto.TransactionResponseDTO;
import com.pay_my_buddy.OC_P6.mapper.TransactionMapper;
import com.pay_my_buddy.OC_P6.model.Transaction;
import com.pay_my_buddy.OC_P6.model.User;
import com.pay_my_buddy.OC_P6.repository.TransactionRepository;
import com.pay_my_buddy.OC_P6.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final TransactionMapper transactionMapper;

    public List<Transaction> getTransactionsBySenderId(Long senderId) {
        return transactionRepository.findBySenderId(senderId);
    }

    public List<Transaction> getTransactionsByReceiverId(Long receiverId) {
        return transactionRepository.findByReceiverId(receiverId);
    }

    private Transaction createTransaction(User sender, User receiver, BigDecimal amount, String description) {
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setTransactionDate(LocalDateTime.now());
        return transaction;

    }

    @Transactional
    public TransactionResponseDTO addNewTransaction(Long senderId, Long receiverId, BigDecimal amount,
            String description) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("L'expéditeur n'existe pas"));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("Le destinataire n'existe pas"));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à zéro.");
        }

        BigDecimal feePercentage = new BigDecimal("0.005");
        BigDecimal feeAmount = amount.multiply(feePercentage).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalAmount = amount.add(feeAmount);

        if (sender.getAccountBalance().compareTo(totalAmount) < 0) {
            throw new IllegalArgumentException("Le solde de l'expéditeur est insuffisant.");
        }

        sender.setAccountBalance(sender.getAccountBalance().subtract(totalAmount));

        receiver.setAccountBalance(receiver.getAccountBalance().add(amount));

        Transaction transaction = createTransaction(sender, receiver, amount, description);
        transactionRepository.save(transaction);

        userRepository.saveAll(List.of(sender, receiver));

        return transactionMapper.toTransactionResponseDTO(transaction);
    }

    @Transactional
    public List<TransactionResponseDTO> getUserTransactionHistory(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("L'utilisateur n'existe pas"));

        List<Transaction> transactions = transactionRepository.findBySenderId(userId);
        transactions.addAll(transactionRepository.findByReceiverId(userId));

        return transactions.stream()
                .sorted(Comparator.comparing(Transaction::getTransactionDate).reversed())
                .map(transactionMapper::toTransactionResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TransactionResponseDTO feedAccount(Long userId, BigDecimal amount, String description) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("L'utilisateur n'existe pas"));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant du dépôt doit être supérieur à zéro.");
        }

        user.setAccountBalance(user.getAccountBalance().add(amount));

        Transaction transaction = createTransaction(null, user, amount, description);
        transactionRepository.save(transaction);

        return transactionMapper.toTransactionResponseDTO(transaction);
    }

    @Transactional
    public TransactionResponseDTO transfertToBank(Long userId, BigDecimal amount, String description) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("L'utilisateur n'existe pas"));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant du transfert doit être supérieur à zéro.");
        }
        if (user.getAccountBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Le solde est insuffisant pour le transfert");
        }
        BigDecimal feePercentage = new BigDecimal("0.005");
        BigDecimal feeAmount = amount.multiply(feePercentage).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalAmount = amount.add(feeAmount);

        user.setAccountBalance(user.getAccountBalance().subtract(totalAmount));

        Transaction transaction = createTransaction(user, null, amount, "Transfert vers Banque :" + description);
        transactionRepository.save(transaction);

        return transactionMapper.toTransactionResponseDTO(transaction);

    }
}
