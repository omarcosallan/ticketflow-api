package dev.marcos.ticketflow_api.dto.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record EventSummaryResponse(
        UUID id,
        String title,
        LocalDateTime startDateTime,
        String location,
        String bannerUrl,
        BigDecimal lowestTicketPrice
) {
}
