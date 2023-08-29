package com.ays.assignment.model.dto.request;

import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.common.model.AysPhoneNumberBuilder;
import com.ays.common.model.TestDataBuilder;

import java.util.List;

public class AssignmentListRequestBuilder extends TestDataBuilder<AssignmentListRequest> {

    public AssignmentListRequestBuilder() {
        super(AssignmentListRequest.class);
    }

    public AssignmentListRequestBuilder withStatus() {

        data.setFilter(new AssignmentListRequest.Filter(List.of(AssignmentStatus.AVAILABLE, AssignmentStatus.ASSIGNED)
                , null));

        return this;
    }

    public AssignmentListRequestBuilder withPhoneNumber() {

        AysPhoneNumberBuilder phoneNumberBuilder = new AysPhoneNumberBuilder().withValidFields();

        data.setFilter(new AssignmentListRequest.Filter(null
                , phoneNumberBuilder.build()));

        return this;
    }

    public AssignmentListRequestBuilder withPhoneNumberAndStatus() {

        AysPhoneNumberBuilder phoneNumberBuilder = new AysPhoneNumberBuilder().withValidFields();

        data.setFilter(new AssignmentListRequest.Filter(List.of(AssignmentStatus.AVAILABLE, AssignmentStatus.ASSIGNED)
                , phoneNumberBuilder.build()));

        return this;
    }

}
