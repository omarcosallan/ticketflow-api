package dev.marcos.ticketflow_api.dto.ticketType;

import java.math.BigDecimal;
import java.util.UUID;

public record TicketTypeAdminResponse(
        UUID id,
        String name,
        BigDecimal price,
        Integer totalQuantity,
        Integer soldQuantity,
        BigDecimal currentRevenue
) {
}
