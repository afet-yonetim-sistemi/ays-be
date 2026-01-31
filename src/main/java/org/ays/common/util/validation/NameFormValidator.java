package org.ays.common.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

/**
 * A custom validator implementation for the {@link NameForm} annotation.
 * Validates whether the provided first/last name is a valid name based on the
 * specified regular expression.
 */
class NameFormValidator implements ConstraintValidator<NameForm, String> {
    /**
     * Regular expression defining the valid structure of a name.
     * <p>
     * This regex enforces:
     * <ul>
     *   <li>Minimum length of 2 and maximum length of 100 characters</li>
     *   <li>Starting and ending with a letter</li>
     *   <li>Only allowed punctuation characters (.,'-) and spaces</li>
     *   <li>No consecutive punctuation or whitespace characters</li>
     *   <li>Optional whitespace before or after punctuation</li>
     * </ul>
     * </p>
     */
    private static final String NAME_REGEX = "^(?=.{2,100}$)(?iu)[a-zçğışöü](?:[ a-zçğışöü]|(?<![.,'\\-])[.,'\\-](?![.,'\\-]))*[a-zçğışöü]$";

    /**
     * Validates whether the given value is a valid name.
     *
     * <p><b>Examples of valid names:</b></p>
     * <ul>
     *   <li>Su</li>
     *   <li>Çağla</li>
     *   <li>Robert William Floyd</li>
     *   <li>Dr. Ahmet</li>
     *   <li>Ahmet - Mehmet</li>
     *   <li>Ahmet-Mehmet</li>
     *   <li>O'Connor</li>
     * </ul>
     *
     * <p><b>Examples of invalid names:</b></p>
     * <ul>
     *   <li>"" (empty)</li>
     *   <li>" "</li>
     *   <li>".Mehmet"</li>
     *   <li>"Mehmet-"</li>
     *   <li>"123456"</li>
     *   <li>"sanshi345"</li>
     *   <li>"#$½%"</li>
     *   <li>"Ali$$Veli"</li>
     *   <li>"Ahmet!"</li>
     *   <li>"Ahmet--Mehmet"</li>
     *   <li>"Ali     Can"</li>
     *   <li>"A"</li>
     * </ul>
     *
     * @param value   the name value to validate
     * @param context context in which the constraint is evaluated
     * @return {@code true} if the value is valid; {@code false} otherwise
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (!StringUtils.hasText(value)) {
            return false;
        }

        boolean hasLeadingOrTrailingWhitespace = !value.equals(value.trim());
        if (hasLeadingOrTrailingWhitespace) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("name must not start or end with whitespace")
                    .addConstraintViolation();
            return false;
        }

        boolean containsOnlyDigits = value.chars()
                .allMatch(Character::isDigit);
        if (containsOnlyDigits) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("name cannot consist only of digits")
                    .addConstraintViolation();
            return false;
        }

        String allowedSpecials = ".,'- ";
        boolean containsOnlySpecialCharacters =
                value.chars()
                        .allMatch(ch -> allowedSpecials.indexOf(ch) >= 0);

        if (containsOnlySpecialCharacters) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("name cannot consist only of special characters")
                    .addConstraintViolation();
            return false;
        }

        String consecutiveWhitespace = "  ";

        if (value.contains(consecutiveWhitespace)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("name must not contain consecutive whitespaces")
                    .addConstraintViolation();
            return false;
        }

        boolean matchesFormat = value.matches(NAME_REGEX);
        if (!matchesFormat) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("name format is invalid")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
