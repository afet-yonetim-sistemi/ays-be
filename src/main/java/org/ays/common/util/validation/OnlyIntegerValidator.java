package org.ays.common.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * A custom validator implementation for the {@link OnlyInteger} annotation.
 * <p>
 * Validates whether a given {@link String} value represents a valid integer
 * and conforms to the specified sign constraint defined by {@link OnlyInteger.Sign}.
 * </p>
 *
 * <p><strong>Validation behavior:</strong></p>
 * <ul>
 *   <li>If the input is {@code null}, empty, or exceeds {@link Long#MAX_VALUE} in length, it is considered valid (no validation error).</li>
 *   <li>If the input contains non-numeric characters or a decimal point, it is considered invalid.</li>
 *   <li>If a specific sign constraint is defined (e.g., {@code POSITIVE}, {@code NEGATIVE}), the input must match that sign.</li>
 *   <li>Custom constraint violation messages such as {@code "must be positive integer"} or {@code "must be negative integer"} are added on sign mismatch.</li>
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
     * <p>
     * This method performs the following validation logic:
     * <ul>
     *     <li>If the value is {@code null}, empty, or exceeds {@link Long#MAX_VALUE} in length, it is considered valid.</li>
     *     <li>If the value is not a valid integer (e.g., contains non-digit characters or decimal point), it is invalid.</li>
     *     <li>If the value passes basic checks, it proceeds to sign validation (e.g., positive or negative).</li>
     * </ul>
     *
     * @param number  the value to validate
     * @param context the constraint validation context used to build violation messages
     * @return {@code true} if the value is valid or blank or too long; {@code false} if invalid
     */
    @Override
    public boolean isValid(String number, ConstraintValidatorContext context) {

        if (StringUtils.isEmpty(number) || number.length() > Long.toString(Long.MAX_VALUE).length()) {
            return true;
        }

        if (this.isNotValidInteger(number)) {
            return false;
        }

        return this.isPositiveOrNegativeInteger(number, context);
    }

    /**
     * Checks whether the given string fails to qualify as a valid integer.
     * <p>
     * A string is considered invalid if:
     * <ul>
     *     <li>It cannot be parsed into a number (non-numeric characters).</li>
     *     <li>It contains a decimal point, which disqualifies it as an integer.</li>
     * </ul>
     *
     * @param number the input string to evaluate
     * @return {@code true} if the input is not a valid integer; {@code false} otherwise
     */
    private boolean isNotValidInteger(String number) {

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

        return Long.parseLong(number) > 0;
    }

    /**
     * Checks whether the given string represents a negative integer.
     *
     * @param number the input string
     * @return {@code true} if the number is lower than zero
     */
    private boolean isNegative(String number) {
        return Long.parseLong(number) < 0;
    }

}
