package com.ays.user.controller.payload.request;

import com.ays.admin_user.controller.dto.request.AdminUserRegisterRequest;
import com.ays.common.model.TestDataBuilder;

public class AdminRegisterRequestBuilder extends TestDataBuilder<AdminUserRegisterRequest> {

    public AdminRegisterRequestBuilder() {
        super(AdminUserRegisterRequest.class);
    }
}
