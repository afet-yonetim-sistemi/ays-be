package com.ays.assignment.model.mapper;

import com.ays.assignment.model.UserAssignment;
import com.ays.assignment.model.entity.UserAssignmentEntity;
import com.ays.common.model.mapper.BaseMapper;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * UserAssignmentToUserAssignmentEntityMapper is an interface that defines the mapping between an {@link UserAssignment} and an {@link UserAssignmentEntity}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface UserAssignmentToUserAssignmentEntityMapper extends BaseMapper<UserAssignment, UserAssignmentEntity> {

    /**
     * Maps UserAssignment to UserAssignmentEntity.
     *
     * @param userAssignment the UserAssignment object
     * @return the mapped UserAssignmentEntity object
     */
    @Mapping(target = "point", expression = "java(mapToPoint(userAssignment.getLatitude(), userAssignment.getLongitude()))")
    UserAssignmentEntity map(UserAssignment userAssignment);


    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static UserAssignmentToUserAssignmentEntityMapper initialize() {
        return Mappers.getMapper(UserAssignmentToUserAssignmentEntityMapper.class);
    }

    /**
     * Maps latitude and longitude to the point field.
     *
     * @param latitude  the latitude value
     * @param longitude the longitude value
     * @return the mapped Point object
     */
    default Point mapToPoint(Double latitude, Double longitude) {
        if (latitude != null && longitude != null) {
            Coordinate coordinate = new Coordinate(latitude, longitude);
            GeometryFactory geometryFactory = new GeometryFactory();
            return geometryFactory.createPoint(coordinate);
        }
        return null;
    }
}
