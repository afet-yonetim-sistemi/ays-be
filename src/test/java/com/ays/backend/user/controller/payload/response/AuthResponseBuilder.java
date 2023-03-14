package com.ays.backend.user.controller.payload.response;

import com.ays.backend.base.TestDataBuilder;

/**
 * This is a class that can be used to generate test data for the AuthResponse class without the need for manual input.
 */
public class AuthResponseBuilder extends TestDataBuilder<AuthResponse> {

    public AuthResponseBuilder() {
        super(AuthResponse.class);
    }

    public AuthResponseBuilder withMessage(String message) {
        data.setMessage(message);
        return this;
    }

    public AuthResponseBuilder withAccessToken(String accessToken) {
        data.setAccessToken(accessToken);
        return this;
    }
}
