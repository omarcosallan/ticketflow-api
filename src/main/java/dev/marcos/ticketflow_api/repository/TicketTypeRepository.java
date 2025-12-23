package dev.marcos.ticketflow_api.repository;

import dev.marcos.ticketflow_api.entity.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TicketTypeRepository extends JpaRepository<TicketType, UUID> {
    List<TicketType> findAllByEventId(UUID eventId);
}
