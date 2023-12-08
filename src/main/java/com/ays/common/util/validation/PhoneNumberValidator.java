package com.ays.common.util.validation;

import com.ays.common.model.AysPhoneNumberAccessor;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * A custom validator implementation for the {@link PhoneNumber} annotation.
 * Validates whether the provided {@link AysPhoneNumberAccessor} object is a valid phone number or not based on E.164 international standard.
 */
class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, AysPhoneNumberAccessor> {

    /**
     * Validates an AysPhoneNumberAccessor object based on E.164 international standard.
     *
     * @param phoneNumber The AysPhoneNumberAccessor object to be validated.
     * @param context     The context for validation.
     * @return True if the number is valid, false otherwise.
     */
    @Override
    public boolean isValid(AysPhoneNumberAccessor phoneNumber, ConstraintValidatorContext context) {
        final String countryCode = phoneNumber.getCountryCode();
        final String lineNumber = phoneNumber.getLineNumber();
        final String fullNumber = "+" + countryCode + lineNumber;

        if (countryCode == null || lineNumber == null) {
            return true;
        }

        final boolean countryCodeIsNumeric = countryCode.chars().allMatch(Character::isDigit);
        final boolean lineNumberIsNumeric = lineNumber.chars().allMatch(Character::isDigit);

        if (!countryCodeIsNumeric || !lineNumberIsNumeric) {
            return false;
        }

        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

        try {
            final String regionCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt(countryCode));
            Phonenumber.PhoneNumber number = phoneNumberUtil.parse(fullNumber, null);
            return phoneNumberUtil.isValidNumberForRegion(number, regionCode);
        } catch (Exception e) {
            return false;
        }
    }

}
