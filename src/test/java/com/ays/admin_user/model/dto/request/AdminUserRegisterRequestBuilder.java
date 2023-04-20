package com.ays.admin_user.model.dto.request;

import com.ays.common.model.TestDataBuilder;

public class AdminUserRegisterRequestBuilder extends TestDataBuilder<AdminUserRegisterRequest> {

    public AdminUserRegisterRequestBuilder() {
        super(AdminUserRegisterRequest.class);
    }

    public AdminUserRegisterRequestBuilder withEmail(String email) {
        data.setEmail(email);
        return this;
    }

}
