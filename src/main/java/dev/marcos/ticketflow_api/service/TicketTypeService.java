package dev.marcos.ticketflow_api.service;

import dev.marcos.ticketflow_api.dto.ticketType.CreateTicketTypeRequest;
import dev.marcos.ticketflow_api.dto.ticketType.TicketTypeDetailResponse;
import dev.marcos.ticketflow_api.entity.Event;
import dev.marcos.ticketflow_api.entity.TicketType;
import dev.marcos.ticketflow_api.mapper.TicketTypeMapper;
import dev.marcos.ticketflow_api.repository.TicketTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketTypeService {

    private final TicketTypeRepository ticketTypeRepository;
    private final EventService eventService;
    private final TicketTypeMapper mapper;

    @Transactional
    public TicketTypeDetailResponse create(UUID orgId, UUID eventId, CreateTicketTypeRequest dto) {
        Event event = eventService.findEntityById(orgId, eventId);

        TicketType ticketType = new TicketType();
        ticketType.setName(dto.name());
        ticketType.setPrice(dto.price());
        ticketType.setTotalQuantity(dto.totalQuantity());
        ticketType.setSoldQuantity(0);
        ticketType.setEvent(event);

        TicketType ticketTypeSaved = ticketTypeRepository.save(ticketType);

        return mapper.toTicketTypeDetailDTO(ticketTypeSaved);
    }
}
