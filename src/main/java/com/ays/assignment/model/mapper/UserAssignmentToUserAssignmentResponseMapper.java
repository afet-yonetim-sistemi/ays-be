package com.ays.assignment.model.mapper;

import com.ays.assignment.model.UserAssignment;
import com.ays.assignment.model.dto.response.UserAssignmentResponse;
import com.ays.common.model.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * AssignmentToAssignmentResponseMapper is an interface that defines the mapping between an {@link UserAssignment} and an {@link UserAssignmentResponse}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface UserAssignmentToUserAssignmentResponseMapper extends BaseMapper<UserAssignment, UserAssignmentResponse> {

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static UserAssignmentToUserAssignmentResponseMapper initialize() {
        return Mappers.getMapper(UserAssignmentToUserAssignmentResponseMapper.class);
    }

}
