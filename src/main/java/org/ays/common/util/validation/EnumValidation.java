package org.ays.common.util.validation;

import lombok.experimental.UtilityClass;

import java.util.Set;

/**
 * Utility class for validating if an Enum value is contained within a set of accepted values.
 */
@UtilityClass
public class EnumValidation {

    /**
     * Determines if a given Enum value is contained within a set of accepted values.
     *
     * @param value          The Enum value to be checked.
     * @param acceptedValues The set of accepted Enum values.
     * @param <T>            The type of the Enum.
     * @return true if the given Enum value is contained within the set of accepted values, false otherwise.
     */
    public static <T extends Enum<T>> boolean anyOf(T value, Set<T> acceptedValues) {
        return acceptedValues.contains(value);
    }

}
