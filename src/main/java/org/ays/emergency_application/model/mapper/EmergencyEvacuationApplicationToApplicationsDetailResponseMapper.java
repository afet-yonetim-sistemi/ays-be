package org.ays.emergency_application.model.mapper;

import org.ays.common.model.mapper.BaseMapper;
import org.ays.emergency_application.model.EmergencyEvacuationApplication;
import org.ays.emergency_application.model.dto.response.EmergencyEvacuationApplicationDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * {@link EmergencyEvacuationApplicationToApplicationsDetailResponseMapper} is an interface that defines the
 * mapping between an {@link EmergencyEvacuationApplication} and an {@link EmergencyEvacuationApplicationDetailResponse}.
 * This interface uses the MapStruct annotation @Mapper to generate an implementation of this interface at compile-time.
 * <p>The class provides a static method {@code initialize()} that returns an instance of the generated mapper implementation.
 * <p>The interface extends the MapStruct interface {@link BaseMapper}, which defines basic mapping methods.
 * The interface adds no additional mapping methods, but simply defines the types to be used in the mapping process.
 */
@Mapper
public interface EmergencyEvacuationApplicationToApplicationsDetailResponseMapper extends BaseMapper<EmergencyEvacuationApplication, EmergencyEvacuationApplicationDetailResponse> {

    static EmergencyEvacuationApplicationToApplicationsDetailResponseMapper initialize() {
        return Mappers.getMapper(EmergencyEvacuationApplicationToApplicationsDetailResponseMapper.class);
    }
}
