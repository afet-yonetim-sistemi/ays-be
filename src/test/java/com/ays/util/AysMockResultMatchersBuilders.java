package com.ays.util;

import lombok.experimental.UtilityClass;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;

@UtilityClass
public class AysMockResultMatchersBuilders {

    public static StatusResultMatchers status() {
        return MockMvcResultMatchers.status();
    }

    public static JsonPathResultMatchers time() {
        return MockMvcResultMatchers.jsonPath("$.time");
    }


    public static JsonPathResultMatchers httpStatus() {
        return MockMvcResultMatchers.jsonPath("$.httpStatus");
    }

    public static JsonPathResultMatchers header() {
        return MockMvcResultMatchers.jsonPath("$.header");
    }

    public static JsonPathResultMatchers isSuccess() {
        return MockMvcResultMatchers.jsonPath("$.isSuccess");
    }


    public static JsonPathResultMatchers response() {
        return MockMvcResultMatchers.jsonPath("$.response");
    }

    public static JsonPathResultMatchers subErrors() {
        return MockMvcResultMatchers.jsonPath("$.subErrors");
    }

}
