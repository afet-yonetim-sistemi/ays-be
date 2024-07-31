package org.ays.common.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;
import java.util.UUID;

/**
 * Utility class for generating random numbers and UUIDs.
 * This class provides methods for generating a random number with a specified number of digits, as well as a method for
 * generating a UUID string.
 * <p>
 * The class uses the Java built-in {@link Random} class for generating random numbers and the {@link UUID} class for
 * generating UUIDs.
 * <p>
 * Note that this class is a utility class and should not be instantiated.
 * All methods are static and can be called using the class name.
 */
@UtilityClass
public class AysRandomUtil {

    private static final Random RANDOM = new Random();


    /**
     * Generates a random number with the specified number of digits.
     * The generated number will be a positive long value with the specified number of digits.
     *
     * @param length the number of digits the generated number should have
     * @return a random number with the specified number of digits
     */
    public static Long generateNumber(int length) {
        long minimumValue = generateMinimumValue(length);
        long maximumValue = generateMaximumValue(length);
        return RANDOM.nextLong(minimumValue, maximumValue);
    }

    private static Long generateMinimumValue(int length) {
        return (long) Math.pow(10.0, length - 1.0);
    }

    private static Long generateMaximumValue(int length) {
        return (long) Math.pow(10.0, length);
    }


    /**
     * Generates a random UUID (Universally Unique Identifier) string.
     * The generated string will be a unique identifier with a length of 36 characters, consisting of hexadecimal digits
     * separated by hyphens.
     *
     * @return a random UUID string
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }


    /**
     * Utility method to generate a random alphabetic string of the specified length.
     * <p>
     * This method uses the {@link RandomStringUtils#randomAlphabetic(int)} to generate a string
     * containing random alphabetic characters (a-z, A-Z). The length of the generated string is
     * determined by the input parameter.
     * </p>
     *
     * @param length the length of the random string to generate
     * @return a random alphabetic string of the specified length
     * @throws IllegalArgumentException if the specified length is negative
     */
    public static String generateText(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

}
