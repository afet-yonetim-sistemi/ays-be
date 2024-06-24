package org.ays.auth.model.mapper;

import org.ays.auth.model.AysUser;
import org.ays.auth.model.entity.AysUserEntity;
import org.ays.common.model.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * {@link AysUserToEntityMapper} is an interface that defines the mapping between an {@link AysUser} and an {@link AysUserEntity}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface AysUserToEntityMapper extends BaseMapper<AysUser, AysUserEntity> {

    @Override
    @Mapping(target = "countryCode", source = "phoneNumber.countryCode")
    @Mapping(target = "lineNumber", source = "phoneNumber.lineNumber")
    @Mapping(target = "institutionId", source = "institution.id")
    AysUserEntity map(AysUser user);

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static AysUserToEntityMapper initialize() {
        return Mappers.getMapper(AysUserToEntityMapper.class);
    }

}
