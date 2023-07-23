package com.ays.assignment.model.mapper;

import com.ays.assignment.model.Assignment;
import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.common.model.mapper.BaseMapper;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * AssignmentToAssignmentEntityMapper is an interface that defines the mapping between an {@link Assignment} and an {@link AssignmentEntity}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface AssignmentToAssignmentEntityMapper extends BaseMapper<Assignment, AssignmentEntity> {

    /**
     * Maps UserAssignment to UserAssignmentEntity.
     *
     * @param assignment the UserAssignment object
     * @return the mapped UserAssignmentEntity object
     */
    @Mapping(target = "point", expression = "java(mapToPoint(assignment.getLatitude(), assignment.getLongitude()))")
    AssignmentEntity map(Assignment assignment);


    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static AssignmentToAssignmentEntityMapper initialize() {
        return Mappers.getMapper(AssignmentToAssignmentEntityMapper.class);
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
