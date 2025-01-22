package org.ays.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;

/**
 * Utility class for masking sensitive information in JSON data.
 * <p>
 * This class provides methods for masking fields within a {@link JsonNode} or individual string values.
 * It is designed to handle common sensitive fields such as tokens, passwords, email addresses, and more.
 * </p>
 *
 * <p><strong>Example Usage:</strong></p>
 * <p><strong>Unmasked JSON:</strong></p>
 * <pre>
 * {
 *   "emailAddress": "test@example.com",
 *   "password": "123456789",
 *   "lineNumber": "1234567890",
 *   "address": "123 Main Street, Springfield",
 *   "firstName": "John",
 *   "lastName": "Doe"
 * }
 * </pre>
 *
 * <p><strong>Masked JSON:</strong></p>
 * <pre>
 * {
 *   "emailAddress": "tes******com",
 *   "password": "******",
 *   "lineNumber": "******7890",
 *   "address": "123 M******field",
 *   "firstName": "J******",
 *   "lastName": "D******"
 * }
 * </pre>
 */
@UtilityClass
public class AysMaskUtil {

    private static final String MASKED_VALUE = "******";

    /**
     * Masks sensitive fields in the given {@link JsonNode}.
     * <p>
     * This method recursively processes the fields in the JSON object or array,
     * applying masking to sensitive fields based on their names. The masking logic
     * is determined by the field name and uses predefined rules for known sensitive fields.
     * </p>
     *
     * @param jsonNode the JSON node to process for masking
     */
    public static void mask(final JsonNode jsonNode) {

        if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            for (JsonNode arrayElement : arrayNode) {
                mask(arrayElement);
            }
        }

        if (!jsonNode.isObject()) {
            return;
        }

        ObjectNode objectNode = (ObjectNode) jsonNode;
        Iterator<String> fieldNames = objectNode.fieldNames();

        while (fieldNames.hasNext()) {

            String fieldName = fieldNames.next();
            JsonNode fieldValue = objectNode.get(fieldName);

            if (fieldValue.isValueNode()) {
                String maskedValue = mask(fieldName, fieldValue.asText());
                objectNode.put(fieldName, maskedValue);
                continue;
            }

            mask(fieldValue);
        }

    }

    /**
     * Masks the value of a specific field based on its name.
     * <p>
     * Sensitive fields such as "Authorization", "accessToken", and "password" are masked using specific
     * rules. Fields not recognized as sensitive remain unchanged.
     * </p>
     *
     * @param field the name of the field
     * @param value the value to mask
     * @return the masked value
     */
    public static String mask(final String field, final String value) {

        if ("null".equalsIgnoreCase(value) || StringUtils.isBlank(value)) {
            return value;
        }

        return switch (field) {
            case "Authorization", "authorization", "accessToken", "refreshToken" -> maskToken(value);
            case "password" -> maskPassword();
            case "emailAddress" -> maskEmailAddress(value);
            case "lineNumber" -> maskLineNumber(value);
            case "address" -> maskAddress(value);
            case "firstName", "lastName", "applicantFirstName", "applicantLastName" -> maskName(value);
            default -> value;
        };
    }

    /**
     * Masks token fields such as "Authorization", "authorization", "accessToken", or "refreshToken".
     * <p>
     * This method reveals the first 20 characters of the token and replaces the remaining part
     * with a fixed placeholder to obscure sensitive information.
     * </p>
     *
     * @param value the token value to mask
     * @return the masked token, preserving the first 20 characters and appending "******"
     */
    private static String maskToken(String value) {

        if (value.length() <= 20) {
            return value;
        }

        return value.substring(0, 20) + MASKED_VALUE;
    }

    /**
     * Masks password fields by replacing their values with a fixed placeholder.
     * <p>
     * This method is used to obscure passwords completely, ensuring no characters from
     * the original value are visible.
     * </p>
     *
     * @return the masked password placeholder (e.g., "******")
     */
    private static String maskPassword() {
        return MASKED_VALUE;
    }

    /**
     * Masks email addresses by obscuring the middle characters with asterisks,
     * while keeping the first three and last three characters visible.
     * <p>
     * If the email address is shorter than or equal to three characters, the value is returned as-is.
     * </p>
     *
     * @param value the email address to mask
     * @return the masked email address with the first three and last three characters visible, or unaltered if too short
     */
    private static String maskEmailAddress(String value) {

        if (value.length() <= 3) {
            return value;
        }

        int length = value.length();
        String firstThree = value.substring(0, 3);
        String lastThree = value.substring(length - 3);
        return firstThree + MASKED_VALUE + lastThree;
    }

    /**
     * Masks address fields by hiding the middle characters with asterisks,
     * while keeping the first five and last five characters visible.
     * <p>
     * If the address is shorter than or equal to 20 characters, only the first character
     * is revealed, followed by asterisks.
     * </p>
     *
     * @param value the address to mask
     * @return the masked address with the first five and last five characters visible, or partially masked if shorter
     */
    private static String maskAddress(String value) {

        if (value.length() <= 20) {
            return value.charAt(0) + MASKED_VALUE;
        }

        return value.substring(0, 5) + MASKED_VALUE + value.substring(value.length() - 5);
    }

    /**
     * Masks a line number by hiding all but the last four digits with asterisks.
     * <p>
     * If the line number is shorter than or equal to four characters, it is returned as is.
     * </p>
     *
     * @param value the line number to mask
     * @return the masked line number with the last four digits visible
     */
    private static String maskLineNumber(String value) {

        if (value.length() <= 4) {
            return value;
        }

        return MASKED_VALUE + value.substring(value.length() - 4);
    }

    /**
     * Masks name fields by revealing the first character and replacing the remaining part with a fixed placeholder.
     * <p>
     * This method ensures sensitive information in name fields such as "firstName" or "lastName"
     * is obscured while maintaining the first character for partial identification.
     * </p>
     *
     * @param value the name to mask
     * @return the masked name, showing the first character followed by "******"
     */
    private static String maskName(String value) {

        if (value.length() <= 1) {
            return value;
        }

        return value.charAt(0) + MASKED_VALUE;
    }

}
