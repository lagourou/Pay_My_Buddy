package com.pay_my_buddy.OC_P6.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {

    @NotBlank(message ="Le nom d'utilisateur ne doit pas être voide")
    @Size(min = 3, max = 50, message = "Le nom d'utilisateur doit contenir entre 3 et 50 caractères")
    private String username;

    @NotBlank(message = "L'email ne doit pas être vide")
    @Email(message = "L'email doit être valide")
    private String email;

    @Pattern(regexp = "^$|[a-zA-Z0-9]{8,20}",
            message = "Le mot de passe doit contenir entre 8 et 20 caractères alphanumériques ")
    private String password;
    

}
