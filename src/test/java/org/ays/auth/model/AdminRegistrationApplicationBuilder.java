package org.ays.auth.model;

import org.ays.auth.model.enums.AdminRegistrationApplicationStatus;
import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.Institution;
import org.ays.institution.model.InstitutionBuilder;

public class AdminRegistrationApplicationBuilder extends TestDataBuilder<AdminRegistrationApplication> {

    public AdminRegistrationApplicationBuilder() {
        super(AdminRegistrationApplication.class);
    }

    public AdminRegistrationApplicationBuilder withValidValues() {

        final Institution institution = new InstitutionBuilder()
                .withValidValues()
                .build();

        final AysUser user = new AysUserBuilder()
                .withValidValues()
                .build();

        return this
                .withId(AysRandomUtil.generateUUID())
                .withInstitution(institution)
                .withUser(user)
                .withReason(AysRandomUtil.generateText(41))
                .withStatus(AdminRegistrationApplicationStatus.WAITING);
    }

    public AdminRegistrationApplicationBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public AdminRegistrationApplicationBuilder withoutId() {
        data.setId(null);
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

    public AdminRegistrationApplicationBuilder withUser(AysUser user) {
        data.setUser(user);
        return this;
    }

    public AdminRegistrationApplicationBuilder withoutUser() {
        data.setUser(null);
        return this;
    }

}
