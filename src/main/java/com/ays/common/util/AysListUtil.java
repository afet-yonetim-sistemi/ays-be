package com.ays.common.util;

import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * This utility class provides a method for converting a generic object to a List of a specified class type.
 */
@UtilityClass
public class AysListUtil {

    /**
     * Converts an object to a List of the specified class type.
     *
     * @param object the object to convert
     * @param clazz  the class type to convert the object to
     * @param <C>    the type parameter for the class
     * @return a List of objects of the specified class type
     */
    @SuppressWarnings({"unchecked", "unused"})
    public static <C> List<C> to(Object object, Class<C> clazz) {
        return (List<C>) object;
    }

}
