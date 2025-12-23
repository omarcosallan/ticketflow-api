package dev.marcos.ticketflow_api.mapper;

import dev.marcos.ticketflow_api.dto.event.CreateEventRequest;
import dev.marcos.ticketflow_api.dto.event.EventDetailResponse;
import dev.marcos.ticketflow_api.dto.event.EventSummaryResponse;
import dev.marcos.ticketflow_api.dto.event.UpdateEventRequest;
import dev.marcos.ticketflow_api.entity.Event;
import dev.marcos.ticketflow_api.entity.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.Objects;

@Mapper(componentModel = "spring", uses = { TicketTypeMapper.class })
public interface EventMapper {

    @Mapping(target = "tickets", source = "ticketTypes")
    EventDetailResponse toEventDetailDTO(Event event);

    @Mapping(target = "lowestTicketPrice", source = ".", qualifiedByName = "lowestTicketPrice")
    EventSummaryResponse toEventSummaryDTO(Event event);

    Event toEntity(CreateEventRequest dto);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "ticketTypes", ignore = true)
    void updateEntityFromDto(UpdateEventRequest dto, @MappingTarget Event entity);

    @Named("lowestTicketPrice")
    default BigDecimal lowestTicketPrice(Event event) {
        return event.getTicketTypes().stream()
                .map(TicketType::getPrice)
                .filter(Objects::nonNull)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }
}
