package org.ays.auth.model.request;

import org.ays.auth.model.enums.AysSourcePage;
import org.ays.common.model.TestDataBuilder;
import org.ays.util.AysValidTestData;

public class AysLoginRequestV2Builder extends TestDataBuilder<AysLoginRequest> {

    public AysLoginRequestV2Builder() {
        super(AysLoginRequest.class);
    }

    public AysLoginRequestV2Builder withValidValues() {
        return new AysLoginRequestV2Builder()
                .withEmailAddress(AysValidTestData.EMAIL)
                .withPassword(AysValidTestData.PASSWORD)
                .withSourcePage(AysSourcePage.INSTITUTION);
    }

    public AysLoginRequestV2Builder withEmailAddress(final String emailAddress) {
        data.setEmailAddress(emailAddress);
        return this;
    }

    public AysLoginRequestV2Builder withPassword(final String password) {
        data.setPassword(password);
        return this;
    }

    public AysLoginRequestV2Builder withSourcePage(final AysSourcePage sourcePage) {
        data.setSourcePage(sourcePage);
        return this;
    }

}
