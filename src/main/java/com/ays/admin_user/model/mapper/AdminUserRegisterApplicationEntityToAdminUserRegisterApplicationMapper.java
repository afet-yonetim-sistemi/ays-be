package com.ays.admin_user.model.mapper;

import com.ays.admin_user.model.AdminUserRegisterApplication;
import com.ays.admin_user.model.entity.AdminUserRegisterApplicationEntity;
import com.ays.common.model.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * AdminUserRegisterApplicationEntityToAdminUserRegisterApplication is an interface that defines the mapping between an {@link AdminUserRegisterApplicationEntity} and an {@link AdminUserRegisterApplication}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface AdminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper extends BaseMapper<AdminUserRegisterApplicationEntity, AdminUserRegisterApplication> {

    @Override
    @Mapping(target = "adminUser.phoneNumber.countryCode", source = "adminUser.countryCode")
    @Mapping(target = "adminUser.phoneNumber.lineNumber", source = "adminUser.lineNumber")
    AdminUserRegisterApplication map(AdminUserRegisterApplicationEntity entity);

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static AdminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper initialize() {
        return Mappers.getMapper(AdminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper.class);
    }

}
