package org.ays.common.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Enum representing categories of sensitive data and their respective masking strategies.
 * <p>
 * Each category is associated with a list of field names and a specific masking logic
 * for obscuring sensitive information. The purpose of this enum is to ensure data privacy
 * and security by masking fields such as tokens, passwords, email addresses, phone numbers,
 * addresses, and names.
 * </p>
 *
 * <p><strong>Example Usage:</strong></p>
 * <pre>
 * String maskedEmail = AysSensitiveMaskingCategory.EMAIL_ADDRESS.mask("test@example.com");
 * System.out.println(maskedEmail); // Output: tes******com
 * </pre>
 */
@Getter
@RequiredArgsConstructor
enum AysSensitiveMaskingCategory {

    /**
     * Category for masking token fields such as "authorization", "accessToken", or "refreshToken".
     * <p>
     * Reveals the first 20 characters of the token and replaces the remaining part with a fixed placeholder.
     * </p>
     */
    AUTH(List.of("authorization", "accessToken", "refreshToken")) {
        @Override
        public String mask(String value) {

            if (value.length() <= 20) {
                return value;
            }

            return value.substring(0, 20) + MASK;
        }
    },


    /**
     * Category for masking password fields by replacing their values with a fixed placeholder.
     * <p>
     * Ensures the entire password is obscured for security purposes.
     * </p>
     */
    PASSWORD(List.of("password")) {
        @Override
        public String mask(String value) {
            return MASK;
        }
    },


    /**
     * Category for masking email addresses by obscuring the middle characters with asterisks,
     * while keeping the first three and last three characters visible.
     * <p>
     * If the email address is shorter than or equal to three characters, it is returned as is.
     * </p>
     */
    EMAIL_ADDRESS(List.of("emailAddress")) {
        @Override
        public String mask(String value) {

            if (value.length() <= 3) {
                return value;
            }

            int length = value.length();
            String firstThree = value.substring(0, 3);
            String lastThree = value.substring(length - 3);
            return firstThree + MASK + lastThree;
        }
    },


    /**
     * Category for masking phone numbers by hiding all but the last four digits with asterisks.
     * <p>
     * If the phone number is shorter than or equal to four characters, it is returned as is.
     * </p>
     */
    PHONE_NUMBER(List.of("lineNumber", "phoneNumber")) {
        @Override
        public String mask(String value) {

            if (value.length() <= 4) {
                return value;
            }

            return MASK + value.substring(value.length() - 4);
        }
    },


    /**
     * Category for masking address fields by obscuring the middle characters with asterisks,
     * while keeping the first five and last five characters visible.
     * <p>
     * If the address is shorter than or equal to 20 characters, only the first character
     * is revealed, followed by asterisks.
     * </p>
     */
    ADDRESS(List.of("address")) {
        @Override
        public String mask(String value) {

            if (value.length() <= 20) {
                return value.charAt(0) + MASK;
            }

            return value.substring(0, 5) + MASK + value.substring(value.length() - 5);
        }
    },


    /**
     * Category for masking name fields such as "firstName" or "lastName" by revealing the first character
     * and replacing the remaining part with a fixed placeholder.
     * <p>
     * Ensures sensitive information in name fields is obscured while maintaining partial identification.
     * </p>
     */
    NAME(List.of("firstName", "lastName")) {
        @Override
        public String mask(String value) {

            if (value.length() <= 1) {
                return value;
            }

            return value.charAt(0) + MASK;
        }
    };


    /**
     * List of field names associated with the sensitive category.
     */
    private final List<String> fields;


    /**
     * Abstract method to be implemented by each category for masking sensitive data.
     *
     * @param value the value to mask
     * @return the masked value
     */
    public abstract String mask(String value);


    private static final String MASK = "******";

}
