package dev.marcos.ticketflow_api.dto.auth;

import java.time.Instant;

public record RefreshTokenDTO(
        String refreshToken,
        Instant expiryDate
) {
}
