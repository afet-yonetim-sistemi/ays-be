package com.ays.admin_user.model.mapper;

import com.ays.admin_user.model.entity.AdminUserEntity;
import com.ays.auth.model.AysUser;
import com.ays.common.model.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * AdminUserEntityToAysUserMapper is an interface that defines the mapping between an {@link AdminUserEntity} and an {@link AysUser}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface AdminUserEntityToAysUserMapper extends BaseMapper<AdminUserEntity, AysUser> {

    static AdminUserEntityToAysUserMapper initialize() {
        return Mappers.getMapper(AdminUserEntityToAysUserMapper.class);
    }

}
