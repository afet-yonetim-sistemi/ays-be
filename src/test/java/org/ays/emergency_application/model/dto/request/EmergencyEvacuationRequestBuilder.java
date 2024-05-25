package org.ays.emergency_application.model.dto.request;

import org.ays.common.model.TestDataBuilder;
import org.ays.common.model.dto.request.AysPhoneNumberRequest;
import org.ays.common.model.dto.request.AysPhoneNumberRequestBuilder;

public class EmergencyEvacuationRequestBuilder extends TestDataBuilder<EmergencyEvacuationRequest> {
    public EmergencyEvacuationRequestBuilder() {
        super(EmergencyEvacuationRequest.class);
    }

    public EmergencyEvacuationRequestBuilder withValidFields() {
        return this
                .withPhoneNumber(new AysPhoneNumberRequestBuilder().withValidFields().build());
    }

    public EmergencyEvacuationRequestBuilder withPhoneNumber(AysPhoneNumberRequest phoneNumber) {
        data.setPhoneNumber(phoneNumber);
        data.setApplicantPhoneNumber(phoneNumber);
        return this;
    }

}
