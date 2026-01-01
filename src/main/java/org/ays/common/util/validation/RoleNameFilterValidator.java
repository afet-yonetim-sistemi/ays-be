package org.ays.common.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

/**
 * Validator implementation for the {@link RoleNameFilter} annotation.
 */
class RoleNameFilterValidator implements ConstraintValidator<RoleNameFilter, String> {

    /**
     * A custom validator implementation for the {@link RoleNameFilter} annotation.
     * <p>
     * This validator checks whether the provided search text satisfies the filtering rules.
     * Since filtering allows partial matches (e.g., "Admin-"), the rules focus on preventing
     * nonsensical queries rather than strict formatting.
     * </p>
     *
     * <p><b>Valid filter examples:</b></p>
     * <ul>
     * <li>Admin</li>
     * <li>adm (Partial match)</li>
     * <li>21 (Digits allowed)</li>
     * <li>- 1 (Can start with special char)</li>
     * <li>&a (Can start with special char)</li>
     * <li>AYS | Admin (Spaces and specials allowed)</li>
     * <li>A  (Space matches allowed)</li>
     * </ul>
     *
     * <p><b>Invalid filter examples:</b></p>
     * <ul>
     * <li>"#$½%" (Consists only of special characters)</li>
     * <li>"A--b" (Consecutive punctuation)</li>
     * <li>"A**b" (Consecutive special characters)</li>
     * <li>"   " (Consists only of whitespace)</li>
     * </ul>
     *
     * <p>
     * If validation fails, the validator provides a descriptive
     * constraint violation message indicating the specific rule that was violated.
     * </p>
     *
     * @see RoleNameFilter
     * @see ConstraintValidator
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (!StringUtils.hasText(value)) {
            return true;
        }

        boolean containsOnlyWhitespace = value.trim().isEmpty();
        if (containsOnlyWhitespace) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("role filter cannot be only whitespace")
                    .addConstraintViolation();
            return false;
        }

        boolean containsOnlySpecialCharacters = value.chars()
                .noneMatch(ch -> Character.isLetterOrDigit(ch) || Character.isWhitespace(ch));

        if (containsOnlySpecialCharacters) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("role filter cannot consist only of special characters")
                    .addConstraintViolation();
            return false;
        }

        if (hasConsecutiveSpecialCharacters(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("role filter cannot contain consecutive special characters")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

    /**
     * Checks whether the given text contains any consecutive special characters
     * (non-alphanumeric and non-whitespace characters).
     *
     * @param text the input string to check for consecutive special characters
     * @return true if the text contains consecutive special characters, false otherwise
     */
    private boolean hasConsecutiveSpecialCharacters(String text) {
        return text.chars()
                .anyMatch(character -> !Character.isLetterOrDigit(character) && !Character.isWhitespace(character));
    }

}
