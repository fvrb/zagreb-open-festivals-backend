package com.zagrebopenfestivals.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "Username je obavezan")
        String username,

        @NotBlank(message = "Password je obavezan")
        String password
) {
}
