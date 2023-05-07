package com.ays.common.model;

import com.ays.common.util.AysRandomUtil;

public class AysPhoneNumberBuilder extends TestDataBuilder<AysPhoneNumber> {

    public AysPhoneNumberBuilder() {
        super(AysPhoneNumber.class);
    }

    public AysPhoneNumberBuilder withValidFields() {
        return new AysPhoneNumberBuilder()
                .withCountryCode(90L)
                .withLineNumber(AysRandomUtil.generateNumber(10));
    }

    public AysPhoneNumberBuilder withCountryCode(Long countryCode) {
        data.setCountryCode(countryCode);
        return this;
    }

    public AysPhoneNumberBuilder withLineNumber(Long lineNumber) {
        data.setLineNumber(lineNumber);
        return this;
    }

}
