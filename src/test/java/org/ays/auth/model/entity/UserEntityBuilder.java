package org.ays.auth.model.entity;

import org.ays.auth.model.enums.UserRole;
import org.ays.auth.model.enums.UserStatus;
import org.ays.auth.model.enums.UserSupportStatus;
import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.AysPhoneNumberBuilder;
import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.institution.model.entity.InstitutionEntityBuilder;

public class UserEntityBuilder extends TestDataBuilder<UserEntity> {

    public UserEntityBuilder() {
        super(UserEntity.class);
    }

    public UserEntityBuilder withValidFields() {
        InstitutionEntity institutionEntity = new InstitutionEntityBuilder().withValidFields().build();
        return this
                .withId(AysRandomUtil.generateUUID())
                .withUsername(String.valueOf(AysRandomUtil.generateNumber(6)))
                .withPassword("$2a$10$H/lKEaKsusQztOaJmYTAi.4MAmjvnxWOh0DY.XrgwHy5D2gENVIky")
                .withPhoneNumber(new AysPhoneNumberBuilder().withValidFields().build())
                .withRole(UserRole.VOLUNTEER)
                .withStatus(UserStatus.ACTIVE)
                .withSupportStatus(UserSupportStatus.IDLE)
                .withInstitution(institutionEntity);
    }

    public UserEntityBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public UserEntityBuilder withUsername(String username) {
        data.setUsername(username);
        return this;
    }

    public UserEntityBuilder withPassword(String password) {
        data.setPassword(password);
        return this;
    }

    public UserEntityBuilder withStatus(UserStatus status) {
        data.setStatus(status);
        return this;
    }

    public UserEntityBuilder withSupportStatus(UserSupportStatus userSupportStatus) {
        data.setSupportStatus(userSupportStatus);
        return this;
    }

    public UserEntityBuilder withPhoneNumber(AysPhoneNumber phoneNumber) {
        data.setCountryCode(phoneNumber.getCountryCode());
        data.setLineNumber(phoneNumber.getLineNumber());
        return this;
    }

    public UserEntityBuilder withRole(UserRole role) {
        data.setRole(role);
        return this;
    }

    public UserEntityBuilder withInstitutionId(String institutionId) {
        data.setInstitutionId(institutionId);
        return this;
    }

    public UserEntityBuilder withInstitution(InstitutionEntity institution) {
        data.setInstitution(institution);
        return this;
    }

}
