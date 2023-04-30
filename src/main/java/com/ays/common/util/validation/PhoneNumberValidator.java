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
        final var countryCodeLength = String.valueOf(phoneNumber.getLineNumber()).length();
        final var lineNumberLength = String.valueOf(phoneNumber.getCountryCode()).length();
        return countryCodeLength + lineNumberLength <= 15;
    }

}
