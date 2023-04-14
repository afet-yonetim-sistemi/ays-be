package com.ays.auth.model.mapper;

import com.ays.auth.model.AysUser;
import com.ays.common.mapper.BaseMapper;
import com.ays.user.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserEntityToAysUserMapper extends BaseMapper<UserEntity, AysUser> {

    static UserEntityToAysUserMapper initialize() {
        return Mappers.getMapper(UserEntityToAysUserMapper.class);
    }

}
