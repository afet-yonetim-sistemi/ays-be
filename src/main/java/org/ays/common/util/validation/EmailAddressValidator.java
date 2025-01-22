package org.ays.common.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * A custom validator implementation for the {@link EmailAddress} annotation.
 * Validates whether the provided email matches a specified set of rules
 * to ensure it's in a valid format.
 */
class EmailAddressValidator implements ConstraintValidator<EmailAddress, String> {

    @SuppressWarnings("java:S5998")
    private static final Pattern EMAIL_REGEX = Pattern.compile(
            "^(?!.*\\.{2})" +
                    "[\\p{Alnum}][\\p{Alnum}._%+\\-]*" +
                    "@" +
                    "(?!-)(?:[\\p{Alnum}]+(?<!-)\\.)+" +
                    "[\\p{Alpha}]{2,}$",
            Pattern.UNICODE_CHARACTER_CLASS
    );

    /**
     * Checks whether the given value is a valid emailAddress or not.
     *
     * <p>Some valid emails are:</p>
     * <ul>
     * <li>abcdef@mail.com</li>
     * <li>abc+def@archive.com</li>
     * <li>john.doe123@example.co.uk</li>
     * <li>admin_123@example.org</li>
     * <li>admin-test@ays.com</li>
     * <li>johndoe@gmail.com</li>
     * <li>janedoe123@yahoo.com</li>
     * <li>michael.jordan@nba.com</li>
     * <li>alice.smith@company.co.uk</li>
     * <li>info@mywebsite.org</li>
     * <li>support@helpdesk.net</li>
     * </ul>
     *
     * <p>Some invalid emails are:</p>
     * <ul>
     * <li>plainaddress</li>
     * <li>@missingusername.com</li>
     * <li>username@.com</li>
     * <li>username@gmail</li>
     * <li>username@gmail..com</li>
     * <li>username@gmail.c</li>
     * <li>username@-gmail.com</li>
     * <li>username@gmail-.com</li>
     * <li>username@gmail.com.</li>
     * <li>username@.gmail.com</li>
     * <li>username@gmail@gmail.com</li>
     * <li>username(john.doe)@gmail.com</li>
     * <li>user@domain(comment).com</li>
     * <li>usernamegmail.com</li>
     * <li>username@gmail,com</li>
     * <li>username@gmail space.co</li>
     * <li>username@gmail..co.uk</li>
     * <li>user#gmail.com</li>
     * </ul>
     *
     * @param emailAddress object to validate
     * @return true if the value is valid, false otherwise
     */
    @Override
    public boolean isValid(String emailAddress, ConstraintValidatorContext constraintValidatorContext) {

        if (!StringUtils.hasText(emailAddress)) {
            return true;
        }

        return EMAIL_REGEX.matcher(emailAddress).matches();
    }

}