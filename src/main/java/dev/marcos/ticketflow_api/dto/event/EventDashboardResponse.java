package dev.marcos.ticketflow_api.dto.event;

import dev.marcos.ticketflow_api.entity.enums.EventStatus;

import java.math.BigDecimal;

public record EventDashboardResponse(
        String eventName,
        EventStatus status,
        long ticketsSold,
        long ticketsAvailable,
        BigDecimal revenue
) {
}
