package com.ays.assignment.model.mapper;

import com.ays.assignment.model.Assignment;
import com.ays.assignment.model.dto.response.AssignmentsResponse;
import com.ays.common.model.mapper.BaseMapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * AssignmentEntityToAssignmentMapper is an interface that defines the mapping between an {@link Assignment} and an {@link AssignmentsResponse}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
public interface AssignmentToAssignmentsResponseMapper extends BaseMapper<Assignment, AssignmentsResponse> {

    @Override
    @Mapping(target = "location.longitude", source = "point.coordinates.y")
    @Mapping(target = "location.latitude", source = "point.coordinates.x")
    AssignmentsResponse map(Assignment source);

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static AssignmentEntityToAssignmentMapper initialize() {
        return Mappers.getMapper(AssignmentEntityToAssignmentMapper.class);
    }
}
