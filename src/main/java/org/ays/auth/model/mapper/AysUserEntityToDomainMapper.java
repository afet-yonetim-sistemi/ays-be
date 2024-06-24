package org.ays.auth.model.mapper;

import org.ays.auth.model.AysUser;
import org.ays.auth.model.entity.AysUserEntity;
import org.ays.common.model.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * {@link AysUserEntityToDomainMapper} is an interface that defines the mapping between an {@link AysUserEntity} and an {@link AysUser}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface AysUserEntityToDomainMapper extends BaseMapper<AysUserEntity, AysUser> {

    @Override
    @Mapping(target = "phoneNumber.countryCode", source = "countryCode")
    @Mapping(target = "phoneNumber.lineNumber", source = "lineNumber")
    AysUser map(AysUserEntity userEntity);

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static AysUserEntityToDomainMapper initialize() {
        return Mappers.getMapper(AysUserEntityToDomainMapper.class);
    }

}
