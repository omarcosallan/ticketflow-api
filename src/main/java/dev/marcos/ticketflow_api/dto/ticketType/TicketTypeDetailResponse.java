package dev.marcos.ticketflow_api.dto.ticketType;

import java.math.BigDecimal;
import java.util.UUID;

public record TicketTypeDetailResponse(
        UUID id,
        String name,
        BigDecimal price,
        Integer totalQuantity,
        Integer soldQuantity,
        boolean available
) {
}
