package org.ays.auth.model.dto.request;

import org.ays.auth.model.enums.AdminRegistrationApplicationStatus;
import org.ays.auth.model.request.AdminRegistrationApplicationListRequest;
import org.ays.common.model.AysPaging;
import org.ays.common.model.AysPagingBuilder;
import org.ays.common.model.AysSorting;
import org.ays.common.model.TestDataBuilder;
import org.springframework.data.domain.Sort;

import java.util.List;

public class AdminRegistrationApplicationListRequestBuilder extends TestDataBuilder<AdminRegistrationApplicationListRequest> {

    public AdminRegistrationApplicationListRequestBuilder() {
        super(AdminRegistrationApplicationListRequest.class);
    }

    public AdminRegistrationApplicationListRequestBuilder withValidValues() {
        final AysSorting createdAtSort = AysSorting.builder()
                .property("createdAt")
                .direction(Sort.Direction.DESC)
                .build();

        return this
                .initializeFilter()
                .withStatuses(List.of(AdminRegistrationApplicationStatus.WAITING))
                .withPagination(new AysPagingBuilder().withValidValues().build())
                .withSort(List.of(createdAtSort));
    }

    private AdminRegistrationApplicationListRequestBuilder initializeFilter() {
        data.setFilter(new AdminRegistrationApplicationListRequest.Filter());
        return this;
    }

    public AdminRegistrationApplicationListRequestBuilder withFilter(AdminRegistrationApplicationListRequest.Filter filter) {
        data.setFilter(filter);
        return this;
    }

    public AdminRegistrationApplicationListRequestBuilder withStatuses(List<AdminRegistrationApplicationStatus> statuses) {
        data.getFilter().setStatuses(statuses);
        return this;
    }

    public AdminRegistrationApplicationListRequestBuilder withPagination(AysPaging aysPaging) {
        data.setPagination(aysPaging);
        return this;
    }

    public AdminRegistrationApplicationListRequestBuilder withSort(List<AysSorting> sorting) {
        data.setSort(sorting);
        return this;
    }

}
