package org.ays.emergency_application.model.mapper;

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
public interface EmergencyEvacuationApplicationRequestToEntityMapper extends BaseMapper<EmergencyEvacuationApplicationRequest, EmergencyEvacuationApplicationEntity> {

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static EmergencyEvacuationApplicationRequestToEntityMapper initialize() {
        return Mappers.getMapper(EmergencyEvacuationApplicationRequestToEntityMapper.class);
    }

    /**
     * Maps an {@link EmergencyEvacuationApplicationRequest} object to an {@link EmergencyEvacuationApplicationEntity}
     * object for saving in the database.
     *
     * @param applicationRequest the {@link EmergencyEvacuationApplicationRequest} object to be mapped.
     * @return the mapped {@link EmergencyEvacuationApplicationEntity} object.
     */
    @Named("mapForSaving")
    default EmergencyEvacuationApplicationEntity mapForSaving(EmergencyEvacuationApplicationRequest applicationRequest) {
        Optional<AysPhoneNumberRequest> optionalApplicantPhoneNumber = Optional
                .ofNullable(applicationRequest.getApplicantPhoneNumber());

        return EmergencyEvacuationApplicationEntity.builder()
                .firstName(applicationRequest.getFirstName())
                .lastName(applicationRequest.getLastName())
                .referenceNumber(AysRandomUtil.generateNumber(10).toString())
                .countryCode(applicationRequest.getPhoneNumber().getCountryCode())
                .lineNumber(applicationRequest.getPhoneNumber().getLineNumber())
                .sourceCity(applicationRequest.getSourceCity())
                .sourceDistrict(applicationRequest.getSourceDistrict())
                .address(applicationRequest.getAddress())
                .seatingCount(applicationRequest.getSeatingCount())
                .targetCity(applicationRequest.getTargetCity())
                .targetDistrict(applicationRequest.getTargetDistrict())
                .applicantFirstName(applicationRequest.getApplicantFirstName())
                .applicantLastName(applicationRequest.getApplicantLastName())
                .applicantCountryCode(optionalApplicantPhoneNumber.map(AysPhoneNumberRequest::getCountryCode).orElse(null))
                .applicantLineNumber(optionalApplicantPhoneNumber.map(AysPhoneNumberRequest::getLineNumber).orElse(null))
                .status(EmergencyEvacuationApplicationStatus.PENDING)
                .build();
    }

}
