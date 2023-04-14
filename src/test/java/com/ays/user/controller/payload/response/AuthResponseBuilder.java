package com.ays.user.controller.payload.response;

import com.ays.common.model.TestDataBuilder;

/**
 * This is a class that can be used to generate test data for the AuthTokenResponse class without the need for manual input.
 */
public class AuthTokenResponseBuilder extends TestDataBuilder<AuthTokenResponse> {

    public AuthTokenResponseBuilder() {
        super(AuthTokenResponse.class);
    }


    public AuthTokenResponseBuilder withAccessToken(String accessToken) {
        data.setAccessToken(accessToken);
        return this;
    }
}
