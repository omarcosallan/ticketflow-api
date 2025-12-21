package dev.marcos.ticketflow_api.dto.organization;

import dev.marcos.ticketflow_api.validator.DocumentValidator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record OrganizationCreateDTO(
        @NotEmpty(message = "Nome da organização é obrigatório")
        @Size(min = 6, message = "Nome deve ter no mínimo 3 caracteres")
        String name,

        @NotEmpty(message = "Slug é obrigatório")
        @Size(min = 6, message = "Slug deve ter no mínimo 3 caracteres")
        String slug,

        @DocumentValidator
        String document,

        @Email(message = "Formato de email inválido")
        String contactEmail
) {
}
