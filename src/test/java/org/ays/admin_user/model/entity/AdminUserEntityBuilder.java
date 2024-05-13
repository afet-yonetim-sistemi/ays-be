package org.ays.admin_user.model.entity;

import org.ays.admin_user.model.enums.AdminRole;
import org.ays.admin_user.model.enums.AdminUserStatus;
import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.AysPhoneNumberBuilder;
import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.institution.model.entity.InstitutionEntityBuilder;

import java.util.ArrayList;
import java.util.List;

@Deprecated(since = "AdminUserEntityBuilder V2 Production'a alınınca burası silinecektir.", forRemoval = true)
public class AdminUserEntityBuilder extends TestDataBuilder<AdminUserEntity> {

    public AdminUserEntityBuilder() {
        super(AdminUserEntity.class);
    }

    public static List<AdminUserEntity> generateValidUserEntities(int size) {
        List<AdminUserEntity> userEntities = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            AdminUserEntity userEntity = new AdminUserEntityBuilder().withValidFields().build();
            userEntities.add(userEntity);
        }
        return userEntities;
    }

    public AdminUserEntityBuilder withValidFields() {
        InstitutionEntity institutionEntity = new InstitutionEntityBuilder().withValidFields().build();
        final String id = AysRandomUtil.generateUUID();

        return this
                .withId(id)
                .withUsername(String.valueOf(AysRandomUtil.generateNumber(6)))
                .withPassword("$2a$10$H/lKEaKsusQztOaJmYTAi.4MAmjvnxWOh0DY.XrgwHy5D2gENVIky")
                .withPhoneNumber(new AysPhoneNumberBuilder().withValidFields().build())
                .withEmail(id + "@ays.com")
                .withStatus(AdminUserStatus.ACTIVE)
                .withInstitutionId(institutionEntity.getId())
                .withInstitution(institutionEntity);
    }

    public AdminUserEntityBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public AdminUserEntityBuilder withUsername(String username) {
        data.setUsername(username);
        return this;
    }

    public AdminUserEntityBuilder withPassword(String password) {
        data.setPassword(password);
        return this;
    }

    public AdminUserEntityBuilder withEmail(String email) {
        data.setEmail(email);
        return this;
    }

    public AdminUserEntityBuilder withPhoneNumber(AysPhoneNumber phoneNumber) {
        data.setCountryCode(phoneNumber.getCountryCode());
        data.setLineNumber(phoneNumber.getLineNumber());
        return this;
    }

    public AdminUserEntityBuilder withStatus(AdminUserStatus status) {
        data.setStatus(status);
        return this;
    }

    public AdminUserEntityBuilder withRole(AdminRole adminRole) {
        data.setRole(adminRole);
        return this;
    }

    public AdminUserEntityBuilder withInstitutionId(String institutionId) {
        data.setInstitutionId(institutionId);
        return this;
    }

    public AdminUserEntityBuilder withInstitution(InstitutionEntity institution) {
        data.setInstitution(institution);
        return this;
    }

}
