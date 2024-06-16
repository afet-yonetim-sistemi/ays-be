package org.ays.user.model.dto.response;

import org.ays.common.model.TestDataBuilder;
import org.ays.user.model.response.UserSavedResponse;

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
