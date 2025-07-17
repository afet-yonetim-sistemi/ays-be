package org.ays.common.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to validate that a numeric field contains only positive values.
 * <p>
 * This constraint is validated using the {@link OnlyPositiveNumberValidator} class.
 * It can be applied to fields of numeric types (e.g., {@link Integer}, {@link Long}) to enforce positivity.
 * </p>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OnlyPositiveNumberValidator.class)
public @interface OnlyPositiveNumber {


    /**
     * Returns the error message when the number is not positive.
     *
     * @return the validation error message
     */
    String message() default "must be positive number";

    /**
     * Returns the validation groups this constraint belongs to.
     *
     * @return the validation groups
     */
    Class<?>[] groups() default {};

    /**
     * Returns the custom payload objects associated with this constraint.
     *
     * @return the payload
     */
    Class<? extends Payload>[] payload() default {};

}
