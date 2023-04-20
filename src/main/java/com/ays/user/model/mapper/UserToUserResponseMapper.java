package com.ays.user.model.mapper;

import com.ays.common.model.mapper.BaseMapper;
import com.ays.user.model.User;
import com.ays.user.model.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserToUserResponseMapper extends BaseMapper<User, UserResponse> {

    static UserToUserResponseMapper initialize() {
        return Mappers.getMapper(UserToUserResponseMapper.class);
    }
}
