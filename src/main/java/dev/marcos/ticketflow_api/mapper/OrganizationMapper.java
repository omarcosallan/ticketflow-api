package dev.marcos.ticketflow_api.mapper;

import dev.marcos.ticketflow_api.dto.organization.OrganizationDetailResponse;
import dev.marcos.ticketflow_api.dto.organization.OrganizationSummaryResponse;
import dev.marcos.ticketflow_api.dto.organization.UpdateOrganizationRequest;
import dev.marcos.ticketflow_api.entity.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OrganizationMapper {

    OrganizationSummaryResponse toOrgSummaryDTO(Organization organization);

    OrganizationDetailResponse toOrgDetailDTO(Organization organization);

    void updateEntityFromDto(UpdateOrganizationRequest dto, @MappingTarget Organization entity);
}
