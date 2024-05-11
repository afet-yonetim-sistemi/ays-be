package org.ays.auth.model.dto.request;

import org.ays.common.model.TestDataBuilder;
import org.ays.user.model.enums.SourcePage;
import org.ays.util.AysValidTestData;

public class AysLoginRequestV2Builder extends TestDataBuilder<AysLoginRequestV2> {

    public AysLoginRequestV2Builder() {
        super(AysLoginRequestV2.class);
    }

    public AysLoginRequestV2Builder withValidFields() {
        return new AysLoginRequestV2Builder()
                .withEmailAddress(AysValidTestData.EMAIL)
                .withPassword(AysValidTestData.PASSWORD)
                .withSourcePage(SourcePage.INSTITUTION);
    }

    public AysLoginRequestV2Builder withEmailAddress(final String emailAddress) {
        data.setEmailAddress(emailAddress);
        return this;
    }

    public AysLoginRequestV2Builder withPassword(final String password) {
        data.setPassword(password);
        return this;
    }

    public AysLoginRequestV2Builder withSourcePage(final SourcePage sourcePage) {
        data.setSourcePage(sourcePage);
        return this;
    }

}
