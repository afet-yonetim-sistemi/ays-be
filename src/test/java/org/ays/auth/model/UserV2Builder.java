package org.ays.auth.model;

import org.ays.auth.model.enums.UserStatus;
import org.ays.common.model.AysPhoneNumber;
import org.ays.common.model.AysPhoneNumberBuilder;
import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.Institution;
import org.ays.institution.model.InstitutionBuilder;
import org.ays.util.AysValidTestData;

import java.time.LocalDateTime;
import java.util.Set;

public class UserV2Builder extends TestDataBuilder<AysUser> {

    public UserV2Builder() {
        super(AysUser.class);
    }

    public UserV2Builder withValidValues() {
        final Institution institution = new InstitutionBuilder()
                .withValidValues()
                .build();

        return this
                .withId(AysRandomUtil.generateUUID())
                .withInstitution(institution)
                .withEmailAddress("@afetyonetimsistemi.org")
                .withPhoneNumber(new AysPhoneNumberBuilder().withValidValues().build())
                .withStatus(UserStatus.ACTIVE)
                .withPassword(null)
                .withLoginAttempt(null);
    }

    public UserV2Builder withId(String id) {
        data.setId(id);
        return this;
    }

    public UserV2Builder withoutId() {
        data.setId(null);
        return this;
    }

    public UserV2Builder withEmailAddress(String emailAddress) {
        data.setEmailAddress(data.getEmailAddress() + emailAddress);
        return this;
    }

    public UserV2Builder withPhoneNumber(AysPhoneNumber phoneNumber) {
        data.setPhoneNumber(phoneNumber);
        return this;
    }

    public UserV2Builder withStatus(UserStatus status) {
        data.setStatus(status);
        return this;
    }

    public UserV2Builder withPassword(AysUser.Password password) {
        data.setPassword(password);
        return this;
    }

    public UserV2Builder withValidPassword() {
        data.setPassword(new PasswordBuilder().withValidValues().build());
        return this;
    }

    public UserV2Builder withLoginAttempt(AysUser.LoginAttempt loginAttempt) {
        data.setLoginAttempt(loginAttempt);
        return this;
    }

    public UserV2Builder withValidLoginAttempt() {
        data.setLoginAttempt(new LoginAttemptBuilder().withValidValues().build());
        return this;
    }

    public UserV2Builder withRoles(Set<AysRole> roles) {
        data.setRoles(roles);
        return this;
    }

    public UserV2Builder withInstitution(Institution institution) {
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
