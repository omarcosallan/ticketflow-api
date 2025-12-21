package dev.marcos.ticketflow_api.mapper;

import dev.marcos.ticketflow_api.dto.member.MemberDTO;
import dev.marcos.ticketflow_api.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "name")
    @Mapping(source = "user.email", target = "email")
    MemberDTO toDTO(Member member);
}
