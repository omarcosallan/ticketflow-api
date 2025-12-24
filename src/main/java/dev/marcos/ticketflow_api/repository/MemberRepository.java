package dev.marcos.ticketflow_api.repository;

import dev.marcos.ticketflow_api.entity.Member;
import dev.marcos.ticketflow_api.entity.enums.OrgRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {

    boolean existsByUserIdAndOrganizationId(UUID userId, UUID organizationId);

    Optional<Member> findByUserIdAndOrganizationId(UUID userId, UUID orgId);

    @Query("SELECT m.role FROM Member m WHERE m.user.id = :userId AND m.organization.id = :orgId")
    Optional<OrgRole> findRoleByUserIdAndOrgId(@Param("userId") UUID userId, @Param("orgId") UUID orgId);
}
