package org.ays.common.util;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class AysUUID {

    /**
     * Checks if the given string is a valid UUID (Universally Unique Identifier).
     * The method attempts to create a UUID object from the given string and returns true if successful, false otherwise.
     *
     * @param value the string to check
     * @return true if the string is a valid UUID, false otherwise
     */
    public static boolean isValid(String value) {
        try {
            UUID.fromString(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
