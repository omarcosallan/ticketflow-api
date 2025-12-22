package dev.marcos.ticketflow_api.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserSummaryResponse(
        UUID id,
        String name,
        String email,
        String avatarUrl
) {
}
