package com.ays.common.util;

import lombok.experimental.UtilityClass;

import java.util.Random;
import java.util.UUID;

@UtilityClass
public class AysRandomUtil {

    private static final Random RANDOM = new Random();

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


    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

}
