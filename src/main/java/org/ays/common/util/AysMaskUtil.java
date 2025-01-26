package org.ays.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

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

    /**
     * Masks sensitive fields in the given {@link JsonNode}.
     * <p>
     * This method processes the provided JSON node, masking sensitive fields based on their names.
     * It recursively iterates through JSON objects and arrays, applying masking rules where applicable.
     * The method supports scenarios where field-value pairs require context-based masking
     * (e.g., masking values based on associated field names).
     * </p>
     *
     * @param jsonNode the JSON node to process for masking
     */
    @SuppressWarnings("java:S135")
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

        final List<String> fieldNames = new ArrayList<>();
        objectNode.fieldNames().forEachRemaining(fieldNames::add);

        final ListIterator<String> fieldNamesIterator = fieldNames.listIterator();

        String previousFieldName = null;
        while (fieldNamesIterator.hasNext()) {

            String currentFieldName = fieldNamesIterator.next();
            JsonNode currentFieldValue = objectNode.get(currentFieldName);

            if (!currentFieldValue.isValueNode()) {
                mask(currentFieldValue);
                continue;
            }

            if ("field".equals(currentFieldName)) {
                previousFieldName = currentFieldValue.asText();
            }

            if ("value".equals(currentFieldName)) {
                String maskedValue = mask(previousFieldName, currentFieldValue.asText());
                objectNode.put(currentFieldName, maskedValue);
                continue;
            }

            String maskedValue = mask(currentFieldName, currentFieldValue.asText());
            objectNode.put(currentFieldName, maskedValue);
        }
    }

    /**
     * Masks the value of a specific field based on its name.
     * <p>
     * Sensitive fields such as "Authorization", "accessToken", "password", and others are masked
     * using specific rules defined in the {@link AysSensitiveMaskingCategory}. Fields not recognized as sensitive
     * remain unchanged. If a field is part of a message string, its value is also masked.
     * </p>
     *
     * @param field the name of the field to be checked
     * @param value the value to mask
     * @return the masked value, or the original value if no sensitive fields are detected
     */
    public static String mask(final String field, final String value) {

        if ("null".equalsIgnoreCase(value) || StringUtils.isBlank(value)) {
            return value;
        }

        final List<AysSensitiveMaskingCategory> sensitiveCategories = Arrays.asList(AysSensitiveMaskingCategory.values());

        final Optional<AysSensitiveMaskingCategory> sensitiveCategoryForField = sensitiveCategories.stream()
                .filter(category -> category.getFields().contains(field))
                .findFirst();
        if (sensitiveCategoryForField.isPresent()) {
            return sensitiveCategoryForField.get().mask(value);
        }

        for (AysSensitiveMaskingCategory category : sensitiveCategories) {

            for (String sensitiveField : category.getFields()) {

                if (!value.contains(sensitiveField)) {
                    continue;
                }

                return maskValue(value, category, sensitiveField);
            }

        }

        return value;
    }

    /**
     * Masks a sensitive field's value extracted from a string message.
     * <p>
     * This method identifies the value associated with a sensitive field
     * in the provided string, applies the appropriate masking logic
     * defined in the {@link AysSensitiveMaskingCategory}, and returns the masked result.
     * </p>
     *
     * @param value                       the string containing the sensitive field and its value
     * @param aysSensitiveMaskingCategory the {@link AysSensitiveMaskingCategory} that defines the masking logic
     * @param sensitiveField              the name of the sensitive field in the string
     * @return the original string with the sensitive field's value masked
     */
    private static String maskValue(final String value,
                                    final AysSensitiveMaskingCategory aysSensitiveMaskingCategory,
                                    final String sensitiveField) {

        final String fieldFromMessage = sensitiveField + ":";
        final String trimmedValue = value.replace(" ", "");
        final int beginIndex = trimmedValue.indexOf(fieldFromMessage) + fieldFromMessage.length();
        final String valueToBeMask = trimmedValue.substring(beginIndex);

        if (StringUtils.isBlank(valueToBeMask)) {
            return value;
        }

        final String maskedValue = aysSensitiveMaskingCategory.mask(valueToBeMask);
        return value.replace(valueToBeMask, maskedValue);
    }

}
