package org.ays.auth.model.request;

import org.ays.auth.model.enums.AysSourcePage;
import org.ays.common.model.TestDataBuilder;
import org.ays.util.AysValidTestData;

public class AysLoginRequestBuilder extends TestDataBuilder<AysLoginRequest> {

    public AysLoginRequestBuilder() {
        super(AysLoginRequest.class);
    }

    public AysLoginRequestBuilder withValidValues() {
        return new AysLoginRequestBuilder()
                .withEmailAddress(AysValidTestData.EMAIL_ADDRESS)
                .withPassword(AysValidTestData.PASSWORD)
                .withSourcePage(AysSourcePage.INSTITUTION);
    }

    public AysLoginRequestBuilder withEmailAddress(final String emailAddress) {
        data.setEmailAddress(emailAddress);
        return this;
    }

    public AysLoginRequestBuilder withPassword(final String password) {
        data.setPassword(password);
        return this;
    }

    public AysLoginRequestBuilder withSourcePage(final AysSourcePage sourcePage) {
        data.setSourcePage(sourcePage);
        return this;
    }

}
