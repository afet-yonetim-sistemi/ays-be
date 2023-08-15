package com.ays.assignment.model;

import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.common.model.AysPhoneNumber;
import com.ays.common.model.TestDataBuilder;

public class AssignmentBuilder extends TestDataBuilder<Assignment> {

    public AssignmentBuilder() {
        super(Assignment.class);
    }

    public AssignmentBuilder withDescription(String description) {
        data.setDescription(description);
        return this;
    }

    public AssignmentBuilder withFirstName(String firstName) {
        data.setFirstName(firstName);
        return this;
    }

    public AssignmentBuilder withLastName(String lastName) {
        data.setLastName(lastName);
        return this;
    }

    public AssignmentBuilder withStatus(AssignmentStatus status) {
        data.setStatus(status);
        return this;
    }

    public AssignmentBuilder withPhoneNumber(AysPhoneNumber phoneNumber) {
        data.setPhoneNumber(phoneNumber);
        return this;
    }

}
