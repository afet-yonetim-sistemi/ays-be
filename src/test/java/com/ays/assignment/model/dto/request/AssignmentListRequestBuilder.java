package com.ays.assignment.model.dto.request;

import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.common.model.*;
import com.ays.user.model.dto.request.UserListRequestBuilder;

import java.util.List;

public class AssignmentListRequestBuilder extends TestDataBuilder<AssignmentListRequest> {

    public AssignmentListRequestBuilder() {
        super(AssignmentListRequest.class);
    }

    public AssignmentListRequestBuilder withValidValuesForStatus() {
        return this
                .withStatus()
                .withPagination(new AysPagingBuilder().withValidValues().build())
                .withSort(null);
    }

    public AssignmentListRequestBuilder withValidValuesForPhoneNumber() {
        return this
                .withPhoneNumber()
                .withPagination(new AysPagingBuilder().withValidValues().build())
                .withSort(null);
    }

    public AssignmentListRequestBuilder withValidValues() {
        return this
                .withPhoneNumberAndStatus()
                .withPagination(new AysPagingBuilder().withValidValues().build())
                .withSort(null);
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

    public AssignmentListRequestBuilder withPagination(AysPaging aysPaging) {
        data.setPagination(aysPaging);
        return this;
    }

    public AssignmentListRequestBuilder withSort(List<AysSorting> sorting) {
        data.setSort(sorting);
        return this;
    }
}
