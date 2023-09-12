package com.ays.assignment.model.mapper;

import com.ays.assignment.model.dto.request.AssignmentUpdateRequest;
import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.common.model.mapper.BaseMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AssignmentUpdateRequestToAssignmentEntityMapper extends BaseMapper<AssignmentUpdateRequest, AssignmentEntity> {

    @Override
    @Mapping(target = "countryCode", source = "phoneNumber.countryCode")
    @Mapping(target = "lineNumber", source = "phoneNumber.lineNumber")
    AssignmentEntity map(AssignmentUpdateRequest source);

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
