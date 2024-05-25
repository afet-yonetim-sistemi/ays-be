package org.ays.emergency_application.model.entity;

import org.ays.common.model.TestDataBuilder;

public class EmergencyEvacuationEntityBuilder extends TestDataBuilder<EmergencyEvacuationEntity> {
    public EmergencyEvacuationEntityBuilder() {
        super(EmergencyEvacuationEntity.class);
    }

    public EmergencyEvacuationEntityBuilder withValidFields() {
        return this;
    }

}