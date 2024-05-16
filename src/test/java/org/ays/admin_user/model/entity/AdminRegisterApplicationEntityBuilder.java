package org.ays.admin_user.model.entity;

import org.ays.admin_user.model.enums.AdminRegistrationApplicationStatus;
import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomTestUtil;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.institution.model.entity.InstitutionEntityBuilder;
import org.ays.user.model.entity.AdminRegistrationApplicationEntity;
import org.ays.user.model.entity.UserEntityV2;
import org.ays.user.model.entity.UserEntityV2Builder;

public class AdminRegisterApplicationEntityBuilder extends TestDataBuilder<AdminRegistrationApplicationEntity> {

    public AdminRegisterApplicationEntityBuilder() {
        super(AdminRegistrationApplicationEntity.class);
    }

    public AdminRegisterApplicationEntityBuilder withValidFields() {
        final UserEntityV2 userEntity = new UserEntityV2Builder()
                .withValidFields()
                .build();

        final InstitutionEntity institutionEntity = new InstitutionEntityBuilder()
                .withValidFields()
                .build();

        return this
                .withId(AysRandomUtil.generateUUID())
                .withStatus(AdminRegistrationApplicationStatus.WAITING)
                .withUser(userEntity)
                .withInstitution(institutionEntity)
                .withReason(AysRandomTestUtil.generateString(41));
    }

    public AdminRegisterApplicationEntityBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public AdminRegisterApplicationEntityBuilder withStatus(AdminRegistrationApplicationStatus status) {
        data.setStatus(status);
        return this;
    }

    public AdminRegisterApplicationEntityBuilder withUserId(String userId) {
        data.setUserId(userId);
        data.setUser(null);
        return this;
    }

    public AdminRegisterApplicationEntityBuilder withUser(UserEntityV2 userEntity) {
        data.setUserId(userEntity.getId());
        data.setUser(userEntity);
        return this;
    }

    public AdminRegisterApplicationEntityBuilder withInstitutionId(String institutionId) {
        data.setInstitutionId(institutionId);
        data.setInstitution(null);
        return this;
    }

    public AdminRegisterApplicationEntityBuilder withInstitution(InstitutionEntity institutionEntity) {
        data.setInstitutionId(institutionEntity.getId());
        data.setInstitution(institutionEntity);
        return this;
    }

    public AdminRegisterApplicationEntityBuilder withReason(String reason) {
        data.setReason(reason);
        return this;
    }

}
