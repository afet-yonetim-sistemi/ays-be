package org.ays.auth.model;

import org.ays.auth.model.enums.AysUserStatus;
import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.AysPhoneNumberBuilder;
import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.Institution;
import org.ays.institution.model.InstitutionBuilder;
import org.ays.util.AysValidTestData;

import java.time.LocalDateTime;
import java.util.Set;

public class AysUserBuilder extends TestDataBuilder<AysUser> {

    public AysUserBuilder() {
        super(AysUser.class);
    }

    public AysUserBuilder withValidValues() {
        final Institution institution = new InstitutionBuilder()
                .withValidValues()
                .build();

        return this
                .withId(AysRandomUtil.generateUUID())
                .withInstitution(institution)
                .withEmailAddress("@afetyonetimsistemi.org")
                .withPhoneNumber(new AysPhoneNumberBuilder().withValidValues().build())
                .withStatus(AysUserStatus.ACTIVE)
                .withPassword(null)
                .withLoginAttempt(null);
    }

    public AysUserBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public AysUserBuilder withoutId() {
        data.setId(null);
        return this;
    }

    public AysUserBuilder withEmailAddress(String emailAddress) {
        data.setEmailAddress(data.getEmailAddress() + emailAddress);
        return this;
    }

    public AysUserBuilder withPhoneNumber(AysPhoneNumber phoneNumber) {
        data.setPhoneNumber(phoneNumber);
        return this;
    }

    public AysUserBuilder withStatus(AysUserStatus status) {
        data.setStatus(status);
        return this;
    }

    public AysUserBuilder withPassword(AysUser.Password password) {
        data.setPassword(password);
        return this;
    }

    public AysUserBuilder withValidPassword() {
        data.setPassword(new PasswordBuilder().withValidValues().build());
        return this;
    }

    public AysUserBuilder withLoginAttempt(AysUser.LoginAttempt loginAttempt) {
        data.setLoginAttempt(loginAttempt);
        return this;
    }

    public AysUserBuilder withValidLoginAttempt() {
        data.setLoginAttempt(new LoginAttemptBuilder().withValidValues().build());
        return this;
    }

    public AysUserBuilder withRoles(Set<AysRole> roles) {
        data.setRoles(roles);
        return this;
    }

    public AysUserBuilder withInstitution(Institution institution) {
        data.setInstitution(institution);
        return this;
    }


    public static class PasswordBuilder extends TestDataBuilder<AysUser.Password> {

        public PasswordBuilder() {
            super(AysUser.Password.class);
        }

        public PasswordBuilder withValidValues() {
            return this
                    .withValue(AysValidTestData.PASSWORD_ENCRYPTED);
        }

        public PasswordBuilder withValue(String password) {
            data.setValue(password);
            return this;
        }

    }


    public static class LoginAttemptBuilder extends TestDataBuilder<AysUser.LoginAttempt> {

        public LoginAttemptBuilder() {
            super(AysUser.LoginAttempt.class);
        }

        public LoginAttemptBuilder withValidValues() {
            return this
                    .withLastLoginAt(LocalDateTime.now());
        }

        public LoginAttemptBuilder withLastLoginAt(LocalDateTime lastLoginAt) {
            data.setLastLoginAt(lastLoginAt);
            return this;
        }

    }

}
