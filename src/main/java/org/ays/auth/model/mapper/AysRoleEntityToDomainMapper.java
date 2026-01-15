package org.ays.auth.model.mapper;

import org.ays.auth.model.AysRole;
import org.ays.auth.model.entity.AysRoleEntity;
import org.ays.common.model.mapper.BaseMapper;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * {@link AysRoleEntityToDomainMapper} is an interface that defines the mapping between an {@link AysRoleEntity} and an {@link AysRole}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface AysRoleEntityToDomainMapper extends BaseMapper<AysRoleEntity, AysRole> {

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static AysRoleEntityToDomainMapper initialize() {
        return Mappers.getMapper(AysRoleEntityToDomainMapper.class);
    }

    /**
     * Maps an {@link AysRoleEntity} object to an {@link AysRole} object while excluding specific fields.
     * This mapping is primarily intended for summarizing the role information, with the "permissions" field ignored.
     *
     * @param entity the {@link AysRoleEntity} object to be mapped.
     * @return the mapped {@link AysRole} object containing summarized role data.
     */
    @Named("toSummary")
    @Mapping(target = "permissions", ignore = true)
    AysRole mapForSummary(AysRoleEntity entity);

    /**
     * Maps a list of {@link AysRoleEntity} objects to a list of {@link AysRole} objects for summary representation.
     * This mapping excludes certain fields, such as permissions, as defined by the qualified mapping method {@code toSummary}.
     *
     * @param entities the list of {@link AysRoleEntity} objects to be mapped.
     * @return a list of {@link AysRole} objects with summarized information.
     */
    @IterableMapping(qualifiedByName = "toSummary")
    List<AysRole> mapListForSummary(List<AysRoleEntity> entities);

}
