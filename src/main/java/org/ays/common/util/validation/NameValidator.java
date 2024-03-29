package org.ays.common.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

/**
 * A custom validator implementation for the {@link Name} annotation.
 * Validates whether the provided first/last name is a valid name based on the
 * specified regular expression.
 */
class NameValidator implements ConstraintValidator<Name, String> {

    /**
     * Regular expression for validating names.
     * <p>
     * The regular expression matches names containing:
     * <ul>
     *   <li>Uppercase and lowercase letters (a-z, A-Z)</li>
     *   <li>Turkish special characters: ÇçĞğİıÖöŞşÜü</li>
     *   <li>Common punctuation: space( ), comma(,), period(.), apostrophe('), hyphen(-)</li>
     * </ul>
     *  It also avoids strings that start with special characters
     * </p>
     * <p>
     */
    private static final String NAME_REGEX = "^(?![^a-zA-ZÇçĞğİıÖöŞşÜü]).*[a-zA-ZÇçĞğİıÖöŞşÜü ,.'-]+$";
    private static final Integer NAME_MIN_LENGTH = 2;
    private static final Integer NAME_MAX_LENGTH = 255;

    /**
     * Checks whether the given value is a valid name or not.
     * <p>Some valid names are:</p>
     * <ul>
     *     <li>Şule Çeliköz</li>
     *     <li>Mathias d'Arras</li>
     *     <li>Martin Luther King, Jr.</li>
     *     <li>Hector Sausage-Hausen</li>
     * </ul>
     *
     * <p>Some invalid names are:</p>
     * <ul>
     *     <li>?Menekşe</li>
     *     <li>John Doe 123</li>
     *     <li>Ahmet?*</li>
     *     <li>Mehmet!</li>
     *     <li>A</li>
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

        if (value.length() < NAME_MIN_LENGTH || value.length() > NAME_MAX_LENGTH) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            String.format("NAME MUST BE BETWEEN %d AND %d CHARACTERS LONG", NAME_MIN_LENGTH, NAME_MAX_LENGTH)
                    )
                    .addConstraintViolation();
            return false;
        }

        if (value.startsWith(" ") || value.endsWith(" ")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("NAME MUST NOT START OR END WITH WHITESPACE")
                    .addConstraintViolation();
            return false;
        }

        return value.matches(NAME_REGEX);
    }
}
