package com.ays.common.util.validation;

import com.ays.common.model.AysPhoneNumber;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * A custom validator implementation for the {@link PhoneNumber} annotation.
 * Validates whether the provided {@link AysPhoneNumber} object is a valid phone number or not based on the
 * country code and line number length.
 */
class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, AysPhoneNumber> {

    /**
     * Validates whether the provided {@link AysPhoneNumber} object is a valid phone number or not based on the
     * country code and line number length.
     *
     * @param phoneNumber the {@link AysPhoneNumber} object to be validated
     * @param context     the context in which the constraint is evaluated
     * @return true if the phone number is valid, false otherwise
     */
    @Override
    public boolean isValid(AysPhoneNumber phoneNumber, ConstraintValidatorContext context) {
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
