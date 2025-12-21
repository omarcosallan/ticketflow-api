package dev.marcos.ticketflow_api.dto.member;

import dev.marcos.ticketflow_api.entity.enums.OrgRole;

import java.util.UUID;

public record MemberDTO(
        UUID userId,
        String name,
        String email,
        OrgRole role
) {
}
