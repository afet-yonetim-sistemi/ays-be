package org.ays.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.experimental.UtilityClass;

/**
 * This is a utility class to handle JSON operations within the project.
 */
@UtilityClass
public class AysJsonUtil {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Converts an object to a JSON string.
     *
     * @param object the object to convert
     * @return a JSON string representation of the object
     */
    public static String toJson(Object object) {
        return GSON.toJson(object);
    }
}
