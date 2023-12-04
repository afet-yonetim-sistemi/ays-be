package com.ays.admin_user.model;

import com.ays.common.model.TestDataBuilder;

public class AdminUserRegisterApplicationBuilder extends TestDataBuilder<AdminUserRegisterApplication> {

    public AdminUserRegisterApplicationBuilder() {
        super(AdminUserRegisterApplication.class);
    }

    public AdminUserRegisterApplicationBuilder withId(String id) {
        data.setId(id);
        return this;
    }

}
