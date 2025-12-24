package dev.marcos.ticketflow_api.mapper;

import dev.marcos.ticketflow_api.dto.ticket.TicketResponse;
import dev.marcos.ticketflow_api.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { EventMapper.class, TicketTypeMapper.class})
public interface TicketMapper {

    @Mapping(target = "ticketTypeName", source = "ticketType.name")
    @Mapping(target = "pricePaid", source = "ticketType.price")
    TicketResponse toDTO(Ticket ticket);
}
