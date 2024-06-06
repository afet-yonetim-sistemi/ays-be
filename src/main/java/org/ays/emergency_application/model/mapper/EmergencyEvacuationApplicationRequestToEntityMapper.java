package org.ays.emergency_application.model.mapper;

import org.ays.common.model.mapper.BaseMapper;
import org.ays.emergency_application.model.dto.request.EmergencyEvacuationApplicationRequest;
import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

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

    @Override
    @Mapping(target = "countryCode", source = "phoneNumber.countryCode")
    @Mapping(target = "lineNumber", source = "phoneNumber.lineNumber")
    @Mapping(target = "applicantCountryCode", source = "applicantPhoneNumber.countryCode")
    @Mapping(target = "applicantLineNumber", source = "applicantPhoneNumber.lineNumber")
    EmergencyEvacuationApplicationEntity map(EmergencyEvacuationApplicationRequest applicationRequest);

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static EmergencyEvacuationApplicationRequestToEntityMapper initialize() {
        return Mappers.getMapper(EmergencyEvacuationApplicationRequestToEntityMapper.class);
    }

}
