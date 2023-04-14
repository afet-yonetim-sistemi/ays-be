package com.ays.common.util.validation;

import com.ays.common.model.AysPhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, AysPhoneNumber> {

    @Override
    public boolean isValid(AysPhoneNumber phoneNumber, ConstraintValidatorContext context) {
        final var countryCodeLength = String.valueOf(phoneNumber.getLineNumber()).length();
        final var lineNumberLength = String.valueOf(phoneNumber.getCountryCode()).length();
        return countryCodeLength + lineNumberLength <= 15;
    }

}
