package dev.marcos.ticketflow_api.mapper;

import dev.marcos.ticketflow_api.dto.user.UserDetailResponse;
import dev.marcos.ticketflow_api.dto.user.UserSummaryResponse;
import dev.marcos.ticketflow_api.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDetailResponse toUserDetailDTO(User user);

    UserSummaryResponse toUserSummaryDTO(User user);
}
