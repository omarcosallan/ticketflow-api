package dev.marcos.ticketflow_api.dto.organization;

import java.util.UUID;

public record OrganizationDetailResponse(
        UUID id,
        String name,
        String slug,
        String document,
        String contactEmail,
        String avatarUrl
) {
}
