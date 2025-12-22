package dev.marcos.ticketflow_api.dto.organization;

import dev.marcos.ticketflow_api.validator.DocumentValidator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateOrganizationRequest(
        @NotBlank(message = "Nome da organização é obrigatório")
        @Size(min = 3, message = "Nome deve ter no mínimo 3 caracteres")
        String name,

        @NotBlank(message = "Slug é obrigatório")
        @Size(min = 3, message = "Slug deve ter no mínimo 3 caracteres")
        @Pattern(regexp = "^[a-z0-9-]+$", message = "O slug deve conter apenas letras minúsculas, números e hífens")
        String slug,

        @DocumentValidator
        String document,

        @Email(message = "Formato de email inválido")
        String contactEmail
) {
}
