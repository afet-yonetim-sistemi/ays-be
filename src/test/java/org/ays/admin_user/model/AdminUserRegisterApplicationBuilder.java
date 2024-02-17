package org.ays.admin_user.model;

import org.ays.admin_user.model.enums.AdminUserRegisterApplicationStatus;
import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomTestUtil;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.Institution;
import org.ays.institution.model.entity.InstitutionBuilder;

public class AdminUserRegisterApplicationBuilder extends TestDataBuilder<AdminUserRegisterApplication> {

    public AdminUserRegisterApplicationBuilder() {
        super(AdminUserRegisterApplication.class);
    }

    public AdminUserRegisterApplicationBuilder withValidFields() {
        final Institution institution = new InstitutionBuilder()
                .withValidFields()
                .build();
        final AdminUser adminUser = new AdminUserBuilder()
                .withValidFields()
                .build();

        return this
                .withId(AysRandomUtil.generateUUID())
                .withInstitution(institution)
                .withAdminUser(adminUser)
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

    public AdminUserRegisterApplicationBuilder withAdminUser(AdminUser adminUser) {
        data.setAdminUser(adminUser);
        return this;
    }
}
