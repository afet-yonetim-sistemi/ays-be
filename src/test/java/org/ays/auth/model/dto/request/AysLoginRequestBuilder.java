package org.ays.auth.model.dto.request;

import org.ays.common.model.TestDataBuilder;

@Deprecated(since = "AysLoginRequestBuilder V2 Production'a alınınca burası silinecektir.", forRemoval = true)
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
