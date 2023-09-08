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

    public AssignmentListRequestBuilder withValidValues() {
        return this
                .initializeFilter()
                .withStatuses(List.of(AssignmentStatus.AVAILABLE, AssignmentStatus.ASSIGNED))
                .withPhoneNumber(new AssignmentListRequest.PhoneNumber("90", "1234567890"))
                .withPagination(new AysPagingBuilder().withValidValues().build())
                .withSort(null);
    }

    private AssignmentListRequestBuilder initializeFilter() {
        data.setFilter(new AssignmentListRequest.Filter());
        return this;
    }

    public AssignmentListRequestBuilder withFilter(AssignmentListRequest.Filter filter) {
        data.setFilter(filter);
        return this;
    }

    public AssignmentListRequestBuilder withStatuses(List<AssignmentStatus> statuses) {
        data.getFilter().setStatuses(statuses);
        return this;
    }

    public AssignmentListRequestBuilder withPhoneNumber(AssignmentListRequest.PhoneNumber phoneNumber) {
        data.getFilter().setPhoneNumber(phoneNumber);
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
