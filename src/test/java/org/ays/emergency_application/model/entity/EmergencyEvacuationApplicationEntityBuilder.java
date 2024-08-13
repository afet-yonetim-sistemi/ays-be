package org.ays.emergency_application.model.entity;

import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.AysPhoneNumberBuilder;
import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.emergency_application.model.enums.EmergencyEvacuationApplicationStatus;

public class EmergencyEvacuationApplicationEntityBuilder extends TestDataBuilder<EmergencyEvacuationApplicationEntity> {

    public EmergencyEvacuationApplicationEntityBuilder() {
        super(EmergencyEvacuationApplicationEntity.class);
    }

    public EmergencyEvacuationApplicationEntityBuilder withValidValues() {
        return this
                .withId(AysRandomUtil.generateUUID())
                .withInstitutionId(AysRandomUtil.generateUUID())
                .withReferenceNumber(AysRandomUtil.generateNumber(10).toString())
                .withPhoneNumber(new AysPhoneNumberBuilder().withValidValues().build());
    }

    public EmergencyEvacuationApplicationEntityBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public EmergencyEvacuationApplicationEntityBuilder withInstitutionId(String institutionId) {
        data.setInstitutionId(institutionId);
        return this;
    }

    public EmergencyEvacuationApplicationEntityBuilder withReferenceNumber(String referenceNumber) {
        data.setReferenceNumber(referenceNumber);
        return this;
    }

    public EmergencyEvacuationApplicationEntityBuilder withStatus(EmergencyEvacuationApplicationStatus status) {
        data.setStatus(status);
        return this;
    }

    public EmergencyEvacuationApplicationEntityBuilder withPhoneNumber(AysPhoneNumber phoneNumber) {
        data.setCountryCode(phoneNumber.getCountryCode());
        data.setLineNumber(phoneNumber.getLineNumber());
        return this;
    }

    public EmergencyEvacuationApplicationEntityBuilder withoutApplicant() {
        data.setApplicantFirstName(null);
        data.setApplicantLastName(null);
        data.setApplicantCountryCode(null);
        data.setApplicantLineNumber(null);
        return this;
    }

}
