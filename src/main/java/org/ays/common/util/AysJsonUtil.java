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

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
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
    public static String toJson(Object object) {
        return GSON.toJson(object);
    }

    /**
     * Converts a formatted JSON string into an unformatted (compact) JSON string.
     * <p>
     * This method parses the input JSON string into a tree structure using the {@link ObjectMapper}
     * and then converts it back to a compact string representation.
     * If an exception occurs during parsing, an empty string is returned.
     *
     * @param json the formatted JSON string to be converted
     * @return a compact (unformatted) JSON string, or an empty string if the input is invalid
     */
    public static String toEscapedJson(final String json) {
        try {
            final JsonNode jsonNode = OBJECT_MAPPER.readTree(json);
            return jsonNode.toString().replace("\"", "\\\"");
        } catch (JsonProcessingException exception) {
            return "";
        }
    }

}
