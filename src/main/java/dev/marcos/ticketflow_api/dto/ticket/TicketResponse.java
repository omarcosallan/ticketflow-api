package dev.marcos.ticketflow_api.dto.ticket;

import dev.marcos.ticketflow_api.dto.event.EventSummaryResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TicketResponse(
        UUID id,
        String code,
        String ticketTypeName,
        BigDecimal pricePaid,
        Boolean checkedIn,
        LocalDateTime checkedInAt,
        EventSummaryResponse event
) {
}
