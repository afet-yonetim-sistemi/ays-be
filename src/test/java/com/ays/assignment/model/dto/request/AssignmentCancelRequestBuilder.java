package com.ays.assignment.model.dto.request;

import com.ays.common.model.TestDataBuilder;

public class AssignmentCancelRequestBuilder extends TestDataBuilder<AssignmentCancelRequest> {

    public AssignmentCancelRequestBuilder() {
        super(AssignmentCancelRequest.class);
    }

    public AssignmentCancelRequestBuilder withValidFields() {
        return this
                .withReason("Kaza nedeniyle assignment'a devam edemeyecek durumdayım.");
    }

    public AssignmentCancelRequestBuilder withReason(String reason) {
        data.setReason(reason);
        return this;
    }
}
