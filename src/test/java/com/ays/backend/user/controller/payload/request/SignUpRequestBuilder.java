package com.ays.backend.user.controller.payload.request;

import com.ays.backend.base.TestDataBuilder;

/**
 * This is a class that can be used to generate test data for the SignUpRequest class without the need for manual input.
 */
public class SignUpRequestBuilder extends TestDataBuilder<SignUpRequest> {

    public SignUpRequestBuilder() {
        super(SignUpRequest.class);
    }

    /**
     * To change the statusId field inside the SignUpRequest object according to our preference, this method can be used.
     */
    public SignUpRequestBuilder withStatusId(int statusId) {
        data.setStatusId(statusId);
        return this;
    }
}
