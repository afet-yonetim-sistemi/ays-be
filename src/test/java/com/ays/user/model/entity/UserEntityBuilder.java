package com.ays.user.model.entity;

import com.ays.common.model.TestDataBuilder;
import com.ays.user.model.enums.UserStatus;

public class UserEntityBuilder extends TestDataBuilder<UserEntity> {

    public UserEntityBuilder() {
        super(UserEntity.class);
    }

    public UserEntityBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public UserEntityBuilder withStatus(UserStatus status) {
        data.setStatus(status);
        return this;
    }

}
