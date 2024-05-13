package org.ays.user.model.entity;

import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.TestDataBuilder;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.institution.model.entity.InstitutionEntityBuilder;
import org.ays.user.model.enums.UserStatus;
import org.ays.util.AysValidTestData;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.util.Set;
import java.util.UUID;

public class UserEntityV2Builder extends TestDataBuilder<UserEntityV2> {

    public UserEntityV2Builder() {
        super(UserEntityV2.class);
    }

    public UserEntityV2Builder withValidFields() {
        String id = UUID.randomUUID().toString();

        UserEntityV2.PasswordEntity passwordEntity = new PasswordEntityBuilder()
                .withValidFields()
                .withUserId(id)
                .withValue(AysValidTestData.PASSWORD_ENCRYPTED)
                .build();

        Set<RoleEntity> roles = Set.of(
                new RoleEntityBuilder().withValidFields().build()
        );

        InstitutionEntity institutionEntity = new InstitutionEntityBuilder().withValidFields().build();

        return this
                .withId(id)
                .withEmailAddress(RandomStringUtils.randomAlphabetic(8).concat("@afetyonetimsistemi.org"))
                .withPassword(passwordEntity)
                .withRoles(roles)
                .withStatus(UserStatus.ACTIVE)
                .withInstitution(institutionEntity);
    }

    public UserEntityV2Builder withId(String id) {
        data.setId(id);
        return this;
    }

    public UserEntityV2Builder withEmailAddress(String emailAddress) {
        data.setEmailAddress(emailAddress);
        return this;
    }

    public UserEntityV2Builder withStatus(UserStatus status) {
        data.setStatus(status);
        return this;
    }

    public UserEntityV2Builder withPhoneNumber(AysPhoneNumber phoneNumber) {
        data.setCountryCode(phoneNumber.getCountryCode());
        data.setLineNumber(phoneNumber.getLineNumber());
        return this;
    }

    public UserEntityV2Builder withPassword(UserEntityV2.PasswordEntity password) {
        data.setPassword(password);
        return this;
    }

    public UserEntityV2Builder withRoles(Set<RoleEntity> roles) {
        data.setRoles(roles);
        return this;
    }

    public UserEntityV2Builder withInstitutionId(String institutionId) {
        data.setInstitutionId(institutionId);
        return this;
    }

    public UserEntityV2Builder withInstitution(InstitutionEntity institution) {
        data.setInstitution(institution);
        return this;
    }

    private static class PasswordEntityBuilder extends TestDataBuilder<UserEntityV2.PasswordEntity> {

        public PasswordEntityBuilder() {
            super(UserEntityV2.PasswordEntity.class);
        }

        public PasswordEntityBuilder withValidFields() {
            return this
                    .withUserId(UUID.randomUUID().toString())
                    .withValue(AysValidTestData.PASSWORD_ENCRYPTED);
        }

        public PasswordEntityBuilder withUserId(String userId) {
            data.setUserId(userId);
            return this;
        }

        public PasswordEntityBuilder withValue(String password) {
            data.setValue(password);
            return this;
        }

    }

}
