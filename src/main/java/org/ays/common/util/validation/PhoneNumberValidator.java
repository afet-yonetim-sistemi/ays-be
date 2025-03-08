package org.ays.common.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.ays.common.model.request.AysPhoneNumberRequest;
import org.ays.common.util.AysPhoneNumberUtil;
import org.springframework.util.StringUtils;

/**
 * A custom validator implementation for the {@link PhoneNumber} annotation.
 * Validates whether the provided {@link AysPhoneNumberRequest} object is a valid phone number or not based on E.164 international standard.
 */
class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, AysPhoneNumberRequest> {

    /**
     * Validates an AysPhoneNumberRequest object based on E.164 international standard.
     *
     * @param phoneNumber The AysPhoneNumberAccessor object to be validated.
     * @param context     The context for validation.
     * @return True if the number is valid, false otherwise.
     */
    @Override
    public boolean isValid(AysPhoneNumberRequest phoneNumber, ConstraintValidatorContext context) {

        final String countryCode = phoneNumber.getCountryCode();
        final String lineNumber = phoneNumber.getLineNumber();

        if (!StringUtils.hasText(countryCode) || !StringUtils.hasText(lineNumber)) {
            return true;
        }

        if (!"90".equals(countryCode)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("country code must be 90")
                    .addConstraintViolation();
            return false;
        }

        if (lineNumber.length() != 10) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("line number length must be 10")
                    .addConstraintViolation();
            return false;
        }

        final boolean countryCodeIsNumeric = countryCode.chars().allMatch(Character::isDigit);
        final boolean lineNumberIsNumeric = lineNumber.chars().allMatch(Character::isDigit);

        if (!countryCodeIsNumeric || !lineNumberIsNumeric) {
            return false;
        }

        return AysPhoneNumberUtil.isValid(countryCode, lineNumber);
    }

}
