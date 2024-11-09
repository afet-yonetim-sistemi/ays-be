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
     */
    private static final String NAME_REGEX = "^(?!.*[ ,.'-]{2})[a-zA-ZÇçĞğİıÖöŞşÜü]+(?:[ ,.'-](?![ ,.'-])[a-zA-ZÇçĞğİıÖöŞşÜü]+)*$";

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
     *     <li>123</li>
     *     <li>Mary*land</li>
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

        if (value.startsWith(" ") || value.endsWith(" ")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("name must not start or end with whitespace")
                    .addConstraintViolation();
            return false;
        }

        return value.matches(NAME_REGEX);
    }
}
