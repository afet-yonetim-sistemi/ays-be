package org.ays.admin_user.model.dto.request;

import org.ays.admin_user.model.enums.AdminRegistrationApplicationStatus;
import org.ays.common.model.AysPaging;
import org.ays.common.model.AysPagingBuilder;
import org.ays.common.model.AysSorting;
import org.ays.common.model.TestDataBuilder;
import org.springframework.data.domain.Sort;

import java.util.List;

public class AdminRegisterApplicationListRequestBuilder extends TestDataBuilder<AdminRegisterApplicationListRequest> {

    public AdminRegisterApplicationListRequestBuilder() {
        super(AdminRegisterApplicationListRequest.class);
    }

    public AdminRegisterApplicationListRequestBuilder withValidValues() {
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

    private AdminRegisterApplicationListRequestBuilder initializeFilter() {
        data.setFilter(new AdminRegisterApplicationListRequest.Filter());
        return this;
    }

    public AdminRegisterApplicationListRequestBuilder withFilter(AdminRegisterApplicationListRequest.Filter filter) {
        data.setFilter(filter);
        return this;
    }

    public AdminRegisterApplicationListRequestBuilder withStatuses(List<AdminRegistrationApplicationStatus> statuses) {
        data.getFilter().setStatuses(statuses);
        return this;
    }

    public AdminRegisterApplicationListRequestBuilder withPagination(AysPaging aysPaging) {
        data.setPagination(aysPaging);
        return this;
    }

    public AdminRegisterApplicationListRequestBuilder withSort(List<AysSorting> sorting) {
        data.setSort(sorting);
        return this;
    }

}
