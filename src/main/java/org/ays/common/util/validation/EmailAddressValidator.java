package org.ays.common.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

/**
 * A custom validator implementation for the {@link EmailAddress} annotation.
 * Validates whether the provided email matches a specified set of rules
 * to ensure it's in a valid format.
 */
class EmailAddressValidator implements ConstraintValidator<EmailAddress, String> {

    private static final String EMAIL_REGEX =
            "^(?!.*\\.\\.|.*--|.*-@|.*@\\.|.*\\.-|.*-\\.).+[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    /**
     * Checks whether the given value is a valid email or not.
     * <p>Some valid emails are:</p>
     * <ul>
     *     <li>user@example.com</li>
     *     <li>john.doe123@example.co.uk</li>
     *     <li>admin_123@example.org</li>
     * </ul>
     *
     * <p>Some invalid emails are:</p>
     * <ul>
     *     <li>user@invalid</li>
     *     <li>user@invalid!.com</li>
     *     <li>u@ser@.com</li>
     *     <li>user@..com</li>
     *     <li>user</li>
     *     <li>a@b.c</li>
     * </ul>
     *
     * @param email object to validate
     * @return true if the value is valid, false otherwise
     */
    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {

        if (!StringUtils.hasText(email)) {
            return true;
        }

        if (email.length() <= 6 || email.length() >= 254) {
            return this.buildViolation(constraintValidatorContext, "length must be between 6 and 254 characters");
        }

        if (email.startsWith(" ") || email.endsWith(" ")) {
            return this.buildViolation(constraintValidatorContext, "email must not start or end with whitespace");
        }

        if (email.chars().filter(ch -> ch == '@').count() != 1) {
            return this.buildViolation(constraintValidatorContext, "email must contain exactly one '@' character");
        }

        if (email.matches(".*[()#\\[\\]\";,\\s].*")) {
            return this.buildViolation(constraintValidatorContext, "email contains invalid special characters");
        }

        String[] parts = email.split("@", 2);
        if (parts[0].isEmpty() || !Character.isLetterOrDigit(parts[0].charAt(0))) {
            return this.buildViolation(constraintValidatorContext, "email local part must start with a letter or number");
        }

        String domainPart = parts[1];
        if (domainPart.startsWith("-") || domainPart.endsWith("-")) {
            return this.buildViolation(constraintValidatorContext, "domain must not start or end with a hyphen");
        }

        if (!email.matches(EMAIL_REGEX)) {
            return this.buildViolation(constraintValidatorContext, "email is not in a valid format");
        }

        return true;
    }

    private boolean buildViolation(ConstraintValidatorContext constraintValidatorContext, String message) {
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        return false;
    }
}
