package org.ays.emergency_application.model.mapper;

import org.ays.common.model.mapper.BaseMapper;
import org.ays.emergency_application.model.EmergencyEvacuationApplication;
import org.ays.emergency_application.model.dto.response.EmergencyEvacuationApplicationDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

// TODO AYS-223 : Add Javadoc
@Mapper
public interface EmergencyEvacuationApplicationToApplicationsDetailResponseMapper extends BaseMapper<EmergencyEvacuationApplication, EmergencyEvacuationApplicationDetailResponse> {

    static EmergencyEvacuationApplicationToApplicationsDetailResponseMapper initialize() {
        return Mappers.getMapper(EmergencyEvacuationApplicationToApplicationsDetailResponseMapper.class);
    }

}
