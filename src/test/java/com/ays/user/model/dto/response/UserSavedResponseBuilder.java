package com.ays.user.model.dto.response;

import com.ays.common.model.TestDataBuilder;

public class UserSavedResponseBuilder extends TestDataBuilder<UserSavedResponse> {

    public UserSavedResponseBuilder() {
        super(UserSavedResponse.class);
    }

    public UserSavedResponseBuilder withUsername(String username) {
        data.setUsername(username);
        return this;
    }

    public UserSavedResponseBuilder withPassword(String password) {
        data.setPassword(password);
        return this;
    }

}
