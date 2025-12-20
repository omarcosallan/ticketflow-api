package dev.marcos.ticketflow_api.service;

import dev.marcos.ticketflow_api.entity.Member;
import dev.marcos.ticketflow_api.entity.User;
import dev.marcos.ticketflow_api.entity.enums.OrgRole;
import dev.marcos.ticketflow_api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service("orgGuard")
@RequiredArgsConstructor
public class OrganizationSecurityService {

    private final MemberRepository memberRepository;

    public boolean hasPermission(Authentication authentication, String orgId, String requiredRole) {

        User user = (User) authentication.getPrincipal();

        if (user == null) return false;

        if (user.getIsSystemAdmin()) return true;

        Optional<Member> memberOpt = memberRepository.findByUserIdAndOrganizationId(user.getId(), UUID.fromString(orgId));

        if (memberOpt.isEmpty()) return false;

        Member member = memberOpt.get();

        OrgRole userRole = member.getRole();
        OrgRole required = OrgRole.valueOf(requiredRole);

        return isRoleSufficient(userRole, required);
    }

    private boolean isRoleSufficient(OrgRole userRole, OrgRole requiredRole) {
        return userRole.ordinal() <= requiredRole.ordinal();
    }
}
