package dev.marcos.ticketflow_api.dto.organization;

import dev.marcos.ticketflow_api.validator.DocumentValidator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record OrganizationUpdateDTO(
        @Size(min = 3, message = "Nome deve ter no mínimo 3 caracteres")
        String name,

        @DocumentValidator
        String document,

        @Email(message = "Formato de email inválido")
        String contactEmail
) {
}
