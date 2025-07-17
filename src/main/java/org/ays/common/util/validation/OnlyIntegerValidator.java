package org.ays.common.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * A custom validator implementation for the {@link OnlyInteger} annotation.
 * <p>
 * Validates whether a given {@link String} value conforms to the expected sign constraint
 * ({@code POSITIVE}, {@code NEGATIVE}, or {@code ANY}) and represents a valid integer value.
 * </p>
 *
 * <p><strong>Validation behavior:</strong></p>
 * <ul>
 *   <li>If the input is {@code null} or blank, it is considered valid.</li>
 *   <li>The input must be an integer value (no decimal points or non-numeric characters).</li>
 *   <li>The sign of the number must match the configured {@link OnlyInteger.Sign} rule.</li>
 *   <li>Custom messages such as {@code "must be positive integer"} or {@code "must be negative integer"} are added on failure.</li>
 * </ul>
 */
class OnlyIntegerValidator implements ConstraintValidator<OnlyInteger, String> {

    private OnlyInteger.Sign sign;

    /**
     * Initializes the validator with the configured sign constraint from the {@link OnlyInteger} annotation.
     *
     * @param constraintAnnotation the annotation instance containing the configuration
     */
    @Override
    public void initialize(OnlyInteger constraintAnnotation) {
        this.sign = constraintAnnotation.sign();
    }

    /**
     * Validates whether the given string is a valid integer and conforms to the specified sign constraint.
     *
     * @param number  the value to validate
     * @param context the constraint validation context
     * @return {@code true} if the number is valid or blank; {@code false} otherwise
     */
    @Override
    public boolean isValid(String number, ConstraintValidatorContext context) {

        if (StringUtils.isEmpty(number)) {
            return true;
        }

        if (this.isValidInteger(number)) {
            return false;
        }

        return this.isPositiveOrNegativeInteger(number, context);
    }

    /**
     * Checks whether the given string can be parsed as an integer.
     *
     * @param number the input string
     * @return {@code true} if not an integer (i.e., invalid); {@code false} if it is parsable
     */
    private boolean isValidInteger(String number) {

        if (!NumberUtils.isParsable(number)) {
            return true;
        }

        return number.contains(".");
    }

    /**
     * Validates the integer's sign according to the specified {@link org.ays.common.util.validation.OnlyInteger.Sign}.
     *
     * @param number  the string representing an integer
     * @param context the validation context
     * @return {@code true} if the sign is valid; {@code false} otherwise
     */
    private boolean isPositiveOrNegativeInteger(String number, ConstraintValidatorContext context) {

        if (sign.isPositive()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("must be positive integer")
                    .addConstraintViolation();
            return this.isPositive(number);
        }

        if (sign.isNegative()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("must be negative integer")
                    .addConstraintViolation();
            return this.isNegative(number);
        }

        return true;
    }

    /**
     * Checks whether the given string represents a positive integer.
     *
     * @param number the input string
     * @return {@code true} if the number is greater than zero
     */
    private boolean isPositive(String number) {

        if (!NumberUtils.isDigits(number)) {
            return false;
        }

        if (number.length() > Long.toString(Long.MAX_VALUE).length()) {
            return true;
        }

        return Long.parseLong(number) > 0;
    }

    /**
     * Checks whether the given string represents a negative integer.
     *
     * @param number the input string
     * @return {@code true} if the number is lower than zero
     */
    private boolean isNegative(String number) {
        return !number.startsWith("-") && Long.parseLong(number) < 0;
    }

}
