package org.ays.emergency_application.model.mapper;

import jakarta.validation.Valid;
import org.ays.common.model.dto.request.AysPhoneNumberRequest;
import org.ays.common.model.mapper.BaseMapper;
import org.ays.common.util.AysRandomUtil;
import org.ays.emergency_application.model.dto.request.EmergencyEvacuationApplicationRequest;
import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationEntity;
import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

/**
 * EmergencyEvacuationRequestToEmergencyEvacuationEntityMapper is an interface that defines the
 * mapping between an {@link EmergencyEvacuationApplicationRequest} and an {@link EmergencyEvacuationApplicationEntity}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface EmergencyEvacuationRequestToEmergencyEvacuationEntityMapper extends BaseMapper<EmergencyEvacuationApplicationRequest, EmergencyEvacuationApplicationEntity> {

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static EmergencyEvacuationRequestToEmergencyEvacuationEntityMapper initialize() {
        return Mappers.getMapper(EmergencyEvacuationRequestToEmergencyEvacuationEntityMapper.class);
    }

    /**
     * Maps an {@link EmergencyEvacuationApplicationRequest} object to an {@link EmergencyEvacuationApplicationEntity}
     * object for saving in the database.
     *
     * @param evacuationRequest the {@link EmergencyEvacuationApplicationRequest} object to be mapped.
     * @return the mapped {@link EmergencyEvacuationApplicationEntity} object.
     */
    @Named("mapForSaving")
    default EmergencyEvacuationApplicationEntity mapForSaving(EmergencyEvacuationApplicationRequest evacuationRequest) {
        @Valid Optional<AysPhoneNumberRequest> optionalApplicantPhoneNumber = Optional.ofNullable(evacuationRequest.getApplicantPhoneNumber());
        return EmergencyEvacuationApplicationEntity.builder()
                .id(AysRandomUtil.generateUUID())
                .firstName(evacuationRequest.getFirstName())
                .lastName(evacuationRequest.getLastName())
                .referenceNumber(AysRandomUtil.generateNumber(10))
                .countryCode(evacuationRequest.getPhoneNumber().getCountryCode())
                .lineNumber(evacuationRequest.getPhoneNumber().getLineNumber())
                .address(evacuationRequest.getAddress())
                .personCount(evacuationRequest.getPersonCount())
                .hasObstaclePersonExist(evacuationRequest.isHasObstaclePersonExist())
                .targetCity(evacuationRequest.getTargetCity())
                .targetDistrict(evacuationRequest.getTargetDistrict())
                .applicantFirstName(evacuationRequest.getApplicantFirstName())
                .applicantLastName(evacuationRequest.getApplicantLastName())
                .applicantCountryCode(optionalApplicantPhoneNumber.map(AysPhoneNumberRequest::getCountryCode).orElse(null))
                .applicantLineNumber(optionalApplicantPhoneNumber.map(AysPhoneNumberRequest::getLineNumber).orElse(null))
                .status(EmergencyEvacuationApplicationStatus.PENDING)
                .build();
    }
}
