package org.ays.common.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

/**
 * Validator for password fields. This validator ensures that a password meets the minimum length requirement.
 */
class PasswordValidator implements ConstraintValidator<Password, String> {

    /**
     * Validates the provided password based on predefined rules.
     *
     * @param password                   the password to validate.
     * @param constraintValidatorContext context in which the constraint is evaluated.
     * @return {@code true} if the password is valid, {@code false} otherwise.
     */
    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {

        if (!StringUtils.hasText(password)) {
            return true;
        }

        if (password.length() < 6) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("size must be at least 6 characters")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

}
