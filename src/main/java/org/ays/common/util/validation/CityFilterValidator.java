package org.ays.common.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * A custom validator implementation for the {@link CityFilter} annotation.
 *
 * <p>
 * Validates whether the provided city or district filter value is valid
 * according to the filtering rules defined for city-based search fields.
 * </p>
 *
 * <p>
 * Since filtering supports partial and character-based searching,
 * this validator focuses on preventing meaningless or invalid inputs
 * rather than enforcing strict city name formatting.
 * </p>
 */
class CityFilterValidator implements ConstraintValidator<CityFilter, String> {

    /**
     * Regular expression defining allowed characters for city filtering.
     *
     * <p>
     * The regular expression allows filter values containing:
     * </p>
     * <ul>
     *   <li>Uppercase and lowercase letters (a-z, A-Z)</li>
     *   <li>Turkish characters: ÇçĞğİıÖöŞşÜü</li>
     *   <li>Whitespace characters</li>
     *   <li>Accepted punctuation characters: period(.), comma(,), hyphen(-), apostrophe(')</li>
     * </ul>
     *
     * <p>
     * Digits and any other special characters are not allowed.
     * </p>
     */
    private static final String ALLOWED_CHARS_REGEX = "^[a-zA-ZçÇğĞıİöÖşŞüÜ.,'\\- ]*$";

    /**
     * Pattern used to detect consecutive punctuation characters.
     *
     * <p>
     * Any occurrence of two or more punctuation characters
     * (.,'-) appearing consecutively is considered invalid.
     * </p>
     */
    private static final Pattern CONSECUTIVE_PUNCTUATION_PATTERN = Pattern.compile("[.,'\\-]{2,}");

    /**
     * Checks whether the given value is a valid city or district filter.
     *
     * <p><b>Some valid filter values are:</b></p>
     * <ul>
     *   <li>İstanbul</li>
     *   <li>Kadıköy</li>
     *   <li>İst</li>
     *   <li>köy</li>
     *   <li>St. Luis</li>
     *   <li>Las Vegas</li>
     *   <li>St.</li>
     *   <li>.S</li>
     * </ul>
     *
     * <p><b>Some invalid filter values are:</b></p>
     * <ul>
     *   <li>" S" (Starts with whitespace)</li>
     *   <li>"S " (Ends with whitespace)</li>
     *   <li>"Ada  na" (Consecutive whitespace)</li>
     *   <li>"Ada.-na" (Consecutive punctuation)</li>
     *   <li>"Adana123" (Contains digits)</li>
     *   <li>"#$½%" (Only special characters)</li>
     * </ul>
     *
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     * @return {@code true} if the filter value is valid, {@code false} otherwise
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (!StringUtils.hasText(value)) {
            return true;
        }

        boolean hasLeadingOrTrailingWhitespace = !value.equals(value.trim());
        if (hasLeadingOrTrailingWhitespace) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("city filter must not start or end with whitespace")
                    .addConstraintViolation();
            return false;
        }

        String allowedSpecials = ".,'- ";
        boolean containsOnlySpecialCharacters = value.chars()
                .allMatch(ch -> allowedSpecials.indexOf(ch) >= 0);
        if (containsOnlySpecialCharacters) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("city filter cannot consist only of special characters")
                    .addConstraintViolation();
            return false;
        }

        boolean matchesAllowedCharacters = value.matches(ALLOWED_CHARS_REGEX);
        if (!matchesAllowedCharacters) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("city filter contains invalid characters")
                    .addConstraintViolation();
            return false;
        }

        boolean hasConsecutiveSpaces = value.contains("  ");
        if (hasConsecutiveSpaces) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("city filter cannot contain consecutive spaces")
                    .addConstraintViolation();
            return false;
        }

        boolean hasConsecutivePunctuation = CONSECUTIVE_PUNCTUATION_PATTERN.matcher(value)
                .find();
        if (hasConsecutivePunctuation) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("city filter cannot contain consecutive punctuation characters")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
