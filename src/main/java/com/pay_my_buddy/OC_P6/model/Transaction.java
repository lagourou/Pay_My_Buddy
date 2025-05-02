package com.pay_my_buddy.OC_P6.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "transaction")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sender", nullable = false)
    private User sender;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "receiver", nullable = false)
    private User receiver;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "transaction_date", nullable = false, updatable = false)
    private LocalDateTime transactionDate = LocalDateTime.now();

}
