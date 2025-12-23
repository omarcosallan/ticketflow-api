package dev.marcos.ticketflow_api.dto.ticketType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateTicketTypeRequest(
        @NotBlank(message = "Nome do lote é obrigatório")
        String name,

        @NotNull(message = "Preço é obrigatório")
        @DecimalMin(value = "0.1", message = "Preço não pode ser menor ou igual a R$ 0.0")
        BigDecimal price,

        @NotNull(message = "Quantidade é obrigatória")
        @Min(value = 1, message = "Quantidade deve ser pelo menos 1")
        Integer totalQuantity
) {
}
