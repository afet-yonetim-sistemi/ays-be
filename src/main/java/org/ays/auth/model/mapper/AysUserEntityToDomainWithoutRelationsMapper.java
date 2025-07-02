package org.ays.auth.model.mapper;

import org.ays.auth.model.AysUser;
import org.ays.auth.model.entity.AysUserEntity;
import org.ays.common.model.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for converting {@link AysUserEntity} to {@link AysUser} domain objects without including
 * related entities such as roles, institution, and login attempt.
 * <p>
 * This interface uses MapStruct to generate the implementation at compile time. It defines a custom mapping
 * configuration that excludes sensitive or unnecessary fields for certain use cases.
 * </p>
 * <ul>
 *   <li>Maps {@code countryCode} and {@code lineNumber} to {@code phoneNumber}.</li>
 *   <li>Ignores {@code roles}, {@code institution}, {@code password}, and {@code loginAttempt} fields.</li>
 * </ul>
 *
 * <p>Implements the {@link BaseMapper} interface which defines the generic map method signature.</p>
 */
@Mapper
public interface AysUserEntityToDomainWithoutRelationsMapper extends BaseMapper<AysUserEntity, AysUser> {

    /**
     * Maps an {@link AysUserEntity} to an {@link AysUser} excluding related entities and sensitive fields.
     *
     * @param userEntity the user entity to be mapped
     * @return a mapped {@link AysUser} object without roles, institution, password, or login attempt
     */
    @Override
    @Mapping(target = "phoneNumber.countryCode", source = "countryCode")
    @Mapping(target = "phoneNumber.lineNumber", source = "lineNumber")
    @Mapping(target = "roles", source = "roles", ignore = true)
    @Mapping(target = "institution", source = "institution", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "loginAttempt", ignore = true)
    AysUser map(AysUserEntity userEntity);

    /**
     * Initializes and returns the MapStruct-generated implementation of this mapper.
     *
     * @return the mapper implementation instance
     */
    static AysUserEntityToDomainWithoutRelationsMapper initialize() {
        return Mappers.getMapper(AysUserEntityToDomainWithoutRelationsMapper.class);
    }

}
