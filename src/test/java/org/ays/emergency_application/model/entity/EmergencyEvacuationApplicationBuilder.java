package org.ays.emergency_application.model.entity;

import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.AysPhoneNumberBuilder;
import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.emergency_application.model.EmergencyEvacuationApplication;

public class EmergencyEvacuationApplicationBuilder extends TestDataBuilder<EmergencyEvacuationApplication> {

    public EmergencyEvacuationApplicationBuilder() {
        super(EmergencyEvacuationApplication.class);
    }

    public EmergencyEvacuationApplicationBuilder withValidFields() {
        return this
                .withId(AysRandomUtil.generateUUID())
                .withPhoneNumber(new AysPhoneNumberBuilder().withValidFields().build())
                .withoutApplicant();
    }

    public EmergencyEvacuationApplicationBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public EmergencyEvacuationApplicationBuilder withPhoneNumber(AysPhoneNumber phoneNumber) {
        data.setPhoneNumber(phoneNumber);
        return this;
    }

    public EmergencyEvacuationApplicationBuilder withoutApplicant() {
        data.setApplicantFirstName(null);
        data.setApplicantLastName(null);
        data.setApplicantPhoneNumber(null);
        return this;
    }
}
