package dev.marcos.ticketflow_api.repository;

import dev.marcos.ticketflow_api.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {

    boolean existsBySlug(String slug);

    @Query("""
        SELECT DISTINCT o
        FROM Organization o
        JOIN o.members m
        WHERE m.user.id = :userId
    """)
    List<Organization> findAllByUserId(@Param("userId") UUID userId);
}
