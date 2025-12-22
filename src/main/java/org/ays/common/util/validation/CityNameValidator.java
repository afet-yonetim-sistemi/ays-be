package org.ays.common.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

/**
 * A custom validator implementation for the {@link CityName} annotation.
 * Validates whether the provided city name is a valid city name based on the
 * specified regular expression.
 */
class CityNameValidator implements ConstraintValidator<CityName, String> {

    /**
     * Regular expression defining the valid structure of a city name.
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
    private static final String CITY_REGEX = "^(?=.{2,100}$)(?iu)[a-zçğışöü](?:[ a-zçğışöü]|(?<![.,'\\-])[.,'\\-](?![.,'\\-]))*[a-zçğışöü]$";

    /**
     * Validates whether the given value is a valid city name.
     *
     * <p><b>Examples of valid city names:</b></p>
     * <ul>
     *   <li>İstanbul</li>
     *   <li>New York</li>
     *   <li>St. Luis</li>
     *   <li>Rio de Janeiro</li>
     *   <li>Saint-Pierre-du-Mont</li>
     * </ul>
     *
     * <p><b>Examples of invalid city names:</b></p>
     * <ul>
     *   <li>12345</li>
     *   <li>New*CityName</li>
     *   <li>A</li>
     *   <li>Saint--Louis</li>
     *   <li>Los  Angele</li>
     *   <li>New York 123</li>
     * </ul>
     *
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     * @return true if the value is valid, false otherwise
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (!StringUtils.hasText(value)) {
            return true;
        }

        boolean hasLeadingOrTrailingWhitespace = !value.equals(value.trim());
        if (hasLeadingOrTrailingWhitespace) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "city must not start or end with whitespace")
                    .addConstraintViolation();
            return false;
        }

        boolean containsOnlyWhitespace = value.trim()
                .isEmpty();
        if (containsOnlyWhitespace) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "city cannot be only whitespace")
                    .addConstraintViolation();
            return false;
        }

        boolean containsOnlyDigits = value.chars()
                .allMatch(Character::isDigit);
        if (containsOnlyDigits) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "city cannot consist only of digits")
                    .addConstraintViolation();
            return false;
        }

        String allowedSpecials = ".,'- ";
        boolean containsOnlySpecialCharacters =
                value.chars()
                        .allMatch(ch -> allowedSpecials.indexOf(ch) >= 0);

        if (containsOnlySpecialCharacters) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "city cannot consist only of special characters")
                    .addConstraintViolation();
            return false;
        }

        boolean matchesFormat = value.matches(CITY_REGEX);
        if (!matchesFormat) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "city format is invalid")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
