package org.ays.admin_user.model;

import org.ays.admin_user.model.enums.AdminUserStatus;
import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.Institution;
import org.ays.institution.model.entity.InstitutionBuilder;

@Deprecated(since = "AdminUserBuilder V2 Production'a alınınca burası silinecektir.", forRemoval = true)
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
