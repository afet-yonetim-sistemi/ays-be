package org.ays.assignment.model.mapper;

import org.ays.assignment.model.Assignment;
import org.ays.assignment.model.dto.response.AssignmentResponse;
import org.ays.common.model.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * AssignmentToAssignmentResponseMapper is an interface that defines the mapping between an {@link Assignment} and an {@link AssignmentResponse}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface AssignmentToAssignmentResponseMapper extends BaseMapper<Assignment, AssignmentResponse> {

    @Override
    @Mapping(target = "location.longitude", source = "point.x")
    @Mapping(target = "location.latitude", source = "point.y")
    AssignmentResponse map(Assignment source);

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static AssignmentToAssignmentResponseMapper initialize() {
        return Mappers.getMapper(AssignmentToAssignmentResponseMapper.class);
    }

}
