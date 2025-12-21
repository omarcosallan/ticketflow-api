package dev.marcos.ticketflow_api.service;

import dev.marcos.ticketflow_api.dto.member.MemberCreateDTO;
import dev.marcos.ticketflow_api.dto.member.MemberDTO;
import dev.marcos.ticketflow_api.entity.Member;
import dev.marcos.ticketflow_api.entity.Organization;
import dev.marcos.ticketflow_api.entity.User;
import dev.marcos.ticketflow_api.entity.enums.OrgRole;
import dev.marcos.ticketflow_api.exception.BusinessException;
import dev.marcos.ticketflow_api.exception.NotFoundException;
import dev.marcos.ticketflow_api.mapper.MemberMapper;
import dev.marcos.ticketflow_api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final UserService userService;
    private final OrganizationService organizationService;
    private final MemberMapper memberMapper;

    @Transactional
    public MemberDTO addMember(UUID orgId, MemberCreateDTO dto) {
        Organization org = organizationService.findEntityById(orgId);

        User userToAdd = userService.loadUserByUsername(dto.email());

        if (memberRepository.existsByUserIdAndOrganizationId(userToAdd.getId(), orgId)) {
            throw new BusinessException("Este usuário já é membro desta organização");
        }

        Member newMember = new Member();
        newMember.setOrganization(org);
        newMember.setUser(userToAdd);
        if (dto.role() != OrgRole.OWNER) {
            newMember.setRole(dto.role());
        } else {
            newMember.setRole(OrgRole.ADMIN);
        }

        Member memberSaved = memberRepository.save(newMember);

        return memberMapper.toDTO(memberSaved);
    }

    @Transactional
    public void removeMember(UUID orgId, UUID userIdToRemove) {
        Member member = memberRepository.findByUserIdAndOrganizationId(userIdToRemove, orgId)
                .orElseThrow(() -> new NotFoundException("Membro não encontrado nesta organização"));

        if (member.getRole() == OrgRole.OWNER) {
            throw new BusinessException("Não é possível remover o dono da organização");
        }

        memberRepository.delete(member);
    }

    public List<MemberDTO> findAllByOrg(UUID orgId) {
        Organization org = organizationService.findEntityById(orgId);

        return org.getMembers()
                .stream()
                .map(memberMapper::toDTO)
                .toList();
    }
}
