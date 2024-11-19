package org.ays.common.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

/**
 * A custom validator implementation for the {@link NoSpecialCharacters} annotation.
 * Validates whether the provided reason invalid domain
 * specified regular expression.
 */
class NoSpecialCharacterValidator implements ConstraintValidator<NoSpecialCharacters, String> {

    /**
     * Regular expression pattern to match valid text with special characters, including Turkish characters.
     */
    private static final String VALID_CHARACTERS_REGEX = "^[a-zA-Z0-9çğıöşüÇĞİÖŞÜ.,';:?()\\-\\s!/]+$";


    /**
     * Checks if the value contains special characters and meets length requirements.
     *
     * @param value                      the value to validate
     * @param constraintValidatorContext context in which the constraint is evaluated
     * @return true if the value is valid, false otherwise
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

        if (!StringUtils.hasText(value)) {
            return true;
        }

        boolean startsOrEndsWithSpace = value.matches("^\\s.*|.*\\s$");

        if (startsOrEndsWithSpace) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("cannot starts or ends with space")
                    .addConstraintViolation();
            return false;
        }

        boolean containsOnlyDigits = value.matches("^\\d+$");

        if (containsOnlyDigits) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("cannot contain only digits")
                    .addConstraintViolation();
            return false;
        }

        boolean containsOnlyPunctuation = value.matches("^\\p{Punct}+$");

        if (containsOnlyPunctuation) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("cannot contain only punctuation marks")
                    .addConstraintViolation();
            return false;
        }

        boolean containsOnlySpecialCharacters = value.matches("^[\\p{Punct}£#$½]+$");

        if (containsOnlySpecialCharacters) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("cannot contain only special characters")
                    .addConstraintViolation();
            return false;
        }

        if (!value.matches(VALID_CHARACTERS_REGEX)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("contains invalid characters")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
