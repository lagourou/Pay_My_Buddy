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

/**
 * Service pour la gestion des transactions.
 */
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final TransactionMapper transactionMapper;

    /**
     * Récupère les transactions envoyées par un utilisateur donné.
     *
     * @param senderId l'ID de l'expéditeur
     * @return la liste des transactions effectuées par l'expéditeur
     */
    public List<Transaction> getTransactionsBySenderId(Long senderId) {
        return transactionRepository.findBySenderId(senderId);
    }

    /**
     * Récupère les transactions reçues par un utilisateur donné.
     *
     * @param receiverId l'ID du destinataire
     * @return la liste des transactions reçues par le destinataire
     */
    public List<Transaction> getTransactionsByReceiverId(Long receiverId) {
        return transactionRepository.findByReceiverId(receiverId);
    }

    /**
     * Crée une nouvelle transaction entre un expéditeur et un destinataire.
     *
     * @param sender      l'expéditeur
     * @param receiver    le destinataire
     * @param amount      le montant de la transaction
     * @param description la description de la transaction
     * @return l'objet Transaction créé
     */
    private Transaction createTransaction(User sender, User receiver, BigDecimal amount, String description) {
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setTransactionDate(LocalDateTime.now());
        return transaction;
    }

    /**
     * Ajoute une nouvelle transaction entre deux utilisateurs.
     *
     * @param senderId      l'ID de l'expéditeur
     * @param receiverEmail l'email du destinataire
     * @param amount        le montant de la transaction
     * @param description   la description de la transaction
     * @return l'objet DTO représentant la transaction
     */
    @Transactional
    public TransactionResponseDTO addNewTransaction(Long senderId, String receiverEmail, BigDecimal amount,
            String description) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("L'expéditeur n'existe pas"));

        User receiver = userRepository.findByEmail(receiverEmail)
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

    /**
     * Récupère l'historique des transactions d'un utilisateur.
     *
     * @param userId l'ID de l'utilisateur
     * @return une liste des transactions associées à cet utilisateur
     */
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

    /**
     * Ajoute des fonds au compte d'un utilisateur.
     *
     * @param userId      l'ID de l'utilisateur
     * @param amount      le montant à ajouter
     * @param description la description du dépôt
     * @return l'objet DTO représentant la transaction
     */
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
}
