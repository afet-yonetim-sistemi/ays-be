package com.ays.user.model.mapper;

import com.ays.common.mapper.BaseMapper;
import com.ays.user.controller.dto.response.UserResponse;
import com.ays.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserToUserResponseMapper extends BaseMapper<User, UserResponse> {

    static UserToUserResponseMapper initialize() {
        return Mappers.getMapper(UserToUserResponseMapper.class);
    }
}
