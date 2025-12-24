package dev.marcos.ticketflow_api.dto.ticket;

import java.time.LocalDateTime;
import java.util.UUID;

public record TicketResponse(
        UUID id,
        String code,
        Boolean checkedIn,
        LocalDateTime checkedInAt
) {
}
