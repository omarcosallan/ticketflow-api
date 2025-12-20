package dev.marcos.ticketflow_api.dto.organization;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrganizationResponseDTO(
        UUID id,
        String name,
        String slug,
        String document,
        String contactEmail,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate
) {
}
