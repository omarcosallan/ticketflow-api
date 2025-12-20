package dev.marcos.ticketflow_api.dto.organization;

public record OrganizationRequestDTO(
        String name,
        String slug,
        String document,
        String contactEmail
) {
}
