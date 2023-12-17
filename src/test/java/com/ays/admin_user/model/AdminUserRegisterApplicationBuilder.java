package com.ays.admin_user.model;

import com.ays.admin_user.model.enums.AdminUserRegisterApplicationStatus;
import com.ays.common.model.TestDataBuilder;
import com.ays.common.util.AysRandomTestUtil;
import com.ays.common.util.AysRandomUtil;
import com.ays.institution.model.Institution;
import com.ays.institution.model.entity.InstitutionBuilder;

public class AdminUserRegisterApplicationBuilder extends TestDataBuilder<AdminUserRegisterApplication> {

    public AdminUserRegisterApplicationBuilder() {
        super(AdminUserRegisterApplication.class);
    }

    public AdminUserRegisterApplicationBuilder withValidFields() {
        final Institution institution = new InstitutionBuilder()
                .withValidFields()
                .build();

        return this
                .withId(AysRandomUtil.generateUUID())
                .withInstitution(institution)
                .withReason(AysRandomTestUtil.generateString(41))
                .withStatus(AdminUserRegisterApplicationStatus.WAITING);
    }

    public AdminUserRegisterApplicationBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public AdminUserRegisterApplicationBuilder withInstitution(Institution institution) {
        data.setInstitution(institution);
        return this;
    }

    public AdminUserRegisterApplicationBuilder withReason(String reason) {
        data.setReason(reason);
        return this;
    }

    public AdminUserRegisterApplicationBuilder withStatus(AdminUserRegisterApplicationStatus status) {
        data.setStatus(status);
        return this;
    }
}
