package org.ays.auth.model.dto.response;

import org.ays.auth.model.response.UserSavedResponse;
import org.ays.common.model.TestDataBuilder;

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
