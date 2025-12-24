package dev.marcos.ticketflow_api.repository;

import dev.marcos.ticketflow_api.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    List<Ticket> getTicketsByCustomerId(UUID id);
}
