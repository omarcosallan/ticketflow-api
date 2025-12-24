package dev.marcos.ticketflow_api.service;

import dev.marcos.ticketflow_api.dto.event.*;
import dev.marcos.ticketflow_api.entity.Event;
import dev.marcos.ticketflow_api.entity.Organization;
import dev.marcos.ticketflow_api.entity.TicketType;
import dev.marcos.ticketflow_api.entity.enums.EventStatus;
import dev.marcos.ticketflow_api.exception.BusinessException;
import dev.marcos.ticketflow_api.exception.NotFoundException;
import dev.marcos.ticketflow_api.mapper.EventMapper;
import dev.marcos.ticketflow_api.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static dev.marcos.ticketflow_api.repository.specs.EventSpec.*;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final OrganizationService organizationService;
    private final EventMapper eventMapper;

    @Transactional
    public EventDetailResponse save(UUID orgId, CreateEventRequest dto) {

        if (!dto.startDateTime().isBefore(dto.endDateTime())) {
            throw new BusinessException("A data de início deve ser anterior à data de fim.");
        }

        boolean hasConflict = eventRepository.hasConflictingEvents(
                orgId,
                null,
                dto.startDateTime(),
                dto.endDateTime()
        );

        if (hasConflict) {
            throw new BusinessException("Já existe outro evento agendado neste intervalo de horário nesta organização");
        }

        Organization org = organizationService.findEntityById(orgId);

        Event event = eventMapper.toEntity(dto);
        event.setStatus(EventStatus.DRAFT);
        event.setOrganization(org);

        Event savedEvent = eventRepository.save(event);

        return eventMapper.toEventDetailDTO(savedEvent);
    }

    public List<EventSummaryResponse> listByOrganization(UUID orgId) {
        return eventRepository.findAllByOrganizationId(orgId).stream()
                .map(eventMapper::toEventSummaryDTO)
                .toList();
    }

    public List<EventSummaryResponse> findAll(int page,
                                       int size,
                                       String title,
                                       EventStatus status,
                                       LocalDateTime startDate,
                                       LocalDateTime endDate) {

        Specification<Event> specs = create(title, status, startDate, endDate);

        Pageable pageable = PageRequest.of(page, size);

        return eventRepository.findAll(specs, pageable).stream().map(eventMapper::toEventSummaryDTO).toList();
    }

    public EventDetailResponse findById(UUID eventId) {
        Event event = findEntityById(eventId);
        return eventMapper.toEventDetailDTO(event);
    }

    @Transactional
    public EventDetailResponse update(UUID orgId, UUID eventId, UpdateEventRequest dto) {
        if (!dto.startDateTime().isBefore(dto.endDateTime())) {
            throw new BusinessException("A data de início deve ser anterior à data de fim.");
        }

        boolean hasConflict = eventRepository.hasConflictingEvents(
                orgId,
                eventId,
                dto.startDateTime(),
                dto.endDateTime()
        );

        if (hasConflict) {
            throw new BusinessException("Já existe outro evento agendado neste intervalo de horário nesta organização");
        }

        Event event = findEntityById(orgId, eventId);

        eventMapper.updateEntityFromDto(dto, event);

        return eventMapper.toEventDetailDTO(eventRepository.save(event));
    }

    @Transactional
    public EventDetailResponse updateStatus(UUID orgId, UUID eventId, UpdateEventStatusRequest dto) {
        Event event = findEntityById(orgId, eventId);

        EventStatus newStatus = dto.status();

        if (event.getStatus() == EventStatus.CANCELLED && newStatus == EventStatus.PUBLISHED) {
            throw new BusinessException("Não é possível reativar um evento cancelado");
        }

        event.setStatus(newStatus);

        eventRepository.save(event);

        return eventMapper.toEventDetailDTO(event);
    }

    public EventDashboardResponse dashboard(UUID orgId, UUID eventId) {
        Event event = findEntityById(orgId, eventId);

        List<TicketType> ticketTypes = event.getTicketTypes() != null ? event.getTicketTypes() : List.<TicketType>of();

        long ticketSold = ticketTypes.stream()
                .mapToLong(TicketType::getSoldQuantity)
                .sum();

        long ticketsAvailable = ticketTypes.stream()
                .mapToLong(t -> t.getTotalQuantity() - t.getSoldQuantity())
                .sum();

        BigDecimal revenue = ticketTypes.stream()
                .map(t -> t.getPrice().multiply(BigDecimal.valueOf(t.getSoldQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new EventDashboardResponse(
                event.getTitle(),
                event.getStatus(),
                ticketSold,
                ticketsAvailable,
                revenue);
    }

    public Event findEntityById(UUID eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Evento não encontrado"));
    }

    public Event findEntityById(UUID orgId, UUID eventId) {
        return eventRepository.findByIdAndOrganizationId(eventId, orgId)
                .orElseThrow(() -> new NotFoundException("Evento não encontrado nesta organização"));
    }
}
