package com.ays.auth.model.dto.request;

import com.ays.common.model.TestDataBuilder;

public class AysLoginRequestBuilder extends TestDataBuilder<AysLoginRequest> {

    public AysLoginRequestBuilder() {
        super(AysLoginRequest.class);
    }

    public AysLoginRequestBuilder withUsername(final String username) {
        data.setUsername(username);
        return this;
    }

    public AysLoginRequestBuilder withPassword(final String password) {
        data.setPassword(password);
        return this;
    }

}
