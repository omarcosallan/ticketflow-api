package dev.marcos.ticketflow_api.dto.ticket;

import java.util.UUID;

public record TicketPurchaseMessage(
        UUID userId,
        UUID ticketTypeId,
        Integer quantity
) {
}
