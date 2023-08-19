package com.ays.assignment.model.mapper;

import com.ays.assignment.model.Assignment;
import com.ays.assignment.model.dto.request.AssignmentSaveRequest;
import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.common.model.mapper.BaseMapper;
import com.ays.common.util.AysRandomUtil;
import com.ays.user.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * UserToUserResponseMapper is an interface that defines the mapping between an {@link AssignmentSaveRequest} and an {@link Assignment}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface AssignmentSaveRequestToAssignmentMapper extends BaseMapper<AssignmentSaveRequest, Assignment> {

    /**
     * Maps an {@link AssignmentSaveRequest} object to an {@link AssignmentEntity} object for saving in the database.
     *
     * @param saveRequest   the {@link AssignmentSaveRequest} object to be mapped.
     * @param institutionId the {@link String} object.
     * @return the mapped {@link UserEntity} object.
     */
    default AssignmentEntity mapForSaving(AssignmentSaveRequest saveRequest, String institutionId) {

        AssignmentEntity assignmentEntity = AssignmentEntity.builder()
                .id(AysRandomUtil.generateUUID())
                .institutionId(institutionId)
                .firstName(saveRequest.getFirstName())
                .lastName(saveRequest.getLastName())
                .lineNumber(saveRequest.getPhoneNumber().getLineNumber())
                .countryCode(saveRequest.getPhoneNumber().getCountryCode())
                .description(saveRequest.getDescription())
                .status(AssignmentStatus.AVAILABLE)
                .build();

        assignmentEntity.setPoint(saveRequest.getLatitude(), saveRequest.getLongitude());

        return assignmentEntity;
    }

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static AssignmentSaveRequestToAssignmentMapper initialize() {
        return Mappers.getMapper(AssignmentSaveRequestToAssignmentMapper.class);
    }

}
