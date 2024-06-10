package org.ays.emergency_application.model.entity;

import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.emergency_application.model.EmergencyEvacuationApplication;

public class EmergencyEvacuationApplicationBuilder extends TestDataBuilder<EmergencyEvacuationApplication> {

    public EmergencyEvacuationApplicationBuilder() {
        super(EmergencyEvacuationApplication.class);
    }

    public EmergencyEvacuationApplicationBuilder withValidFields() {
        return this
                .withId(AysRandomUtil.generateUUID());
    }

    public EmergencyEvacuationApplicationBuilder withId(String id) {
        data.setId(id);
        return this;
    }
}