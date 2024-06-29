package org.ays.auth.model.entity;

import org.ays.auth.model.enums.AysUserStatus;
import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.AysPhoneNumberBuilder;
import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.institution.model.entity.InstitutionEntityBuilder;
import org.ays.util.AysValidTestData;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;
import java.util.List;

public class AysUserEntityBuilder extends TestDataBuilder<AysUserEntity> {

    public AysUserEntityBuilder() {
        super(AysUserEntity.class);
    }

    public AysUserEntityBuilder withValidValues() {
        String id = AysRandomUtil.generateUUID();

        AysUserEntity.PasswordEntity passwordEntity = new PasswordEntityBuilder()
                .withValidValues()
                .withValue(AysValidTestData.PASSWORD_ENCRYPTED)
                .build();

        AysUserEntity.LoginAttemptEntity loginAttemptEntity = new LoginAttemptEntityBuilder()
                .withValidValues()
                .build();

        List<AysRoleEntity> roleEntities = List.of(
                new AysRoleEntityBuilder().withValidValues().build()
        );

        InstitutionEntity institutionEntity = new InstitutionEntityBuilder().withValidValues().build();

        return this
                .withId(id)
                .withPhoneNumber(new AysPhoneNumberBuilder().withValidValues().build())
                .withEmailAddress(RandomStringUtils.randomAlphabetic(8).concat("@afetyonetimsistemi.org"))
                .withPassword(passwordEntity)
                .withLoginAttempt(loginAttemptEntity)
                .withRoles(roleEntities)
                .withStatus(AysUserStatus.ACTIVE)
                .withInstitution(institutionEntity);
    }

    public AysUserEntityBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public AysUserEntityBuilder withEmailAddress(String emailAddress) {
        data.setEmailAddress(emailAddress);
        return this;
    }

    public AysUserEntityBuilder withStatus(AysUserStatus status) {
        data.setStatus(status);
        return this;
    }

    public AysUserEntityBuilder withPhoneNumber(AysPhoneNumber phoneNumber) {
        data.setCountryCode(phoneNumber.getCountryCode());
        data.setLineNumber(phoneNumber.getLineNumber());
        return this;
    }

    public AysUserEntityBuilder withPassword(AysUserEntity.PasswordEntity password) {
        data.setPassword(password);
        return this;
    }

    public AysUserEntityBuilder withLoginAttempt(AysUserEntity.LoginAttemptEntity loginAttempt) {
        data.setLoginAttempt(loginAttempt);
        return this;
    }

    public AysUserEntityBuilder withRoles(List<AysRoleEntity> roles) {
        data.setRoles(roles);
        return this;
    }

    public AysUserEntityBuilder withInstitution(InstitutionEntity institution) {
        data.setInstitution(institution);
        return this;
    }

    public static class PasswordEntityBuilder extends TestDataBuilder<AysUserEntity.PasswordEntity> {

        public PasswordEntityBuilder() {
            super(AysUserEntity.PasswordEntity.class);
        }

        public PasswordEntityBuilder withValidValues() {
            return this
                    .withValue(AysValidTestData.PASSWORD_ENCRYPTED);
        }

        public PasswordEntityBuilder withValue(String password) {
            data.setValue(password);
            return this;
        }

    }

    public static class LoginAttemptEntityBuilder extends TestDataBuilder<AysUserEntity.LoginAttemptEntity> {

        public LoginAttemptEntityBuilder() {
            super(AysUserEntity.LoginAttemptEntity.class);
        }

        public LoginAttemptEntityBuilder withValidValues() {
            return this
                    .withLastLoginAt(LocalDateTime.now());
        }

        public LoginAttemptEntityBuilder withLastLoginAt(LocalDateTime lastLoginAt) {
            data.setLastLoginAt(lastLoginAt);
            return this;
        }

    }

}
