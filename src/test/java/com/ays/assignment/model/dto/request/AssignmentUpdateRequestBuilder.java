package com.ays.assignment.model.dto.request;

import com.ays.common.model.TestDataBuilder;
import com.ays.common.model.dto.request.AysPhoneNumberRequest;
import com.ays.common.model.dto.request.AysPhoneNumberRequestBuilder;

public class AssignmentUpdateRequestBuilder extends TestDataBuilder<AssignmentUpdateRequest> {
    public AssignmentUpdateRequestBuilder() {
        super(AssignmentUpdateRequest.class);
    }

    public AssignmentUpdateRequestBuilder withValidFields() {
        return this
                .withPhoneNumber(new AysPhoneNumberRequestBuilder().withValidFields().build())
                .withLatitude(2.0)
                .withLongitude(2.0);
    }

    public AssignmentUpdateRequestBuilder withPhoneNumber(AysPhoneNumberRequest phoneNumber) {
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

    public AssignmentUpdateRequestBuilder withFirstName(String firstName) {
        data.setFirstName(firstName);
        return this;
    }

    public AssignmentUpdateRequestBuilder withLastName(String lastName) {
        data.setLastName(lastName);
        return this;
    }
}
