package dev.marcos.ticketflow_api.mapper;

import dev.marcos.ticketflow_api.dto.ticketType.TicketTypeDetailResponse;
import dev.marcos.ticketflow_api.entity.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface TicketTypeMapper {

    @Mapping(target = "available", source = ".", qualifiedByName = "isAvailable")
    TicketTypeDetailResponse toTicketTypeDetailDTO(TicketType ticketType);

    @Named("isAvailable")
    default boolean isAvailable(TicketType ticketType) {
        return ticketType.getSoldQuantity() < ticketType.getTotalQuantity();
    }
}
