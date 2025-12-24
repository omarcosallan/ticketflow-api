package dev.marcos.ticketflow_api.dto.ticketType;

import java.math.BigDecimal;
import java.util.UUID;

public record TicketTypeSummaryResponse(
        UUID id,
        String name,
        BigDecimal price,
        Integer remainingQuantity
) {
}
