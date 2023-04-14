package com.ays.auth.model.mapper;

import com.ays.admin_user.model.entity.AdminUserEntity;
import com.ays.auth.model.AysUser;
import com.ays.common.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminUserEntityToAysUserMapper extends BaseMapper<AdminUserEntity, AysUser> {

    static AdminUserEntityToAysUserMapper initialize() {
        return Mappers.getMapper(AdminUserEntityToAysUserMapper.class);
    }

}
