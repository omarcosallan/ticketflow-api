package dev.marcos.ticketflow_api.dto.member;

import dev.marcos.ticketflow_api.dto.user.UserSummaryResponse;
import dev.marcos.ticketflow_api.entity.enums.OrgRole;

public record MemberResponse(
        UserSummaryResponse user,
        OrgRole role
) {
}
