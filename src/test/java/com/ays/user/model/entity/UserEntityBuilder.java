package com.ays.user.model.entity;

import com.ays.common.model.AysPhoneNumber;
import com.ays.common.model.AysPhoneNumberBuilder;
import com.ays.common.model.TestDataBuilder;
import com.ays.common.util.AysRandomUtil;
import com.ays.institution.model.entity.InstitutionEntity;
import com.ays.institution.model.entity.InstitutionEntityBuilder;
import com.ays.user.model.enums.UserRole;
import com.ays.user.model.enums.UserStatus;

import java.util.ArrayList;
import java.util.List;

public class UserEntityBuilder extends TestDataBuilder<UserEntity> {

    public UserEntityBuilder() {
        super(UserEntity.class);
    }

    public static List<UserEntity> generateValidUserEntities(int size) {
        List<UserEntity> userEntities = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            UserEntity userEntity = new UserEntityBuilder().withValidFields().build();
            userEntities.add(userEntity);
        }
        return userEntities;
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
                .withInstitutionId(institutionEntity.getId())
                .withOrganization(institutionEntity);
    }

    public UserEntityBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public UserEntityBuilder withInstitutionId(String institutionId) {
        data.setInstitutionId(institutionId);
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

    public UserEntityBuilder withPhoneNumber(AysPhoneNumber phoneNumber) {
        data.setCountryCode(phoneNumber.getCountryCode());
        data.setLineNumber(phoneNumber.getLineNumber());
        return this;
    }

    public UserEntityBuilder withRole(UserRole role) {
        data.setRole(role);
        return this;
    }

    public UserEntityBuilder withOrganization(InstitutionEntity institution) {
        data.setInstitution(institution);
        return this;
    }

}
