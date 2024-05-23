package org.ays.landing.controller.model.dto.response;

import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.AysPhoneNumberBuilder;
import org.ays.common.model.TestDataBuilder;
import org.ays.common.model.dto.request.AysPhoneNumberRequest;
import org.ays.common.model.dto.request.AysPhoneNumberRequestBuilder;
import org.ays.landing.model.dto.request.EmergencyEvacuationRequest;
import org.ays.landing.model.dto.response.EmergencyEvacuationApplicationResponse;

public class EmergencyEvacuationResponseBuilder extends TestDataBuilder<EmergencyEvacuationApplicationResponse> {
    public EmergencyEvacuationResponseBuilder() {
        super(EmergencyEvacuationApplicationResponse.class);
    }

    public EmergencyEvacuationResponseBuilder withValidFields() {
        return this
                .withPhoneNumber(new AysPhoneNumberBuilder().withValidFields().build());
    }

    public EmergencyEvacuationResponseBuilder withPhoneNumber(AysPhoneNumber phoneNumber) {
        data.setApplicantPhoneNumber(phoneNumber);
        return this;
    }

}
