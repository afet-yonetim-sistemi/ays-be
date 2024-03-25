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

    private static final String SPECIAL_CHARACTERS_REGEX = "^[a-zA-Z0-9çğıöşü.,\\-\\s_!]+$";

    /**
     * Checks if the value contains special characters.
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

        int realCharacterCount = value.replaceAll("\\s", "").length();

        if (value.matches("^\\d+$")) {
            return false;
        }

        return realCharacterCount >= 40 && value.matches(SPECIAL_CHARACTERS_REGEX);
    }
}
