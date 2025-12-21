package dev.marcos.ticketflow_api.mapper;

import dev.marcos.ticketflow_api.dto.event.EventCreateDTO;
import dev.marcos.ticketflow_api.dto.event.EventDTO;
import dev.marcos.ticketflow_api.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(source = "organization.id", target = "organizationId")
    EventDTO toDTO(Event event);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "ticketTypes", ignore = true)
    void updateEntityFromDto(EventCreateDTO dto, @MappingTarget Event entity);

    Event toEntity(EventCreateDTO dto);
}
