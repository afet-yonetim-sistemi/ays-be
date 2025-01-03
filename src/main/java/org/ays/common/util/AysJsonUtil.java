package org.ays.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;

/**
 * This is a utility class to handle JSON operations within the project.
 */
@UtilityClass
public class AysJsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    /**
     * Converts an object to a JSON string.
     *
     * @param object the object to convert
     * @return a JSON string representation of the object
     */
    public static String toJson(final Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException exception) {
            return "";
        }
    }

    /**
     * Converts a JSON string into a masked and escaped JSON string.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Parses the input JSON string into a tree structure using the {@link ObjectMapper}.</li>
     *     <li>Masks sensitive fields in the JSON using {@link AysMaskUtil}.</li>
     *     <li>Escapes double quotes in the JSON string by replacing them with backslash-escaped quotes.</li>
     * </ul>
     * If an exception occurs during parsing or masking, an empty string is returned.
     * </p>
     *
     * @param json The JSON string to be masked and escaped.
     * @return A masked and escaped JSON string, or an empty string if the input is invalid.
     */
    public static String toMaskedEscapedJson(final String json) {
        try {
            final JsonNode jsonNode = OBJECT_MAPPER.readTree(json);
            AysMaskUtil.mask(jsonNode);
            return jsonNode.toString().replace("\"", "\\\"");
        } catch (JsonProcessingException exception) {
            return "";
        }
    }

}
