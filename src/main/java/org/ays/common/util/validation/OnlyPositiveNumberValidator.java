package org.ays.common.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * A custom validator implementation for the {@link OnlyPositiveNumber} annotation.
 * <p>
 * Validates whether the given {@link String} represents a positive numeric value.
 * The input is considered valid if it is non-blank, contains only digits, and represents a number greater than zero.
 * </p>
 */
class OnlyPositiveNumberValidator implements ConstraintValidator<OnlyPositiveNumber, String> {

    /**
     * Validates whether the provided {@code String} value is a positive number.
     *
     * @param number  the string value to validate
     * @param context the context in which the constraint is evaluated
     * @return {@code true} if the value is blank or a positive number; {@code false} otherwise
     */
    @Override
    public boolean isValid(String number, ConstraintValidatorContext context) {

        if (StringUtils.isEmpty(number)) {
            return true;
        }

        if (!NumberUtils.isDigits(number)) {
            return false;
        }

        if (number.length() <= 19) {
            return Long.parseLong(number) > 0;
        }

        return !number.startsWith("-");
    }

}
