package com.ays.location.model.mapper;

import com.ays.common.model.mapper.BaseMapper;
import com.ays.location.model.dto.request.UserLocationSaveRequest;
import com.ays.location.model.entity.UserLocationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * UserLocationSaveRequestToUserLocationEntityMapper is an interface that defines the mapping between an {@link UserLocationSaveRequest} and an {@link UserLocationEntity}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface UserLocationSaveRequestToUserLocationEntityMapper extends BaseMapper<UserLocationSaveRequest, UserLocationEntity> {

    /**
     * Maps an {@link UserLocationSaveRequest} object to an {@link UserLocationEntity} object for saving in the database.
     *
     * @param saveRequest the {@link UserLocationSaveRequest} object to be mapped.
     * @param userId      the {@link String} object.
     * @return the mapped {@link UserLocationEntity} object.
     */
    default UserLocationEntity mapForSaving(UserLocationSaveRequest saveRequest, String userId) {
        return UserLocationEntity.builder()
                .userId(userId)
                .point(saveRequest.getLatitude(), saveRequest.getLongitude())
                .build();
    }

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static UserLocationSaveRequestToUserLocationEntityMapper initialize() {
        return Mappers.getMapper(UserLocationSaveRequestToUserLocationEntityMapper.class);
    }

}
