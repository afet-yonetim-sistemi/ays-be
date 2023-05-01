package com.ays.util;

import com.ays.common.model.AysPhoneNumber;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AysTestData {

    public static final String VALID_EMAIL = "test@ays.com";

    public static final AysPhoneNumber VALID_PHONE_NUMBER = AysPhoneNumber.builder()
            .countryCode(90)
            .lineNumber(1234567890).build();

}
