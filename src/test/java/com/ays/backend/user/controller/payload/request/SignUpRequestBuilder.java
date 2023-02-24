package com.ays.backend.user.controller.payload.request;

import com.ays.backend.base.TestDataBuilder;

public class SignUpRequestBuilder extends TestDataBuilder<SignUpRequest> {

    public SignUpRequestBuilder() {
        super(SignUpRequest.class);
    }

    public SignUpRequestBuilder withStatusId(int statusId) {
        data.setStatusId(statusId);
        return this;
    }
}
