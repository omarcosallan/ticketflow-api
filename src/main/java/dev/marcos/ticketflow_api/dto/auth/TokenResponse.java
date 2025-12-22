package dev.marcos.ticketflow_api.dto.auth;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
