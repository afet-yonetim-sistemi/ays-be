package com.ays.admin_user.model;

import com.ays.admin_user.model.enums.AdminUserStatus;
import com.ays.common.model.TestDataBuilder;
import com.ays.common.util.AysRandomUtil;
import com.ays.institution.model.Institution;
import com.ays.institution.model.entity.InstitutionBuilder;

public class AdminUserBuilder extends TestDataBuilder<AdminUser> {

    public AdminUserBuilder() {
        super(AdminUser.class);
    }

    public AdminUserBuilder withValidFields() {
        final Institution institution = new InstitutionBuilder()
                .withValidFields()
                .build();

        return this
                .withId(AysRandomUtil.generateUUID())
                .withInstitution(institution)
                .withStatus(AdminUserStatus.ACTIVE);
    }

    public AdminUserBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public AdminUserBuilder withInstitution(Institution institution) {
        data.setInstitution(institution);
        return this;
    }

    public AdminUserBuilder withStatus(AdminUserStatus status) {
        data.setStatus(status);
        return this;
    }
}
