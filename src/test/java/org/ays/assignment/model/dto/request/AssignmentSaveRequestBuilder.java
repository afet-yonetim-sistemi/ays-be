package org.ays.assignment.model.dto.request;

import org.ays.common.model.TestDataBuilder;
import org.ays.common.model.dto.request.AysPhoneNumberRequest;
import org.ays.common.model.dto.request.AysPhoneNumberRequestBuilder;

public class AssignmentSaveRequestBuilder extends TestDataBuilder<AssignmentSaveRequest> {

    public AssignmentSaveRequestBuilder() {
        super(AssignmentSaveRequest.class);
    }

    public AssignmentSaveRequestBuilder withValidFields() {
        this.withLatitude(0.0);
        this.withLongitude(0.0);
        this.withPhoneNumber(new AysPhoneNumberRequestBuilder().withValidFields().build());
        return this;
    }

    public AssignmentSaveRequestBuilder withPhoneNumber(AysPhoneNumberRequest phoneNumber) {
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
