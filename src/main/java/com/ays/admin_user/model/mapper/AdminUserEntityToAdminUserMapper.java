package com.ays.admin_user.model.mapper;

import com.ays.admin_user.model.AdminUser;
import com.ays.admin_user.model.entity.AdminUserEntity;
import com.ays.common.model.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminUserEntityToAdminUserMapper extends BaseMapper<AdminUserEntity, AdminUser> {


    @Override
    @Mapping(target = "phoneNumber.countryCode", source = "source.countryCode")
    @Mapping(target = "phoneNumber.lineNumber", source = "source.lineNumber")
    AdminUser map(AdminUserEntity source);

    static AdminUserEntityToAdminUserMapper initialize() {
        return Mappers.getMapper(AdminUserEntityToAdminUserMapper.class);
    }
}
