package dev.marcos.ticketflow_api.dto.event;

import dev.marcos.ticketflow_api.dto.organization.OrganizationSummaryResponse;
import dev.marcos.ticketflow_api.dto.ticketType.TicketTypeSummaryResponse;
import dev.marcos.ticketflow_api.entity.enums.EventStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record EventDetailResponse(
        UUID id,
        String title,
        String description,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        String location,
        String bannerUrl,
        EventStatus status,
        OrganizationSummaryResponse organization,
        List<TicketTypeSummaryResponse> tickets
) {
}
