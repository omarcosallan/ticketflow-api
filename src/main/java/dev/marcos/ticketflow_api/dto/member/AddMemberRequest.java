package dev.marcos.ticketflow_api.dto.member;

import dev.marcos.ticketflow_api.entity.enums.OrgRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AddMemberRequest(
        @NotEmpty(message = "Email é obrigatório")
        @Email(message = "Formato de email inválido")
        String email,

        @NotNull(message = "Role é obrigatório")
        OrgRole role
) {
}
