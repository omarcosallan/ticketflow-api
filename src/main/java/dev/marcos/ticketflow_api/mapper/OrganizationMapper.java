package dev.marcos.ticketflow_api.mapper;

import dev.marcos.ticketflow_api.dto.organization.OrganizationDTO;
import dev.marcos.ticketflow_api.entity.Organization;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {

    OrganizationDTO toDTO(Organization organization);
}
