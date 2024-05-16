package org.ays.admin_user.model;

import org.ays.admin_user.model.enums.AdminRegisterApplicationStatus;
import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomTestUtil;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.Institution;
import org.ays.institution.model.entity.InstitutionBuilder;
import org.ays.user.model.UserV2;
import org.ays.user.model.UserV2Builder;

public class AdminRegisterApplicationBuilder extends TestDataBuilder<AdminRegistrationApplication> {

    public AdminRegisterApplicationBuilder() {
        super(AdminRegistrationApplication.class);
    }

    public AdminRegisterApplicationBuilder withValidFields() {

        final Institution institution = new InstitutionBuilder()
                .withValidFields()
                .build();

        final UserV2 user = new UserV2Builder()
                .withValidFields()
                .build();

        return this
                .withId(AysRandomUtil.generateUUID())
                .withInstitution(institution)
                .withUser(user)
                .withReason(AysRandomTestUtil.generateString(41))
                .withStatus(AdminRegisterApplicationStatus.WAITING);
    }

    public AdminRegisterApplicationBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public AdminRegisterApplicationBuilder withInstitution(Institution institution) {
        data.setInstitution(institution);
        return this;
    }

    public AdminRegisterApplicationBuilder withReason(String reason) {
        data.setReason(reason);
        return this;
    }

    public AdminRegisterApplicationBuilder withStatus(AdminRegisterApplicationStatus status) {
        data.setStatus(status);
        return this;
    }

    public AdminRegisterApplicationBuilder withUser(UserV2 user) {
        data.setUser(user);
        return this;
    }

}
