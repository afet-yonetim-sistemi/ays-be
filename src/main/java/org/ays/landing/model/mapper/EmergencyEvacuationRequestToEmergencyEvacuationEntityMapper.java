package org.ays.landing.model.mapper;

import org.ays.common.model.mapper.BaseMapper;
import org.ays.common.util.AysRandomUtil;
import org.ays.landing.model.dto.request.EmergencyEvacuationRequest;
import org.ays.landing.model.entity.EmergencyEvacuationApplicationStatus;
import org.ays.landing.model.entity.EmergencyEvacuationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * EmergencyEvacuationRequestToEmergencyEvacuationEntityMapper is an interface that defines the
 * mapping between an {@link EmergencyEvacuationRequest} and an {@link EmergencyEvacuationEntity}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface EmergencyEvacuationRequestToEmergencyEvacuationEntityMapper extends BaseMapper<EmergencyEvacuationRequest, EmergencyEvacuationEntity> {

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static EmergencyEvacuationRequestToEmergencyEvacuationEntityMapper initialize() {
        return Mappers.getMapper(EmergencyEvacuationRequestToEmergencyEvacuationEntityMapper.class);
    }

    /**
     * Maps an {@link EmergencyEvacuationRequest} object to an {@link EmergencyEvacuationEntity}
     * object for saving in the database.
     *
     * @param evacuationRequest the {@link EmergencyEvacuationRequest} object to be mapped.
     * @return the mapped {@link EmergencyEvacuationEntity} object.
     */
    @Named("mapForSaving")
    default EmergencyEvacuationEntity mapForSaving(EmergencyEvacuationRequest evacuationRequest) {
        return EmergencyEvacuationEntity.builder()
                .id(AysRandomUtil.generateUUID())
                .firstName(evacuationRequest.getFirstName())
                .lastName(evacuationRequest.getLastName())
                .countryCode(evacuationRequest.getPhoneNumber().getCountryCode())
                .lineNumber(evacuationRequest.getPhoneNumber().getLineNumber())
                .address(evacuationRequest.getAddress())
                .personCount(evacuationRequest.getPersonCount())
                .hasObstaclePersonExist(evacuationRequest.isHasObstaclePersonExist())
                .targetCity(evacuationRequest.getTargetCity())
                .targetDistrict(evacuationRequest.getTargetDistrict())
                .applicantFirstName(evacuationRequest.getApplicantFirstName())
                .applicantLastName(evacuationRequest.getApplicantLastName())
                .applicantCountryCode(evacuationRequest.getApplicantPhoneNumber().getCountryCode())
                .applicantLineNumber(evacuationRequest.getApplicantPhoneNumber().getLineNumber())
                .status(EmergencyEvacuationApplicationStatus.PENDING)
                .build();
    }
}
