package com.ays.assignment.model.dto.request;

import com.ays.common.model.TestDataBuilder;

public class AssignmentSearchRequestBuilder extends TestDataBuilder<AssignmentSearchRequest> {

    public AssignmentSearchRequestBuilder() {
        super(AssignmentSearchRequest.class);
    }

    public AssignmentSearchRequestBuilder withValidFields() {
        this.withLongitude(1.0);
        this.withLatitude(1.0);
        return this;
    }

    public AssignmentSearchRequestBuilder withLongitude(Double longitude) {
        data.setLongitude(longitude);
        return this;
    }

    public AssignmentSearchRequestBuilder withLatitude(Double latitude) {
        data.setLatitude(latitude);
        return this;
    }

}
