package org.ays.emergency_application.model.mapper;

import org.ays.common.model.mapper.BaseMapper;
import org.ays.emergency_application.model.EmergencyEvacuationApplication;
import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

// TODO AYS-222 : Add Javadoc
@Mapper
public interface EmergencyEvacuationApplicationEntityToEmergencyEvacuationApplicationMapper extends BaseMapper<EmergencyEvacuationApplicationEntity, EmergencyEvacuationApplication> {

    /**
     * Initializes the mapper.
     *
     * @return the initialized mapper object.
     */
    static EmergencyEvacuationApplicationEntityToEmergencyEvacuationApplicationMapper initialize() {
        return Mappers.getMapper(EmergencyEvacuationApplicationEntityToEmergencyEvacuationApplicationMapper.class);
    }

}
