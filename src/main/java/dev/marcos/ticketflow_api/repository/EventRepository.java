package dev.marcos.ticketflow_api.repository;

import dev.marcos.ticketflow_api.entity.Event;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID>, JpaSpecificationExecutor<Event> {

    @Override
    @NonNull
    @EntityGraph(attributePaths = { "ticketTypes" })
    Page<Event> findAll(Specification<Event> spec, @NonNull Pageable pageable);

    List<Event> findAllByOrganizationId(UUID orgId);

    Optional<Event> findByIdAndOrganizationId(UUID eventId, UUID orgId);

    @Query("""
                SELECT count(e) > 0 FROM Event e
                WHERE e.organization.id = :orgId
                AND (:eventId IS NULL OR e.id != :eventId)
                AND (e.startDateTime < :endDateTime AND e.endDateTime > :startDateTime)
            """)
    boolean hasConflictingEvents(
            @Param("orgId") UUID orgId,
            @Param("eventId") UUID eventId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );
}
