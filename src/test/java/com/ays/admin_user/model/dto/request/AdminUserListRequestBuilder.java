package com.ays.admin_user.model.dto.request;

import com.ays.common.model.AysPaging;
import com.ays.common.model.AysPagingBuilder;
import com.ays.common.model.AysSorting;
import com.ays.common.model.TestDataBuilder;

import java.util.List;

public class AdminUserListRequestBuilder extends TestDataBuilder<AdminUserListRequest> {

    public AdminUserListRequestBuilder() {
        super(AdminUserListRequest.class);
    }

    public AdminUserListRequestBuilder withValidValues() {
        return this
                .withPagination(new AysPagingBuilder().withValidValues().build())
                .withSort(null);
    }

    public AdminUserListRequestBuilder withPagination(AysPaging aysPaging) {
        data.setPagination(aysPaging);
        return this;
    }

    public AdminUserListRequestBuilder withSort(List<AysSorting> sorting) {
        data.setSort(sorting);
        return this;
    }
}
