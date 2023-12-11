package com.ays.common.model.dto.request;

import com.ays.common.model.TestDataBuilder;
import com.ays.common.util.AysRandomUtil;

public class AysPhoneNumberFilterRequestBuilder extends TestDataBuilder<AysPhoneNumberFilterRequest> {

    public AysPhoneNumberFilterRequestBuilder() {
        super(AysPhoneNumberFilterRequest.class);
    }

    public AysPhoneNumberFilterRequestBuilder withValidValues() {
        return new AysPhoneNumberFilterRequestBuilder()
                .withCountryCode("90")
                .withLineNumber("535" + AysRandomUtil.generateNumber(7));
    }

    public AysPhoneNumberFilterRequestBuilder withCountryCode(String countryCode) {
        data.setCountryCode(countryCode);
        return this;
    }

    public AysPhoneNumberFilterRequestBuilder withLineNumber(String lineNumber) {
        data.setLineNumber(lineNumber);
        return this;
    }
}
