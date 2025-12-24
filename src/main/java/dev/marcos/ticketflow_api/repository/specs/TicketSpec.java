package dev.marcos.ticketflow_api.repository.specs;

import dev.marcos.ticketflow_api.entity.Ticket;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class TicketSpec {

    public static Specification<Ticket> create(UUID ownerId, UUID eventId) {
        return Specification.allOf(
                owner(ownerId),
                event(eventId)
        );
    }

    private static Specification<Ticket> owner(UUID ownerId) {
        if (ownerId == null) return null;
        return (root, query, builder) -> builder.equal(root.get("customer").get("id"), ownerId);
    }

    private static Specification<Ticket> event(UUID eventId) {
        if (eventId == null) return null;
        return (root, query, builder) -> builder.equal(root.get("event").get("id"), eventId);
    }
}
