package com.zagrebopenfestivals.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(message = "Username je obavezan")
        @Size(min = 3, max = 50, message = "Username mora imati 3-50 znakova")
        String username,

        @NotBlank(message = "Email je obavezan")
        @Email(message = "Email nije ispravnog formata")
        String email,

        @NotBlank(message = "Password je obavezan")
        @Size(min = 6, message = "Password mora imati barem 6 znakova")
        String password
) {
}
