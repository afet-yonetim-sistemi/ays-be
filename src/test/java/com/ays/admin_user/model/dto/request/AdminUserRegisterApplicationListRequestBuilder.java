package com.ays.admin_user.model.dto.request;

import com.ays.admin_user.model.enums.AdminUserRegisterApplicationStatus;
import com.ays.common.model.AysPaging;
import com.ays.common.model.AysPagingBuilder;
import com.ays.common.model.AysSorting;
import com.ays.common.model.TestDataBuilder;
import org.springframework.data.domain.Sort;

import java.util.List;

public class AdminUserRegisterApplicationListRequestBuilder extends TestDataBuilder<AdminUserRegisterApplicationListRequest> {

    public AdminUserRegisterApplicationListRequestBuilder() {
        super(AdminUserRegisterApplicationListRequest.class);
    }

    public AdminUserRegisterApplicationListRequestBuilder withValidValues() {
        final AysSorting createdAtSort = AysSorting.builder()
                .property("createdAt")
                .direction(Sort.Direction.DESC)
                .build();

        return this
                .initializeFilter()
                .withStatuses(List.of(AdminUserRegisterApplicationStatus.WAITING))
                .withPagination(new AysPagingBuilder().withValidValues().build())
                .withSort(List.of(createdAtSort));
    }

    private AdminUserRegisterApplicationListRequestBuilder initializeFilter() {
        data.setFilter(new AdminUserRegisterApplicationListRequest.Filter());
        return this;
    }

    public AdminUserRegisterApplicationListRequestBuilder withFilter(AdminUserRegisterApplicationListRequest.Filter filter) {
        data.setFilter(filter);
        return this;
    }

    public AdminUserRegisterApplicationListRequestBuilder withStatuses(List<AdminUserRegisterApplicationStatus> statuses) {
        data.getFilter().setStatuses(statuses);
        return this;
    }

    public AdminUserRegisterApplicationListRequestBuilder withPagination(AysPaging aysPaging) {
        data.setPagination(aysPaging);
        return this;
    }

    public AdminUserRegisterApplicationListRequestBuilder withSort(List<AysSorting> sorting) {
        data.setSort(sorting);
        return this;
    }

}
