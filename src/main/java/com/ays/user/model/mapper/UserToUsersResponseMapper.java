package com.ays.user.model.mapper;

import com.ays.common.mapper.BaseMapper;
import com.ays.user.model.User;
import com.ays.user.model.dto.response.UsersResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserToUsersResponseMapper extends BaseMapper<User, UsersResponse> {

    static UserToUsersResponseMapper initialize() {
        return Mappers.getMapper(UserToUsersResponseMapper.class);
    }
}
