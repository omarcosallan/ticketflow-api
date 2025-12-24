package dev.marcos.ticketflow_api.dto.ticket;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PurchaseRequest(
        @NotNull UUID ticketTypeId,

        @Min(value = 1, message = "Mínimo de 1 ingresso")
        @Max(value = 10, message = "Máximo de 10 ingressos por vez")
        Integer quantity
) {
}
