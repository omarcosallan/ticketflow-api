package dev.marcos.ticketflow_api.service;

import dev.marcos.ticketflow_api.dto.ticket.TicketGroupResponse;
import dev.marcos.ticketflow_api.dto.ticket.TicketResponse;
import dev.marcos.ticketflow_api.entity.Ticket;
import dev.marcos.ticketflow_api.entity.TicketType;
import dev.marcos.ticketflow_api.entity.User;
import dev.marcos.ticketflow_api.mapper.EventMapper;
import dev.marcos.ticketflow_api.mapper.TicketMapper;
import dev.marcos.ticketflow_api.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static dev.marcos.ticketflow_api.repository.specs.TicketSpec.*;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper mapper;
    private final EventMapper eventMapper;

    public List<TicketGroupResponse> findAll(User user, int page, int size, UUID eventId) {

        Pageable pageable = PageRequest.of(page, size);

        Specification<Ticket> spec = create(user.getId(), eventId);

        List<Ticket> allTickets = ticketRepository.findAll(spec, pageable).getContent();

        Map<TicketType, List<Ticket>> groupedMap = allTickets.stream()
                .collect(Collectors.groupingBy(Ticket::getTicketType));

        return groupedMap.entrySet().stream()
                .map(entry -> {
                    TicketType type = entry.getKey();
                    List<Ticket> tickets = entry.getValue();
                    Ticket firstTicket = tickets.get(0);

                    List<TicketResponse> items = tickets.stream()
                            .map(t -> new TicketResponse(
                                    t.getId(),
                                    t.getCode(),
                                    t.isCheckedIn(),
                                    t.getCheckedInAt()
                            ))
                            .toList();

                    return new TicketGroupResponse(
                            type.getName(),
                            type.getPrice(),
                            tickets.size(),
                            eventMapper.toEventSummaryDTO(firstTicket.getEvent()),
                            items
                    );
                })
                .toList();
    }
}
