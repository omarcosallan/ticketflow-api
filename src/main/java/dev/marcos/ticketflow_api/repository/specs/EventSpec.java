package dev.marcos.ticketflow_api.repository.specs;

import dev.marcos.ticketflow_api.entity.Event;
import dev.marcos.ticketflow_api.entity.enums.EventStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class EventSpec {

    public static Specification<Event> create(String title, EventStatus status, LocalDateTime startDate, LocalDateTime endDate) {
        return Specification.allOf(
                title(title),
                status(status),
                startDate(startDate),
                endDate(endDate)
        );
    }

    private static Specification<Event> title(String title) {
        if (title == null) return null;
        return (root, query, builder) -> builder.equal(root.get("title"), title);
    }

    private static Specification<Event> status(EventStatus status) {
        if (status == null) return null;
        return (root, query, builder) -> builder.equal(root.get("status"), status);
    }

    private static Specification<Event> startDate(LocalDateTime startDate) {
        if (startDate == null) return null;
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("startDateTime"), startDate);
    }

    private static Specification<Event> endDate(LocalDateTime endDate) {
        if (endDate == null) return null;
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("endDateTime"), endDate);
    }
}
