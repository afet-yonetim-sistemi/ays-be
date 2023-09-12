package com.ays.assignment.model.dto.request;

import com.ays.common.model.AysPhoneNumber;
import com.ays.common.model.AysPhoneNumberBuilder;
import com.ays.common.model.TestDataBuilder;

public class AssignmentUpdateRequestBuilder extends TestDataBuilder<AssignmentUpdateRequest> {
    public AssignmentUpdateRequestBuilder() {
        super(AssignmentUpdateRequest.class);
    }

    public AssignmentUpdateRequestBuilder withValidFields() {
        return this
                .withPhoneNumber(new AysPhoneNumberBuilder()
                        .withCountryCode("91")
                        .withLineNumber("9876543210")
                        .build())
                .withLatitude(2.0)
                .withLongitude(2.0);
    }

    public AssignmentUpdateRequestBuilder withPhoneNumber(AysPhoneNumber phoneNumber) {
        data.setPhoneNumber(phoneNumber);
        return this;
    }

    public AssignmentUpdateRequestBuilder withLatitude(Double latitude) {
        data.setLatitude(latitude);
        return this;
    }

    public AssignmentUpdateRequestBuilder withLongitude(Double longitude) {
        data.setLongitude(longitude);
        return this;
    }
}
