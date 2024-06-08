package org.ays.emergency_application.model.entity;

import org.ays.common.model.TestDataBuilder;
import org.ays.emergency_application.model.EmergencyEvacuationApplication;

public class EmergencyEvacuationApplicationBuilder extends TestDataBuilder<EmergencyEvacuationApplication> {

    public EmergencyEvacuationApplicationBuilder() {
        super(EmergencyEvacuationApplication.class);
    }

    public EmergencyEvacuationApplicationBuilder withValidFields(String id) {
        return this
                .withId(id);
    }

    public EmergencyEvacuationApplicationBuilder withId(String id) {
        data.setId(id);
        return this;
    }
}