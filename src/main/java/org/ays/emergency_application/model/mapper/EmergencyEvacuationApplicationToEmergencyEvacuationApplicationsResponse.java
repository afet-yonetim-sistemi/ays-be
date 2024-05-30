package org.ays.emergency_application.model.mapper;

import org.ays.common.model.mapper.BaseMapper;
import org.ays.emergency_application.model.EmergencyEvacuationApplication;
import org.ays.emergency_application.model.dto.response.EmergencyEvacuationApplicationsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

// TODO AYS-222 : Add Javadoc
@Mapper
public interface EmergencyEvacuationApplicationToEmergencyEvacuationApplicationsResponse extends BaseMapper<EmergencyEvacuationApplication, EmergencyEvacuationApplicationsResponse> {

    static EmergencyEvacuationApplicationToEmergencyEvacuationApplicationsResponse initialize() {
        return Mappers.getMapper(EmergencyEvacuationApplicationToEmergencyEvacuationApplicationsResponse.class);
    }

}
