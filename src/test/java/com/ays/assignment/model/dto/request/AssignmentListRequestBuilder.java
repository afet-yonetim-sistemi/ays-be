package com.ays.assignment.model.dto.request;

import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.common.model.AysPaging;
import com.ays.common.model.AysPagingBuilder;
import com.ays.common.model.AysSorting;
import com.ays.common.model.TestDataBuilder;

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


        data.setFilter(new AssignmentListRequest.Filter(null
                , new AssignmentListRequest.PhoneNumber("90", "1234567890")));

        return this;
    }

    public AssignmentListRequestBuilder withPhoneNumberAndStatus() {

        data.setFilter(new AssignmentListRequest.Filter(List.of(AssignmentStatus.AVAILABLE, AssignmentStatus.ASSIGNED)
                , new AssignmentListRequest.PhoneNumber("90", "1234567890")));

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
