package dev.marcos.ticketflow_api.dto.auth;

public record AuthResponseDTO(
        String token,
        String refreshToken
) {
}
