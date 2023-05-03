package com.ays.user.model.dto.request;

import com.ays.common.model.AysPaging;
import com.ays.common.model.AysPagingBuilder;
import com.ays.common.model.TestDataBuilder;

public class UserListRequestBuilder extends TestDataBuilder<UserListRequest> {

    public UserListRequestBuilder() {
        super(UserListRequest.class);
    }

    public static final UserListRequestBuilder VALID = new UserListRequestBuilder()
            .withPagination(AysPagingBuilder.VALID.build());

    public UserListRequestBuilder withPagination(AysPaging aysPaging) {
        data.setPagination(aysPaging);
        return this;
    }

}
