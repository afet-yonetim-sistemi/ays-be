package org.ays.auth.model.request;

import org.ays.auth.model.enums.AysSourcePage;
import org.ays.common.model.TestDataBuilder;

public class AysForgotPasswordRequestBuilder extends TestDataBuilder<AysPasswordForgotRequest> {

    public AysForgotPasswordRequestBuilder() {
        super(AysPasswordForgotRequest.class);
    }

    public AysForgotPasswordRequestBuilder withValidValues() {
        return new AysForgotPasswordRequestBuilder()
                .withEmailAddress("test@afetyonetimsistemi.org");
    }

    public AysForgotPasswordRequestBuilder withEmailAddress(String emailAddress) {
        data.setEmailAddress(emailAddress);
        return this;
    }

    public AysForgotPasswordRequestBuilder withSourcePage(final AysSourcePage sourcePage) {
        data.setSourcePage(sourcePage);
        return this;
    }

}
