package com.ays.admin_user.model.entity;

import com.ays.admin_user.model.enums.AdminUserStatus;
import com.ays.common.model.TestDataBuilder;

public class AdminUserEntityBuilder extends TestDataBuilder<AdminUserEntity> {

    public AdminUserEntityBuilder() {
        super(AdminUserEntity.class);
    }

    public AdminUserEntityBuilder withStatus(AdminUserStatus status) {
        data.setStatus(status);
        return this;
    }

}
