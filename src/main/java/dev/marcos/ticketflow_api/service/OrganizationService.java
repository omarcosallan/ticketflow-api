package dev.marcos.ticketflow_api.service;

import dev.marcos.ticketflow_api.dto.organization.CreateOrganizationRequest;
import dev.marcos.ticketflow_api.dto.organization.OrganizationDetailResponse;
import dev.marcos.ticketflow_api.dto.organization.OrganizationSummaryResponse;
import dev.marcos.ticketflow_api.dto.organization.UpdateOrganizationRequest;
import dev.marcos.ticketflow_api.entity.Member;
import dev.marcos.ticketflow_api.entity.Organization;
import dev.marcos.ticketflow_api.entity.User;
import dev.marcos.ticketflow_api.entity.enums.OrgRole;
import dev.marcos.ticketflow_api.exception.BusinessException;
import dev.marcos.ticketflow_api.exception.NotFoundException;
import dev.marcos.ticketflow_api.mapper.OrganizationMapper;
import dev.marcos.ticketflow_api.repository.MemberRepository;
import dev.marcos.ticketflow_api.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;
    private final MemberRepository memberRepository;

    @Transactional
    public OrganizationDetailResponse save(CreateOrganizationRequest dto) {

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (organizationRepository.existsBySlug(dto.slug())) {
            throw new BusinessException("Já existe uma organização com este slug");
        }

        Organization org = new Organization();
        org.setName(dto.name());
        org.setSlug(dto.slug());
        org.setDocument(dto.document());
        org.setContactEmail(dto.contactEmail());

        Organization savedOrg = organizationRepository.save(org);

        Member member = new Member();
        member.setUser(currentUser);
        member.setOrganization(savedOrg);
        member.setRole(OrgRole.OWNER);

        memberRepository.save(member);

        return organizationMapper.toOrgDetailDTO(savedOrg);
    }

    public List<OrganizationSummaryResponse> listMyOrganizations(User user) {
        List<Organization> organizations = organizationRepository.findAllByUserId(user.getId());
        return organizations.stream()
                .map(organizationMapper::toOrgSummaryDTO)
                .toList();
    }

    public OrganizationDetailResponse findById(UUID orgId) {
        Organization org = findEntityById(orgId);
        return organizationMapper.toOrgDetailDTO(org);
    }

    public Organization findEntityById(UUID orgId) {
        return organizationRepository.findById(orgId)
                .orElseThrow(() -> new NotFoundException("Organização não encontrada"));
    }

    @Transactional
    public OrganizationDetailResponse update(UUID orgId, UpdateOrganizationRequest dto) {
        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new NotFoundException("Organização não encontrada"));

        organizationMapper.updateEntityFromDto(dto, org);

        Organization savedOrg = organizationRepository.save(org);

        return organizationMapper.toOrgDetailDTO(savedOrg);
    }

    @Transactional
    public void delete(UUID orgId) {
        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new NotFoundException("Organização não encontrada"));

        organizationRepository.delete(org);
    }
}
