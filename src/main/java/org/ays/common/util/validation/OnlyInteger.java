package org.ays.common.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to validate that a numeric field is an integer and matches a specific sign constraint
 * (e.g., positive, negative).
 * <p>
 * This constraint is validated using the {@link OnlyIntegerValidator} class.
 * It can be applied to fields of numeric-compatible types such as {@link String}, {@link Integer}, or {@link Long}.
 * The value must be a valid integer string and conform to the defined {@link Sign} rule.
 * </p>
 *
 * <p><strong>Example usage:</strong></p>
 * <pre>
 * {@code
 *   @OnlyInteger(sign = OnlyInteger.Sign.POSITIVE)
 *   private String age;
 * }
 * </pre>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OnlyIntegerValidator.class)
public @interface OnlyInteger {

    /**
     * Defines the custom error message to be returned when validation fails.
     *
     * @return the validation error message
     */
    String message() default "must be integer";

    /**
     * Specifies the expected sign of the validated number.
     * <ul>
     *   <li>{@link Sign#POSITIVE} - value must be greater than 0</li>
     *   <li>{@link Sign#NEGATIVE} - value must be less than 0</li>
     *   <li>{@link Sign#ANY} - value can be positive, negative, or zero (default)</li>
     * </ul>
     *
     * @return the expected sign constraint
     */
    Sign sign() default Sign.ANY;

    /**
     * Enumeration representing supported sign constraints for integer validation.
     */
    enum Sign {

        /**
         * Indicates that the number must be a positive integer (> 0).
         */
        POSITIVE,

        /**
         * Indicates that the number must be a negative integer (< 0).
         */
        NEGATIVE,

        /**
         * Indicates that the number can be of any sign (positive, negative, or zero).
         */
        ANY;

        /**
         * Checks if this sign type requires the value to be positive.
         *
         * @return true if the sign is {@code POSITIVE}, false otherwise
         */
        public boolean isPositive() {
            return this == POSITIVE;
        }

        /**
         * Checks if this sign type requires the value to be negative.
         *
         * @return true if the sign is {@code NEGATIVE}, false otherwise
         */
        public boolean isNegative() {
            return this == NEGATIVE;
        }
    }

    /**
     * Defines the validation groups that this constraint belongs to.
     *
     * @return the validation groups
     */
    Class<?>[] groups() default {};

    /**
     * Allows clients of the Bean Validation API to assign custom payload objects to this constraint.
     *
     * @return the custom payload objects
     */
    Class<? extends Payload>[] payload() default {};

}
