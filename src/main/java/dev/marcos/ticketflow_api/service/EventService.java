package dev.marcos.ticketflow_api.service;

import dev.marcos.ticketflow_api.dto.event.EventCreateDTO;
import dev.marcos.ticketflow_api.dto.event.EventDTO;
import dev.marcos.ticketflow_api.dto.event.EventDashboardDTO;
import dev.marcos.ticketflow_api.dto.event.EventStatusRequestDTO;
import dev.marcos.ticketflow_api.entity.Event;
import dev.marcos.ticketflow_api.entity.Organization;
import dev.marcos.ticketflow_api.entity.TicketType;
import dev.marcos.ticketflow_api.entity.enums.EventStatus;
import dev.marcos.ticketflow_api.exception.BusinessException;
import dev.marcos.ticketflow_api.exception.NotFoundException;
import dev.marcos.ticketflow_api.mapper.EventMapper;
import dev.marcos.ticketflow_api.repository.EventRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final OrganizationService organizationService;
    private final EventMapper eventMapper;

    @Transactional
    public EventDTO save(UUID orgId, EventCreateDTO dto) {

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

        return eventMapper.toDTO(savedEvent);
    }

    public List<EventDTO> listByOrganization(UUID orgId) {
        return eventRepository.findAllByOrganizationId(orgId).stream()
                .map(eventMapper::toDTO)
                .toList();
    }

    @Transactional
    public EventDTO update(UUID orgId, UUID eventId, EventCreateDTO dto) {
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

        return eventMapper.toDTO(eventRepository.save(event));
    }

    @Transactional
    public EventDTO updateStatus(UUID orgId, UUID eventId, @Valid EventStatusRequestDTO dto) {
        Event event = findEntityById(orgId, eventId);

        EventStatus newStatus = dto.status();

        if (event.getStatus() == EventStatus.CANCELLED && newStatus == EventStatus.PUBLISHED) {
            throw new BusinessException("Não é possível reativar um evento cancelado");
        }

        event.setStatus(newStatus);

        eventRepository.save(event);

        return eventMapper.toDTO(event);
    }

    public EventDashboardDTO dashboard(UUID orgId, UUID eventId) {
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

        return new EventDashboardDTO(
                event.getTitle(),
                event.getStatus(),
                ticketSold,
                ticketsAvailable,
                revenue);
    }

    private Event findEntityById(UUID orgId, UUID eventId) {
        return eventRepository.findByIdAndOrganizationId(eventId, orgId)
                .orElseThrow(() -> new NotFoundException("Evento não encontrado nesta organização"));
    }
}
