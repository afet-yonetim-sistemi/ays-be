package org.ays.admin_user.model.mapper;

import org.ays.admin_user.model.AdminUser;
import org.ays.admin_user.model.entity.AdminUserEntity;
import org.ays.common.model.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * AdminUserEntityToAdminUserMapper is an interface that defines the
 * mapping between an {@link AdminUserEntity} and an {@link AdminUser}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface AdminUserEntityToAdminUserMapper extends BaseMapper<AdminUserEntity, AdminUser> {


    @Override
    @Mapping(target = "phoneNumber.countryCode", source = "countryCode")
    @Mapping(target = "phoneNumber.lineNumber", source = "lineNumber")
    AdminUser map(AdminUserEntity source);

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static AdminUserEntityToAdminUserMapper initialize() {
        return Mappers.getMapper(AdminUserEntityToAdminUserMapper.class);
    }

}
