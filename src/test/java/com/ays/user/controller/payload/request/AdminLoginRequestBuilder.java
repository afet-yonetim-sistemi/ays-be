package com.ays.user.controller.payload.request;

import com.ays.auth.controller.dto.request.AysLoginRequest;
import com.ays.common.model.TestDataBuilder;

public class AdminLoginRequestBuilder extends TestDataBuilder<AysLoginRequest> {

    public AdminLoginRequestBuilder() {
        super(AysLoginRequest.class);
    }
}