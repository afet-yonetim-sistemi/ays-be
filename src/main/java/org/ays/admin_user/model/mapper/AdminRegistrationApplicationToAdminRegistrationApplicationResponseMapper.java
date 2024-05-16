package org.ays.admin_user.model.mapper;

import org.ays.admin_user.model.dto.response.AdminRegistrationApplicationResponse;
import org.ays.common.model.mapper.BaseMapper;
import org.ays.user.model.AdminRegistrationApplication;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


/**
 * AdminUserRegisterApplicationToAdminUserRegisterApplicationResponse is an interface that defines the mapping between an {@link AdminRegistrationApplication} and an {@link AdminRegistrationApplicationResponse}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface AdminRegistrationApplicationToAdminRegistrationApplicationResponseMapper extends BaseMapper<AdminRegistrationApplication, AdminRegistrationApplicationResponse> {

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static AdminRegistrationApplicationToAdminRegistrationApplicationResponseMapper initialize() {
        return Mappers.getMapper(AdminRegistrationApplicationToAdminRegistrationApplicationResponseMapper.class);
    }

}
