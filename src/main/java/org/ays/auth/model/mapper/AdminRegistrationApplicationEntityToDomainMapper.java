package org.ays.auth.model.mapper;

import org.ays.auth.model.AdminRegistrationApplication;
import org.ays.auth.model.entity.AdminRegistrationApplicationEntity;
import org.ays.common.model.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * {@link AdminRegistrationApplicationEntityToDomainMapper} is an interface that defines the mapping between an {@link AdminRegistrationApplicationEntity} and an {@link AdminRegistrationApplication}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface AdminRegistrationApplicationEntityToDomainMapper extends BaseMapper<AdminRegistrationApplicationEntity, AdminRegistrationApplication> {

    @Override
    @Mapping(target = "user.phoneNumber.countryCode", source = "user.countryCode")
    @Mapping(target = "user.phoneNumber.lineNumber", source = "user.lineNumber")
    AdminRegistrationApplication map(AdminRegistrationApplicationEntity entity);

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static AdminRegistrationApplicationEntityToDomainMapper initialize() {
        return Mappers.getMapper(AdminRegistrationApplicationEntityToDomainMapper.class);
    }

}
