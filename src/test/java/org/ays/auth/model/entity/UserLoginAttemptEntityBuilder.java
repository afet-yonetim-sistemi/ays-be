package org.ays.auth.model.entity;

import org.ays.common.model.TestDataBuilder;
import org.ays.common.util.AysRandomUtil;

import java.time.LocalDateTime;

public class UserLoginAttemptEntityBuilder extends TestDataBuilder<UserLoginAttemptEntity> {

    public UserLoginAttemptEntityBuilder() {
        super(UserLoginAttemptEntity.class);
    }

    public UserLoginAttemptEntityBuilder withValidFields() {
        return new UserLoginAttemptEntityBuilder()
                .withId(AysRandomUtil.generateUUID())
                .withUserId(AysRandomUtil.generateUUID())
                .withLastLoginAt(LocalDateTime.now());
    }

    public UserLoginAttemptEntityBuilder withId(String id) {
        this.data.setId(id);
        return this;
    }

    public UserLoginAttemptEntityBuilder withUserId(String userId) {
        this.data.setUserId(userId);
        return this;
    }

    public UserLoginAttemptEntityBuilder withLastLoginAt(LocalDateTime lastLoginAt) {
        this.data.setLastLoginAt(lastLoginAt);
        return this;
    }

}
