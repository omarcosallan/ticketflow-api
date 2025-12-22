package dev.marcos.ticketflow_api.dto.event;

import dev.marcos.ticketflow_api.entity.enums.EventStatus;
import jakarta.validation.constraints.NotBlank;

public record EventStatusRequestDTO(
        @NotBlank EventStatus status
) {
}
