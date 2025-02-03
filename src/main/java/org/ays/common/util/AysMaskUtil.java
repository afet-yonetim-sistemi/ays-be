package org.ays.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
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
                continue;
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
     * Masks the value of a specific field based on its name or content.
     * <p>
     * This method applies masking to sensitive fields such as "Authorization", "accessToken", "password", and others,
     * using rules defined in the {@link AysSensitiveMaskingCategory}. If the field name or the value contains sensitive
     * fields as part of a message, their values are identified and masked accordingly.
     * </p>
     * <p>
     * Fields that do not match any of the predefined sensitive categories remain unchanged.
     * </p>
     *
     * @param field the name of the field to be checked and potentially masked
     * @param value the value to mask
     * @return the masked value if the field or its content matches a sensitive category; otherwise, the original value
     */
    public static String mask(final String field, final String value) {

        if ("null".equalsIgnoreCase(value) || StringUtils.isBlank(value)) {
            return value;
        }

        final List<AysSensitiveMaskingCategory> sensitiveMaskingCategories = Arrays.asList(AysSensitiveMaskingCategory.values());

        final Optional<AysSensitiveMaskingCategory> sensitiveCategoryForField = sensitiveMaskingCategories.stream()
                .filter(category -> category.getFields().stream()
                        .anyMatch(fieldOfCategory -> StringUtils.containsIgnoreCase(field, fieldOfCategory))
                )
                .findFirst();

        if (sensitiveCategoryForField.isPresent()) {
            return sensitiveCategoryForField.get().mask(value);
        }

        return applyMaskForMatchingCategories(sensitiveMaskingCategories, value);
    }

    /**
     * Iterates through all sensitive masking categories and applies masking for any matching fields found
     * within the value string.
     * <p>
     * This method checks if the provided value contains any sensitive fields defined in the {@link AysSensitiveMaskingCategory}.
     * If a match is found, it applies the appropriate masking rules for the corresponding category.
     * </p>
     *
     * @param sensitiveMaskingCategories the list of sensitive masking categories to evaluate
     * @param value                      the input string to check and mask
     * @return the masked string if sensitive fields are found; otherwise, the original string
     */
    private static String applyMaskForMatchingCategories(final List<AysSensitiveMaskingCategory> sensitiveMaskingCategories,
                                                         final String value) {

        final Map<AysSensitiveMaskingCategory, String> sensitiveFields = new EnumMap<>(AysSensitiveMaskingCategory.class);
        for (AysSensitiveMaskingCategory category : sensitiveMaskingCategories) {

            for (String sensitiveField : category.getFields()) {

                if (!value.contains(sensitiveField)) {
                    continue;
                }

                sensitiveFields.put(category, sensitiveField);
            }
        }

        if (sensitiveFields.isEmpty()) {
            return value;
        }

        return maskAndReplaceWithMaskedValue(value, sensitiveFields);
    }

    /**
     * Masks and replaces sensitive values found in a string with their masked equivalents.
     * <p>
     * This method processes each sensitive field detected in the input string. For each field, it extracts the associated
     * value, applies the appropriate masking rule from the {@link AysSensitiveMaskingCategory}, and replaces the original
     * value in the string with the masked version.
     * </p>
     *
     * @param value           the original string containing sensitive values
     * @param sensitiveFields a map where the keys are {@link AysSensitiveMaskingCategory} objects representing masking rules,
     *                        and the values are the corresponding field names whose values need to be masked
     * @return the updated string with all sensitive values masked
     */
    private static String maskAndReplaceWithMaskedValue(final String value,
                                                        final Map<AysSensitiveMaskingCategory, String> sensitiveFields) {

        String maskedValue = value;
        for (Map.Entry<AysSensitiveMaskingCategory, String> entry : sensitiveFields.entrySet()) {

            final String sensitiveField = entry.getValue();
            final String fieldFromMessage = sensitiveField + ":";
            final String trimmedValue = maskedValue.replace(" ", "");
            final int beginIndex = trimmedValue.indexOf(fieldFromMessage) + fieldFromMessage.length();
            final String dataToBeMask = trimmedValue.substring(beginIndex).split(",")[0];

            if (StringUtils.isBlank(dataToBeMask)) {
                return value;
            }

            final AysSensitiveMaskingCategory sensitiveMaskingCategory = entry.getKey();
            final String maskedData = sensitiveMaskingCategory.mask(dataToBeMask);
            maskedValue = maskedValue.replace(dataToBeMask, maskedData);
        }

        return maskedValue;
    }

}
