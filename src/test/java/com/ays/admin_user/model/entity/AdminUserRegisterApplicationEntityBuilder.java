package com.ays.admin_user.model.entity;

import com.ays.admin_user.model.enums.AdminUserRegisterApplicationStatus;
import com.ays.common.model.TestDataBuilder;
import com.ays.common.util.AysRandomUtil;
import com.ays.institution.model.entity.InstitutionEntity;
import com.ays.institution.model.entity.InstitutionEntityBuilder;

public class AdminUserRegisterApplicationEntityBuilder extends TestDataBuilder<AdminUserRegisterApplicationEntity> {

    public AdminUserRegisterApplicationEntityBuilder() {
        super(AdminUserRegisterApplicationEntity.class);
    }

    public AdminUserRegisterApplicationEntityBuilder withValidFields() {
        final AdminUserEntity adminUser = new AdminUserEntityBuilder()
                .withValidFields()
                .build();

        final InstitutionEntity institution = new InstitutionEntityBuilder()
                .withValidFields()
                .build();

        return this
                .withId(AysRandomUtil.generateUUID())
                .withStatus(AdminUserRegisterApplicationStatus.WAITING)
                .withAdminUserId(adminUser.getId())
                .withAdminUser(adminUser)
                .withInstitutionId(institution.getId())
                .withInstitution(institution)
                .withReason(AysRandomUtil.generateString(41));
    }

    public AdminUserRegisterApplicationEntityBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public AdminUserRegisterApplicationEntityBuilder withStatus(AdminUserRegisterApplicationStatus status) {
        data.setStatus(status);
        return this;
    }

    public AdminUserRegisterApplicationEntityBuilder withAdminUserId(String adminUserId) {
        data.setAdminUserId(adminUserId);
        return this;
    }

    public AdminUserRegisterApplicationEntityBuilder withAdminUser(AdminUserEntity adminUser) {
        data.setAdminUser(adminUser);
        return this;
    }

    public AdminUserRegisterApplicationEntityBuilder withInstitutionId(String institutionId) {
        data.setInstitutionId(institutionId);
        return this;
    }

    public AdminUserRegisterApplicationEntityBuilder withInstitution(InstitutionEntity institution) {
        data.setInstitution(institution);
        return this;
    }

    public AdminUserRegisterApplicationEntityBuilder withReason(String reason) {
        data.setReason(reason);
        return this;
    }

}
