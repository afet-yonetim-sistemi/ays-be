package org.ays.common.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to validate a text using {@link NoSpacesAroundValidator}.
 */
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NoSpacesAroundValidator.class)
public @interface NoSpacesAround {

    /**
     * Returns the error message when the text is not valid.
     *
     * @return the error message
     */
    String message() default "cannot start or end with space";

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
