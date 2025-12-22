package dev.marcos.ticketflow_api.dto.organization;

import java.util.UUID;

public record OrganizationSummaryResponse(
        UUID id,
        String name,
        String avatarUrl,
        String slug
) {
}
