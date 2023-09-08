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
                .withDescription("Description")
                .withFirstName("First Name")
                .withLastName("Last Name")
                .withPhoneNumber(new AysPhoneNumberBuilder().withValidFields().build())
                .withLatitude(1.0)
                .withLongitude(1.0);
    }

    public AssignmentUpdateRequestBuilder withDescription(String description) {
        data.setDescription(description);
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
