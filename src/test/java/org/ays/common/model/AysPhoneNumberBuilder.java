package org.ays.common.model;

import org.ays.common.util.AysRandomUtil;

public class AysPhoneNumberBuilder extends TestDataBuilder<AysPhoneNumber> {

    public AysPhoneNumberBuilder() {
        super(AysPhoneNumber.class);
    }

    public AysPhoneNumberBuilder withValidFields() {
        return new AysPhoneNumberBuilder()
                .withCountryCode("90")
                .withLineNumber("535" + AysRandomUtil.generateNumber(7));
    }

    public AysPhoneNumberBuilder withCountryCode(String countryCode) {
        data.setCountryCode(countryCode);
        return this;
    }

    public AysPhoneNumberBuilder withLineNumber(String lineNumber) {
        data.setLineNumber(lineNumber);
        return this;
    }

}
