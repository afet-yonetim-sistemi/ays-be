package org.ays.common.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

/**
 * A custom validator implementation for the {@link NoSpecialCharacters} annotation.
 * Validates whether the provided reason invalid domain
 * specified regular expression.
 */
class SpecialCharacterValidator implements ConstraintValidator<NoSpecialCharacters, String> {

    /**
     * Regular expression pattern to match valid text with special characters, including Turkish characters.
     */
    private static final String SPECIAL_CHARACTERS_REGEX = "^[a-zA-Z0-9çğıöşüÇĞİÖŞÜ.,'\\-\\s_!]+$";

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

        value = value.trim();

        boolean containsOnlyDigits = value.matches("^\\d+$");

        if (containsOnlyDigits) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("THE TEXT CANNOT CONTAIN ONLY DIGITS")
                    .addConstraintViolation();
            return false;
        }

        boolean containsOnlyPunctuation = value.matches("^\\p{Punct}+$");

        if (containsOnlyPunctuation) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("THE TEXT CANNOT CONTAIN ONLY PUNCTUATION MARKS")
                    .addConstraintViolation();
            return false;
        }

        boolean containsOnlySpecialCharacters = value.matches("^[\\p{Punct}£#$½]+$");

        if (containsOnlySpecialCharacters) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("THE TEXT CANNOT CONTAIN ONLY SPECIAL CHARACTERS")
                    .addConstraintViolation();
            return false;
        }

        if (!value.matches(SPECIAL_CHARACTERS_REGEX)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("THE TEXT CONTAINS INVALID CHARACTERS")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
