package com.ays.common.util.validation;

import com.ays.common.model.AysPhoneNumber;
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

        if (countryCode == null || lineNumber == null) {
            return true;
        }

        final int countryCodeLength = countryCode.length();
        final int lineNumberLength = lineNumber.length();
        final boolean isCountryCodeLengthValid = countryCodeLength >= 1 && countryCodeLength <= 7;
        final boolean isLineNumberLengthValid = lineNumberLength >= 1 && lineNumberLength <= 13;

        final boolean countryCodeIsNumeric = countryCode.chars().allMatch(Character::isDigit);
        final boolean lineNumberIsNumeric = lineNumber.chars().allMatch(Character::isDigit);

        final boolean isPhoneNumberLengthValid = isCountryCodeLengthValid && isLineNumberLengthValid;
        final boolean isPhoneNumberNumeric = countryCodeIsNumeric && lineNumberIsNumeric;
        return isPhoneNumberLengthValid && isPhoneNumberNumeric;
    }

}
