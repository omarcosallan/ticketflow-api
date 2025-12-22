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

import java.math.BigDecimal;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventDetailResponse toEventDetailDTO(Event event);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "ticketTypes", ignore = true)
    void updateEntityFromDto(UpdateEventRequest dto, @MappingTarget Event entity);

    Event toEntity(CreateEventRequest dto);

    default EventSummaryResponse toEventSummaryDTO(Event event) {
        BigDecimal lowestTicketPrice = event.getTicketTypes().stream()
                .map(TicketType::getPrice)
                .filter(Objects::nonNull)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        return new EventSummaryResponse(
                event.getId(),
                event.getTitle(),
                event.getStartDateTime(),
                event.getLocation(),
                event.getBannerUrl(),
                lowestTicketPrice
        );
    }
}
