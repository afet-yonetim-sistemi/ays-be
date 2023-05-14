package com.ays.user.model.dto.request;


import com.ays.auth.model.dto.request.AysLoginRequest;
import com.ays.common.model.TestDataBuilder;

public class AysUserLoginRequestBuilder extends TestDataBuilder<AysLoginRequest> {

    public AysUserLoginRequestBuilder() {
        super(AysLoginRequest.class);
    }

}
