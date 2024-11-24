package org.ays.common.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * A custom validator implementation for the {@link NoTrailingOrLeadingSpaces} annotation.
 * Validates whether the provided text does not have
 * trailing or leading spaces.
 */
class NoTrailingOrLeadingSpacesValidator implements ConstraintValidator<NoTrailingOrLeadingSpaces, String> {

    @Override
    public boolean isValid(String text, ConstraintValidatorContext constraintValidatorContext) {

        if (text.startsWith(" ") || text.endsWith(" ")) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("cannot start or end with space")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

}
