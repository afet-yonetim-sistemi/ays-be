package com.ays.assignment.model.dto.response;

import com.ays.common.model.AysPhoneNumber;
import com.ays.common.model.TestDataBuilder;

public class AssignmentSavedResponseBuilder extends TestDataBuilder<AssignmentSavedResponse> {

    public AssignmentSavedResponseBuilder() {
        super(AssignmentSavedResponse.class);
    }


    public AssignmentSavedResponseBuilder withPhoneNumber(AysPhoneNumber phoneNumber) {
        data.setPhoneNumber(phoneNumber);
        return this;
    }


    public AssignmentSavedResponseBuilder withDescription(String description) {
        data.setDescription(description);
        return this;
    }

    public AssignmentSavedResponseBuilder withFirstName(String firstName) {
        data.setFirstName(firstName);
        return this;
    }

    public AssignmentSavedResponseBuilder withLastName(String lastName) {
        data.setLastName(lastName);
        return this;
    }

    public AssignmentSavedResponseBuilder withLongitude(double longitude) {
        data.setLongitude(longitude);
        return this;
    }

    public AssignmentSavedResponseBuilder withLatitude(double latitude) {
        data.setLatitude(latitude);
        return this;
    }
}
