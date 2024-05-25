package org.ays.emergency_application.model.entity;

import org.ays.common.model.TestDataBuilder;

public class EmergencyEvacuationEntityBuilder extends TestDataBuilder<EmergencyEvacuationApplicationEntity> {
    public EmergencyEvacuationEntityBuilder() {
        super(EmergencyEvacuationApplicationEntity.class);
    }

    public EmergencyEvacuationEntityBuilder withValidFields() {
        return this;
    }

}