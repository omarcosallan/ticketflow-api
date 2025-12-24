package dev.marcos.ticketflow_api.mapper;

import dev.marcos.ticketflow_api.dto.ticket.TicketResponse;
import dev.marcos.ticketflow_api.entity.Ticket;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { EventMapper.class, TicketTypeMapper.class})
public interface TicketMapper {

    TicketResponse toDTO(Ticket ticket);
}
