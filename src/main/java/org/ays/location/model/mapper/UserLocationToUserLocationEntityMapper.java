package org.ays.location.model.mapper;

import org.ays.common.model.mapper.BaseMapper;
import org.ays.location.model.UserLocation;
import org.ays.location.model.entity.UserLocationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * UserLocationToUserLocationEntityMapper is an interface that defines the mapping between an {@link UserLocation} and an {@link UserLocationEntity}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface UserLocationToUserLocationEntityMapper extends BaseMapper<UserLocation, UserLocationEntity> {

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static UserLocationToUserLocationEntityMapper initialize() {
        return Mappers.getMapper(UserLocationToUserLocationEntityMapper.class);
    }

}
