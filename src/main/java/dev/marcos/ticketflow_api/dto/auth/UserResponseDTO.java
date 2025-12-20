package dev.marcos.ticketflow_api.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponseDTO(
        UUID id,
        String name,
        String email,
        LocalDate dateOfBirth,
        String avatarUrl,
        Boolean isSystemAdmin,
        String provider,
        String googleId,
        Boolean emailVerified
) {
}
