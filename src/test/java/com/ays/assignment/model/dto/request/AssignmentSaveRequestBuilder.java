package com.ays.assignment.model.dto.request;

import com.ays.common.model.AysPhoneNumber;
import com.ays.common.model.AysPhoneNumberBuilder;
import com.ays.common.model.TestDataBuilder;

public class AssignmentSaveRequestBuilder extends TestDataBuilder<AssignmentSaveRequest> {

    public AssignmentSaveRequestBuilder() {
        super(AssignmentSaveRequest.class);
    }

    public AssignmentSaveRequestBuilder withValidFields() {
        this.withFirstName("First Name");
        this.withLastName("Last Name");
        this.withLatitude(0.0);
        this.withLongitude(0.0);
        this.withDescription("Description");
        this.withPhoneNumber(new AysPhoneNumberBuilder().withValidFields().build());
        return this;
    }

    public AssignmentSaveRequestBuilder withDescription(String description) {
        data.setDescription(description);
        return this;
    }

    public AssignmentSaveRequestBuilder withPhoneNumber(AysPhoneNumber phoneNumber) {
        data.setPhoneNumber(phoneNumber);
        return this;
    }

    public AssignmentSaveRequestBuilder withFirstName(String firstName) {
        data.setFirstName(firstName);
        return this;
    }

    public AssignmentSaveRequestBuilder withLastName(String lastName) {
        data.setLastName(lastName);
        return this;
    }

    public AssignmentSaveRequestBuilder withLatitude(Double latitude) {
        data.setLatitude(latitude);
        return this;
    }

    public AssignmentSaveRequestBuilder withLongitude(Double longitude) {
        data.setLongitude(longitude);
        return this;
    }

}
