package org.ays.auth.model;

import org.ays.auth.model.enums.UserStatus;
import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.Institution;
import org.ays.institution.model.entity.InstitutionBuilder;

public class UserV2Builder extends TestDataBuilder<UserV2> {

    public UserV2Builder() {
        super(UserV2.class);
    }

    public UserV2Builder withValidFields() {
        final Institution institution = new InstitutionBuilder()
                .withValidFields()
                .build();

        return this
                .withId(AysRandomUtil.generateUUID())
                .withInstitution(institution)
                .withEmailAddress("test@afetyonetimsistemi.org")
                .withStatus(UserStatus.ACTIVE);
    }

    public UserV2Builder withId(String id) {
        data.setId(id);
        return this;
    }

    public UserV2Builder withInstitution(Institution institution) {
        data.setInstitution(institution);
        return this;
    }

    public UserV2Builder withEmailAddress(String emailAddress) {
        data.setEmailAddress(emailAddress);
        return this;
    }

    public UserV2Builder withPassword(String password) {
        data.setPassword(password);
        return this;
    }

    public UserV2Builder withStatus(UserStatus status) {
        data.setStatus(status);
        return this;
    }

}
