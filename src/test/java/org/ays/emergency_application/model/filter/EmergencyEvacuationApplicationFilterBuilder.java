package org.ays.emergency_application.model.filter;

import org.ays.common.model.TestDataBuilder;
import org.ays.emergency_application.model.enums.EmergencyEvacuationApplicationStatus;

import java.util.Set;

public class EmergencyEvacuationApplicationFilterBuilder extends TestDataBuilder<EmergencyEvacuationApplicationFilter> {

    public EmergencyEvacuationApplicationFilterBuilder() {
        super(EmergencyEvacuationApplicationFilter.class);
        data.setReferenceNumber(null);
        data.setSourceCity(null);
        data.setSourceDistrict(null);
        data.setSeatingCount(null);
        data.setTargetCity(null);
        data.setTargetDistrict(null);
        data.setStatuses(null);
        data.setIsInPerson(null);
    }

    public EmergencyEvacuationApplicationFilterBuilder withStatus(Set<EmergencyEvacuationApplicationStatus> statuses) {
        data.setStatuses(statuses);
        return this;
    }

}
