package dev.marcos.ticketflow_api.service;

import dev.marcos.ticketflow_api.dto.organization.OrganizationRequestDTO;
import dev.marcos.ticketflow_api.dto.organization.OrganizationResponseDTO;
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
    public OrganizationResponseDTO save(OrganizationRequestDTO dto) {

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

        return organizationMapper.toDTO(savedOrg);
    }

    public List<OrganizationResponseDTO> listMyOrganizations() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID userId = user.getId();

        List<Organization> organizations = organizationRepository.findAllByUserId(userId);

        return organizations.stream()
                .map(organizationMapper::toDTO)
                .toList();
    }

    public OrganizationResponseDTO findById(UUID orgId) {
        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new NotFoundException("Organização não encontrada"));
        return organizationMapper.toDTO(org);
    }
}
