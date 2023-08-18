package com.ays.user.model;

import com.ays.common.model.TestDataBuilder;
import com.ays.user.model.enums.UserStatus;

public class UserBuilder extends TestDataBuilder<User> {

    public UserBuilder() {
        super(User.class);
    }

    public UserBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public UserBuilder withUsername(String username) {
        data.setUsername(username);
        return this;
    }

    public UserBuilder withPassword(String password) {
        data.setPassword(password);
        return this;
    }

    public UserBuilder withStatus(UserStatus status) {
        data.setStatus(status);
        return this;
    }

}
