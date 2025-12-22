package dev.marcos.ticketflow_api.mapper;

import dev.marcos.ticketflow_api.dto.member.MemberResponse;
import dev.marcos.ticketflow_api.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    MemberResponse toDTO(Member member);
}
