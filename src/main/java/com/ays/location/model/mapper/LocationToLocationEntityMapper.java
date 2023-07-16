package com.ays.location.model.mapper;

import com.ays.common.model.mapper.BaseMapper;
import com.ays.location.model.Location;
import com.ays.location.model.entity.UserLocationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * LocationToLocationEntityMapper is an interface that defines the mapping between an {@link Location} and an {@link UserLocationEntity}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface LocationToLocationEntityMapper extends BaseMapper<Location, UserLocationEntity> {

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static LocationToLocationEntityMapper initialize() {
        return Mappers.getMapper(LocationToLocationEntityMapper.class);
    }

}
