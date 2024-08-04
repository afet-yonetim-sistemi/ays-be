package org.ays.auth.model.request;

import org.ays.common.model.TestDataBuilder;

public class AysPasswordCreateRequestBuilder extends TestDataBuilder<AysPasswordCreateRequest> {

    public AysPasswordCreateRequestBuilder() {
        super(AysPasswordCreateRequest.class);
    }

    public AysPasswordCreateRequestBuilder withValidValues() {
        return this
                .withPassword("testpass")
                .withPasswordRepeat("testpass");
    }

    public AysPasswordCreateRequestBuilder withPassword(String password) {
        data.setPassword(password);
        return this;
    }

    public AysPasswordCreateRequestBuilder withPasswordRepeat(String passwordRepeat) {
        data.setPasswordRepeat(passwordRepeat);
        return this;
    }

}
