package org.ays.user.model.entity;

import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomTestUtil;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.institution.model.entity.InstitutionEntityBuilder;
import org.ays.user.model.enums.AdminRegistrationApplicationStatus;

public class AdminRegistrationApplicationEntityBuilder extends TestDataBuilder<AdminRegistrationApplicationEntity> {

    public AdminRegistrationApplicationEntityBuilder() {
        super(AdminRegistrationApplicationEntity.class);
    }

    public AdminRegistrationApplicationEntityBuilder withValidFields() {
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

    public AdminRegistrationApplicationEntityBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public AdminRegistrationApplicationEntityBuilder withStatus(AdminRegistrationApplicationStatus status) {
        data.setStatus(status);
        return this;
    }

    public AdminRegistrationApplicationEntityBuilder withUserId(String userId) {
        data.setUserId(userId);
        data.setUser(null);
        return this;
    }

    public AdminRegistrationApplicationEntityBuilder withUser(UserEntityV2 userEntity) {
        data.setUserId(userEntity.getId());
        data.setUser(userEntity);
        return this;
    }

    public AdminRegistrationApplicationEntityBuilder withInstitutionId(String institutionId) {
        data.setInstitutionId(institutionId);
        data.setInstitution(null);
        return this;
    }

    public AdminRegistrationApplicationEntityBuilder withInstitution(InstitutionEntity institutionEntity) {
        data.setInstitutionId(institutionEntity.getId());
        data.setInstitution(institutionEntity);
        return this;
    }

    public AdminRegistrationApplicationEntityBuilder withReason(String reason) {
        data.setReason(reason);
        return this;
    }

}
