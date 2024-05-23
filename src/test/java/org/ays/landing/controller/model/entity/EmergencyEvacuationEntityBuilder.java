package org.ays.landing.controller.model.entity;

import org.ays.common.model.TestDataBuilder;
import org.ays.landing.model.entity.EmergencyEvacuationEntity;

public class EmergencyEvacuationEntityBuilder extends TestDataBuilder<EmergencyEvacuationEntity> {
    public EmergencyEvacuationEntityBuilder() {
        super(EmergencyEvacuationEntity.class);
    }

    public EmergencyEvacuationEntityBuilder withValidFields() {
        return this;
    }

}