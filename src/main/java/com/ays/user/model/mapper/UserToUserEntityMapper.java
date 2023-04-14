package com.ays.user.model.mapper;

import com.ays.common.mapper.BaseMapper;
import com.ays.user.model.User;
import com.ays.user.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserToUserEntityMapper extends BaseMapper<User, UserEntity> {

    static UserToUserEntityMapper initialize() {
        return Mappers.getMapper(UserToUserEntityMapper.class);
    }
}
