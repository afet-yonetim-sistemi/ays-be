package org.ays.landing.model.mapper;

import org.ays.common.model.mapper.BaseMapper;
import org.ays.landing.model.dto.response.EmergencyEvacuationApplicationResponse;
import org.ays.landing.model.entity.EmergencyEvacuationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * EmergencyEvacuationEntityToEmergencyEvacuationResponseMapper is an interface that defines the mapping between
 * an {@link EmergencyEvacuationEntity} and an {@link EmergencyEvacuationApplicationResponse}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface EmergencyEvacuationEntityToEmergencyEvacuationResponseMapper extends BaseMapper<EmergencyEvacuationEntity, EmergencyEvacuationApplicationResponse> {

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static EmergencyEvacuationEntityToEmergencyEvacuationResponseMapper initialize() {
        return Mappers.getMapper(EmergencyEvacuationEntityToEmergencyEvacuationResponseMapper.class);
    }

    @Override
    @Mapping(target = "userPhoneNumber.countryCode", source = "countryCode")
    @Mapping(target = "userPhoneNumber.lineNumber", source = "lineNumber")
    @Mapping(target = "applicantPhoneNumber.countryCode", source = "applicantCountryCode")
    @Mapping(target = "applicantPhoneNumber.lineNumber", source = "applicantLineNumber")
    EmergencyEvacuationApplicationResponse map(EmergencyEvacuationEntity entity);

}
