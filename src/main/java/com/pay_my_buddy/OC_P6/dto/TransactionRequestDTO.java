package com.pay_my_buddy.OC_P6.dto;

import java.math.BigDecimal;

import org.springframework.format.annotation.NumberFormat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDTO {

    @NotBlank
    @Email
    private String Email;

    @NotBlank
    @Size(min = 5, max = 50)
    private String description;

    @DecimalMin(value = "0.01")
    @Digits(integer = 10, fraction = 2)
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private BigDecimal amount;

}
