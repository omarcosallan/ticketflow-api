package dev.marcos.ticketflow_api.dto.member;

import dev.marcos.ticketflow_api.entity.enums.OrgRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record MemberCreateDTO(
        @NotEmpty(message = "Email é obrigatório")
        @Email(message = "Formato de email inválido")
        String email,

        @NotEmpty(message = "Role é obrigatório")
        OrgRole role
) {
}
