package dev.marcos.ticketflow_api.mapper;

import dev.marcos.ticketflow_api.dto.ticketType.TicketTypeAdminResponse;
import dev.marcos.ticketflow_api.dto.ticketType.TicketTypeSummaryResponse;
import dev.marcos.ticketflow_api.entity.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface TicketTypeMapper {

    @Mapping(target = "remainingQuantity", source = ".", qualifiedByName = "remainingQuantity")
    TicketTypeSummaryResponse toPublicDTO(TicketType ticketType);

    @Mapping(target = "currentRevenue", source = ".", qualifiedByName = "currentRevenue")
    TicketTypeAdminResponse toAdminDTO(TicketType ticketType);

    @Named("remainingQuantity")
    default Integer remainingQuantity(TicketType ticketType) {
        return ticketType.getTotalQuantity() - ticketType.getSoldQuantity();
    }

    @Named("currentRevenue")
    default BigDecimal currentRevenue(TicketType ticketType) {
        return ticketType.getPrice().multiply(BigDecimal.valueOf(ticketType.getSoldQuantity()));
    }
}
