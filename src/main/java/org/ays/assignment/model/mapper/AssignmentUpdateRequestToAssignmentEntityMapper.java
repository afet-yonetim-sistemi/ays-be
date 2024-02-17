package org.ays.assignment.model.mapper;

import org.ays.assignment.model.dto.request.AssignmentUpdateRequest;
import org.ays.assignment.model.entity.AssignmentEntity;
import org.ays.common.model.mapper.BaseMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * AssignmentUpdateRequestToAssignmentEntityMapper is an interface that defines the mapping between an {@link AssignmentUpdateRequest} and an {@link AssignmentEntity}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface AssignmentUpdateRequestToAssignmentEntityMapper extends BaseMapper<AssignmentUpdateRequest, AssignmentEntity> {

    @Override
    @Mapping(target = "countryCode", source = "phoneNumber.countryCode")
    @Mapping(target = "lineNumber", source = "phoneNumber.lineNumber")
    AssignmentEntity map(AssignmentUpdateRequest source);

    /**
     * Custom mapping method executed after the main mapping process.
     * This method maps the latitude and longitude coordinates from the
     * source {@code AssignmentUpdateRequest} to the corresponding fields
     * in the {@code AssignmentEntityBuilder}.
     *
     * @param source  The source object containing assignment update information.
     * @param builder The builder for constructing an {@link AssignmentEntity}.
     *                It is annotated with {@link @MappingTarget} to indicate
     *                that it will be modified by this method.
     */
    @AfterMapping
    default void mapLocation(AssignmentUpdateRequest source,
                             @MappingTarget AssignmentEntity.AssignmentEntityBuilder<?, ?> builder) {
        builder.point(source.getLongitude(), source.getLatitude());
    }

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static AssignmentUpdateRequestToAssignmentEntityMapper initialize() {
        return Mappers.getMapper(AssignmentUpdateRequestToAssignmentEntityMapper.class);
    }
}
