package com.ays.admin_user.model.entity;

import com.ays.admin_user.model.enums.AdminUserRegisterVerificationStatus;
import com.ays.common.model.TestDataBuilder;

public class AdminUserRegisterVerificationEntityBuilder extends TestDataBuilder<AdminUserRegisterVerificationEntity> {

    public AdminUserRegisterVerificationEntityBuilder() {
        super(AdminUserRegisterVerificationEntity.class);
    }

    public AdminUserRegisterVerificationEntityBuilder withValidFields() {
        return this
                .withStatus(AdminUserRegisterVerificationStatus.WAITING);
    }

    public AdminUserRegisterVerificationEntityBuilder withStatus(AdminUserRegisterVerificationStatus status){
        data.setStatus(status);
        return this;
    }
}
