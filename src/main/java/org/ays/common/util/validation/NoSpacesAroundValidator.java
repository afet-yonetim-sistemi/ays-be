package org.ays.common.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

/**
 * A custom validator implementation for the {@link NoSpacesAround} annotation.
 * Validates whether the provided text does not have
 * trailing or leading spaces.
 */
class NoSpacesAroundValidator implements ConstraintValidator<NoSpacesAround, String> {

    @Override
    public boolean isValid(String text, ConstraintValidatorContext constraintValidatorContext) {

        if (!StringUtils.hasText(text)) {
            return true;
        }

        return !(text.startsWith(" ") || text.endsWith(" "));
    }

}
