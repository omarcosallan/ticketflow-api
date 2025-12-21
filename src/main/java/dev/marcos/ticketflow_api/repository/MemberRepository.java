package dev.marcos.ticketflow_api.repository;

import dev.marcos.ticketflow_api.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {

    boolean existsByUserIdAndOrganizationId(UUID userId, UUID organizationId);
    Optional<Member> findByUserIdAndOrganizationId(UUID userId, UUID orgId);
}
