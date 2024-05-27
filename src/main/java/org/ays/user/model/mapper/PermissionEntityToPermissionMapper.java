package org.ays.user.model.mapper;

import org.ays.common.model.mapper.BaseMapper;
import org.ays.user.model.Permission;
import org.ays.user.model.entity.PermissionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * {@link PermissionEntityToPermissionMapper} is an interface that defines the mapping between an {@link PermissionEntity} and an {@link Permission}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface PermissionEntityToPermissionMapper extends BaseMapper<PermissionEntity, Permission> {

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static PermissionEntityToPermissionMapper initialize() {
        return Mappers.getMapper(PermissionEntityToPermissionMapper.class);
    }

}
