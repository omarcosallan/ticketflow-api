package dev.marcos.ticketflow_api.dto.event;

import dev.marcos.ticketflow_api.entity.enums.EventStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventDTO(
        UUID id,
        String title,
        String description,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        String location,
        String bannerUrl,
        EventStatus status,
        UUID organizationId
) {
}
