package org.ays.common.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to validate reason using {@link SpecialCharacterValidator}.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SpecialCharacterValidator.class)
public @interface NoSpecialCharacters {

    /**
     * Returns the error message when reason is not valid.
     *
     * @return the error message
     */
    String message() default "cannot contain special characters";

    /**
     * Returns the validation groups to which this constraint belongs.
     *
     * @return the validation groups
     */
    Class<?>[] groups() default {};

    /**
     * Returns the payload associated to this constraint.
     *
     * @return the payload
     */
    Class<? extends Payload>[] payload() default {};
}
