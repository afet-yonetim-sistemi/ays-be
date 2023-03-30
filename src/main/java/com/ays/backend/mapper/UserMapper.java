package com.ays.backend.mapper;

import com.ays.backend.user.controller.payload.request.UpdateUserRequest;
import com.ays.backend.user.model.User;
import com.ays.backend.user.model.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
// TODO : Use BaseMapper
// extends BaseMapper<User, UserDTO>
public interface UserMapper {

    UserEntity mapUserToUserEntity(User user);

    User mapUserEntityToUser(UserEntity userEntity);

    @Mapping(target = "id", ignore = true)
    com.ays.backend.user.model.entities.UserEntity mapUpdateRequestToUser(UpdateUserRequest updateUserRequest, @MappingTarget com.ays.backend.user.model.entities.UserEntity user);

}
