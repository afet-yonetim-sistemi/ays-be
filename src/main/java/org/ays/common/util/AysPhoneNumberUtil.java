package org.ays.common.util;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AysPhoneNumberUtil {

    private static final PhoneNumberUtil PHONE_NUMBER_UTIL = PhoneNumberUtil.getInstance();

    public static boolean isValid(final String countryCode, final String lineNumber) {

        final String fullNumber = "+" + countryCode + lineNumber;

        try {
            final Phonenumber.PhoneNumber number = PHONE_NUMBER_UTIL
                    .parse(fullNumber, null);
            return PHONE_NUMBER_UTIL.isValidNumber(number);
        } catch (NumberParseException exception) {
            return false;
        }
    }

}
