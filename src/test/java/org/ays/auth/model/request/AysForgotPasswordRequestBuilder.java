package org.ays.auth.model.request;

import org.ays.common.model.TestDataBuilder;

public class AysForgotPasswordRequestBuilder extends TestDataBuilder<AysForgotPasswordRequest> {

    public AysForgotPasswordRequestBuilder() {
        super(AysForgotPasswordRequest.class);
    }

    public AysForgotPasswordRequestBuilder withValidValues() {
        return new AysForgotPasswordRequestBuilder()
                .withEmailAddress("test@afetyonetimsistemi.org");
    }

    public AysForgotPasswordRequestBuilder withEmailAddress(String emailAddress) {
        data.setEmailAddress(emailAddress);
        return this;
    }

}
