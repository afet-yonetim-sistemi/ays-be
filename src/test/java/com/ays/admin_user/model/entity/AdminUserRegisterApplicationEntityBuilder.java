package com.ays.admin_user.model.entity;

import com.ays.admin_user.model.enums.AdminUserRegisterApplicationStatus;
import com.ays.common.model.TestDataBuilder;

public class AdminUserRegisterApplicationEntityBuilder extends TestDataBuilder<AdminUserRegisterApplicationEntity> {

    public AdminUserRegisterApplicationEntityBuilder() {
        super(AdminUserRegisterApplicationEntity.class);
    }

    public AdminUserRegisterApplicationEntityBuilder withValidFields() {
        return this
                .withStatus(AdminUserRegisterApplicationStatus.WAITING);
    }

    public AdminUserRegisterApplicationEntityBuilder withStatus(AdminUserRegisterApplicationStatus status){
        data.setStatus(status);
        return this;
    }
}
