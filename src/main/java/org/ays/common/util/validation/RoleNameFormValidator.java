package org.ays.common.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

/**
 * Validator implementation for the {@link RoleNameForm} annotation.
 * <p>
 * This validator ensures that role names adhere to a strict formatting standard.
 * The validation includes checks for:
 * </p>
 */
class RoleNameFormValidator implements ConstraintValidator<RoleNameForm, String> {

    /**
     * Regular expression defining the valid structure of a role name.
     * It enforces length limits, ensures the name starts with a letter,
     * prevents consecutive special characters, and controls the allowed character set.
     */
    private static final String ROLE_REGEX =
            "^(?=.{2,255}$)(?iu)[a-zçğıöşü](?:[ a-zçğıöşü0-9]|[.,'/\\-&|_#](?![.,'/\\-&|_#]))*[a-zçğıöşü0-9]$";

    /**
     * Set of allowed special characters used for validation checks.
     */
    private static final String SPECIALS = ".,'/-&|_#";


    /**
     * A custom validator implementation for the {@link RoleNameForm} annotation.
     * <p>
     * This validator checks whether the provided role name satisfies a set of
     * structural and formatting rules defined by a regular expression and several
     * pre-validation checks.
     * </p>
     *
     *
     * <p><b>Valid role names:</b></p>
     * <ul>
     *     <li>Admin</li>
     *     <li>System Supervisor</li>
     *     <li>yetkili_v2</li>
     *     <li>Gönüllü# Rolu 3 -Ankara</li>
     *     <li>İçerik-Yönetici</li>
     * </ul>
     *
     * <p><b>Invalid role names:</b></p>
     * <ul>
     *     <li>" Admin" (leading whitespace)</li>
     *     <li>"User " (trailing whitespace)</li>
     *     <li>"12345" (digits only)</li>
     *     <li>"--#½" (special characters only)</li>
     *     <li>"a" (too short)</li>
     *     <li>"Admin##Team" (consecutive special characters)</li>
     *     <li>"!Role" (starts with invalid character)</li>
     * </ul>
     *
     * <p>
     * If validation fails, the validator provides a descriptive
     * constraint violation message indicating the specific rule that was violated.
     * </p>
     *
     * @see RoleNameForm
     * @see ConstraintValidator
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (!StringUtils.hasText(value)) {
            return true;
        }

        boolean hasLeadingOrTrailingWhitespace = !value.equals(value.trim());
        if (hasLeadingOrTrailingWhitespace) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("role name must not start or end with whitespace")
                    .addConstraintViolation();
            return false;
        }

        boolean containsOnlyWhitespace = value.trim().isEmpty();
        if (containsOnlyWhitespace) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("role name cannot be only whitespace")
                    .addConstraintViolation();
            return false;
        }

        boolean containsOnlyDigits = value.chars().allMatch(Character::isDigit);
        if (containsOnlyDigits) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("role name cannot consist only of digits")
                    .addConstraintViolation();
            return false;
        }

        boolean containsOnlySpecialCharacters = value.chars().allMatch(ch -> SPECIALS.indexOf(ch) >= 0);
        if (containsOnlySpecialCharacters) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("role name cannot consist only of special characters")
                    .addConstraintViolation();
            return false;
        }

        boolean matchesFormat = value.matches(ROLE_REGEX);
        if (!matchesFormat) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("role name format is invalid")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

}
