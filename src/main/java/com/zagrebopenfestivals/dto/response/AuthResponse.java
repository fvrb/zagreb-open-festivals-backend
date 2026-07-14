package com.zagrebopenfestivals.dto.response;

public record AuthResponse(
        String accessToken,
        String tokenType,
        String username,
        String role
) {
    public static AuthResponse of(String accessToken, String username, String role) {
        return new AuthResponse(accessToken, "Bearer", username, role);
    }
}
