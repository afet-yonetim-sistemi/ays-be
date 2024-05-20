package org.ays.user.model;

import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomTestUtil;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.Institution;
import org.ays.institution.model.entity.InstitutionBuilder;
import org.ays.user.model.enums.AdminRegistrationApplicationStatus;

public class AdminRegistrationApplicationBuilder extends TestDataBuilder<AdminRegistrationApplication> {

    public AdminRegistrationApplicationBuilder() {
        super(AdminRegistrationApplication.class);
    }

    public AdminRegistrationApplicationBuilder withValidFields() {

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
                .withStatus(AdminRegistrationApplicationStatus.WAITING);
    }

    public AdminRegistrationApplicationBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public AdminRegistrationApplicationBuilder withInstitution(Institution institution) {
        data.setInstitution(institution);
        return this;
    }

    public AdminRegistrationApplicationBuilder withReason(String reason) {
        data.setReason(reason);
        return this;
    }

    public AdminRegistrationApplicationBuilder withStatus(AdminRegistrationApplicationStatus status) {
        data.setStatus(status);
        return this;
    }

    public AdminRegistrationApplicationBuilder withUser(UserV2 user) {
        data.setUser(user);
        return this;
    }

}
