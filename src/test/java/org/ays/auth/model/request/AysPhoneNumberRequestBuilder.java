package org.ays.auth.model.request;

import org.ays.common.model.TestDataBuilder;
import org.ays.common.model.request.AysPhoneNumberRequest;

public class AysPhoneNumberRequestBuilder extends TestDataBuilder<AysPhoneNumberRequest> {

    public AysPhoneNumberRequestBuilder() {
        super(AysPhoneNumberRequest.class);
    }

    public AysPhoneNumberRequestBuilder withValidValues() {
        return this
                .withCountryCode("90")
                .withLineNumber("5054567811");
    }

    public AysPhoneNumberRequestBuilder withCountryCode(String countryCode) {
        data.setCountryCode(countryCode);
        return this;
    }

    public AysPhoneNumberRequestBuilder withLineNumber(String lineNumber) {
        data.setLineNumber(lineNumber);
        return this;
    }

}
