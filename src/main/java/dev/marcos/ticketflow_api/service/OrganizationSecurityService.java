package dev.marcos.ticketflow_api.service;

import dev.marcos.ticketflow_api.entity.User;
import dev.marcos.ticketflow_api.entity.enums.OrgRole;
import dev.marcos.ticketflow_api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("orgGuard")
@RequiredArgsConstructor
public class OrganizationSecurityService {

    private final MemberRepository memberRepository;

    public boolean hasPermission(Authentication authentication, String orgId, String requiredRole) {

        User user = (User) authentication.getPrincipal();

        if (user == null) return false;

        if (user.getIsSystemAdmin()) return true;

        return memberRepository.findRoleByUserIdAndOrgId(user.getId(), UUID.fromString(orgId))
                .map(currentRole -> hasAccess(currentRole, OrgRole.valueOf(requiredRole)))
                .orElse(false);
    }

    private boolean hasAccess(OrgRole userRole, OrgRole requiredRole) {
        return userRole.ordinal() <= requiredRole.ordinal();
    }
}
