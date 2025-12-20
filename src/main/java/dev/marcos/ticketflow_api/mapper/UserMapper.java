package dev.marcos.ticketflow_api.mapper;

import dev.marcos.ticketflow_api.dto.auth.UserResponseDTO;
import dev.marcos.ticketflow_api.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDTO toDTO(User user);
}
