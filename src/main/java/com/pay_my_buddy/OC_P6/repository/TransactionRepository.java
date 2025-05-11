package com.pay_my_buddy.OC_P6.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pay_my_buddy.OC_P6.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Récupère toutes les transactions envoyées par un utilisateur
    List<Transaction> findBySenderId(Long senderId);

    // Récupère toutes les transactions reçues par un utilisateur
    List<Transaction> findByReceiverId(Long receiverId);

}
