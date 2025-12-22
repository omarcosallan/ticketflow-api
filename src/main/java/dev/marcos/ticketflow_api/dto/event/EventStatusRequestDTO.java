package dev.marcos.ticketflow_api.dto.event;

import dev.marcos.ticketflow_api.entity.enums.EventStatus;
import jakarta.validation.constraints.NotNull;

public record EventStatusRequestDTO(
        @NotNull EventStatus status
) {
}
