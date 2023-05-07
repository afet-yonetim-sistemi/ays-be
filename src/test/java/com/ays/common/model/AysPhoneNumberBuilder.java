package com.ays.common.model;

import com.ays.common.util.AysRandomUtil;
import org.junit.jupiter.api.Assertions;

public class AysPhoneNumberBuilder extends TestDataBuilder<AysPhoneNumber> {

    public AysPhoneNumberBuilder() {
        super(AysPhoneNumber.class);
    }

    public static void assertEquals(AysPhoneNumber mockPhoneNumber, AysPhoneNumber phoneNumber) {
        Assertions.assertEquals(mockPhoneNumber.getCountryCode(), phoneNumber.getCountryCode());
        Assertions.assertEquals(mockPhoneNumber.getLineNumber(), phoneNumber.getLineNumber());
    }

    public AysPhoneNumberBuilder withValidFields() {
        return new AysPhoneNumberBuilder()
                .withCountryCode(90)
                .withLineNumber(AysRandomUtil.generateNumber(10).intValue());
    }

    public AysPhoneNumberBuilder withCountryCode(Integer countryCode) {
        data.setCountryCode(countryCode);
        return this;
    }

    public AysPhoneNumberBuilder withLineNumber(Integer lineNumber) {
        data.setLineNumber(lineNumber);
        return this;
    }

}
