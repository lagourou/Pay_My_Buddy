package com.pay_my_buddy.OC_P6.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDTO {

    private Long id;
    private String description;
    private BigDecimal amount;
    private LocalDateTime transactionDate;

    private Long senderId;
    private String senderUsername;
    private String senderEmail;
    private BigDecimal senderBalance;

    private Long receiverId;
    private String receiverUsername;
    private String receiverEmail;


}
