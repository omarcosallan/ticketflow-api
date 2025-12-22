package dev.marcos.ticketflow_api.dto.event;

import dev.marcos.ticketflow_api.entity.enums.EventStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateEventStatusRequest(
        @NotNull(message = "O status é obrigatório")
        EventStatus status
) {
}
