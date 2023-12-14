package com.ays.common.model.dto.request;

import com.ays.common.model.TestDataBuilder;
import com.ays.common.util.AysRandomUtil;

public class AysPhoneNumberRequestBuilder extends TestDataBuilder<AysPhoneNumberRequest> {

    public AysPhoneNumberRequestBuilder() {
        super(AysPhoneNumberRequest.class);
    }

    public AysPhoneNumberRequestBuilder withValidFields() {
        return new AysPhoneNumberRequestBuilder()
                .withCountryCode("90")
                .withLineNumber("535" + AysRandomUtil.generateNumber(7));
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
