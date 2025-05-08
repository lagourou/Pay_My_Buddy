package com.pay_my_buddy.OC_P6.dto;

import java.math.BigDecimal;

import org.springframework.format.annotation.NumberFormat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountBalanceRequestDTO {

    @DecimalMin(value = "0.1", inclusive = true)
    @Digits(integer = 10, fraction = 1)
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private BigDecimal amount;

}
