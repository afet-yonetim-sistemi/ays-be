package com.ays.backend.mapper;

import com.ays.backend.user.controller.payload.request.UpdateUserRequest;
import com.ays.backend.user.model.entities.User;
import com.ays.backend.user.service.dto.UserDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User mapUserDTOtoUser(UserDTO userDTO);

    UserDTO mapUsertoUserDTO(User user);

    @Mapping(target = "id", ignore = true)
    User mapUpdateRequestToUser(UpdateUserRequest updateUserRequest, @MappingTarget User user);

}
