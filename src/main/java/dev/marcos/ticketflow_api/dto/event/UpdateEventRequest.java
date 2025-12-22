package dev.marcos.ticketflow_api.dto.event;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record UpdateEventRequest(
        String description,

        @Future(message = "A data deve ser no futuro")
        LocalDateTime startDateTime,

        @Future(message = "A data deve ser no futuro")
        LocalDateTime endDateTime,

        @NotBlank(message = "Local é obrigatório")
        String location,

        String bannerUrl
) {}
