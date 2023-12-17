package com.ays.common.util;

import lombok.experimental.UtilityClass;

import java.util.Random;

/**
 * Utility class for generating random string.
 * This class provides methods for generating a random string with a specified length.
 * <p>
 * The class uses the Java built-in {@link Random} class for generating random string.
 * <p>
 * Note that this class is a utility class and should not be instantiated.
 * All methods are static and can be called using the class name.
 */
@UtilityClass
public class AysRandomTestUtil {

    private static final Random RANDOM = new Random();


    /**
     * Generates a random string with the specified length.
     * The generated string will be a random string with the specified length, consisting of uppercase and lowercase
     * letters and digits.
     *
     * @param length the length of the generated string
     * @return a random string with the specified length
     */
    public static String generateString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            stringBuilder.append(characters.charAt(RANDOM.nextInt(characters.length())));
        }
        return stringBuilder.toString();
    }

}
