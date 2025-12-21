package dev.marcos.ticketflow_api.mapper;

import dev.marcos.ticketflow_api.dto.organization.OrganizationDTO;
import dev.marcos.ticketflow_api.dto.organization.OrganizationUpdateDTO;
import dev.marcos.ticketflow_api.entity.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OrganizationMapper {

    OrganizationDTO toDTO(Organization organization);
    void updateEntityFromDto(OrganizationUpdateDTO dto, @MappingTarget Organization entity);
}
