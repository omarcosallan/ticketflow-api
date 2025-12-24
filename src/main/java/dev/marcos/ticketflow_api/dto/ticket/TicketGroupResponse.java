package dev.marcos.ticketflow_api.dto.ticket;

import dev.marcos.ticketflow_api.dto.event.EventSummaryResponse;

import java.math.BigDecimal;
import java.util.List;

public record TicketGroupResponse(
        String ticketTypeName,
        BigDecimal pricePaid,
        Integer quantity,
        EventSummaryResponse event,
        List<TicketResponse> tickets
) {
}
