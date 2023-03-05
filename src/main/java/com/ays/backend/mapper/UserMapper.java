package com.ays.backend.mapper;

import com.ays.backend.user.model.entities.User;
import com.ays.backend.user.service.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User mapUserDTOtoUser(UserDTO userDTO);

    UserDTO mapUsertoUserDTO(User user);
}
