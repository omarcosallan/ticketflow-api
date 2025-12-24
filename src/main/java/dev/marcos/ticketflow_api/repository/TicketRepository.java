package dev.marcos.ticketflow_api.repository;

import dev.marcos.ticketflow_api.entity.Ticket;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID>, JpaSpecificationExecutor<Ticket> {

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"event", "ticketType", "customer"})
    Page<Ticket> findAll(Specification<Ticket> spec, @NonNull Pageable pageable);
}
