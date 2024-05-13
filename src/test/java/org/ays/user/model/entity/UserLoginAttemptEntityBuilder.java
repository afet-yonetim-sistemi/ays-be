package org.ays.user.model.entity;

import org.ays.common.model.TestDataBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserLoginAttemptEntityBuilder extends TestDataBuilder<UserLoginAttemptEntity> {

    public UserLoginAttemptEntityBuilder() {
        super(UserLoginAttemptEntity.class);
    }

    public UserLoginAttemptEntityBuilder withValidFields() {
        return new UserLoginAttemptEntityBuilder()
                .withId(UUID.randomUUID().toString())
                .withUserId(UUID.randomUUID().toString())
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
