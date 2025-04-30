package com.pay_my_buddy.OC_P6.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountBalanceRequestDTO {

    @NotNull(message = "Le solde ne doit pas être null")
    @Positive(message = "Le solde doit être positif")
    private BigDecimal account_balance;

}
