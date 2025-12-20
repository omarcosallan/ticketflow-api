package dev.marcos.ticketflow_api.mapper;

import dev.marcos.ticketflow_api.dto.organization.OrganizationResponseDTO;
import dev.marcos.ticketflow_api.entity.Organization;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {

    OrganizationResponseDTO toDTO(Organization organization);
}
