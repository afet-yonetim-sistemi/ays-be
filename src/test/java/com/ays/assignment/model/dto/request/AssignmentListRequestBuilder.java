package com.ays.assignment.model.dto.request;

import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.common.model.AysPaging;
import com.ays.common.model.AysPagingBuilder;
import com.ays.common.model.AysSorting;
import com.ays.common.model.TestDataBuilder;
import com.ays.common.model.dto.request.AysPhoneNumberFilterRequest;
import com.ays.common.model.dto.request.AysPhoneNumberFilterRequestBuilder;

import java.util.List;

public class AssignmentListRequestBuilder extends TestDataBuilder<AssignmentListRequest> {

    public AssignmentListRequestBuilder() {
        super(AssignmentListRequest.class);
    }

    public AssignmentListRequestBuilder withValidValues() {
        return this
                .withFilter(new FilterBuilder().withValidValues().build())
                .withPagination(new AysPagingBuilder().withValidValues().build())
                .withSort(null);
    }

    public AssignmentListRequestBuilder withFilter(AssignmentListRequest.Filter filter) {
        data.setFilter(filter);
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

    public static class FilterBuilder extends TestDataBuilder<AssignmentListRequest.Filter> {

        public FilterBuilder() {
            super(AssignmentListRequest.Filter.class);
        }

        public FilterBuilder withValidValues() {
            return this
                    .withStatuses(List.of(AssignmentStatus.AVAILABLE, AssignmentStatus.ASSIGNED))
                    .withPhoneNumber(new AysPhoneNumberFilterRequestBuilder().withValidValues().build());
        }

        public FilterBuilder withStatuses(List<AssignmentStatus> statuses) {
            data.setStatuses(statuses);
            return this;
        }

        public FilterBuilder withPhoneNumber(AysPhoneNumberFilterRequest phoneNumber) {
            data.setPhoneNumber(phoneNumber);
            return this;
        }
    }
}
