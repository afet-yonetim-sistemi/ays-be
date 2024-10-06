package org.ays.util;

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

    public static JsonPathResultMatchers code() {
        return MockMvcResultMatchers.jsonPath("$.code");
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

    public static JsonPathResultMatchers response(String path) {
        return MockMvcResultMatchers.jsonPath("$.response.".concat(path));
    }

    public static JsonPathResultMatchers response(int index, String path) {
        return MockMvcResultMatchers.jsonPath("$.response[" + index + "].".concat(path));
    }

    public static JsonPathResultMatchers responseSize() {
        return MockMvcResultMatchers.jsonPath("$.response.size()");
    }

    public static JsonPathResultMatchers responses(String path) {
        return MockMvcResultMatchers.jsonPath("$.response[*].".concat(path));
    }

    public static JsonPathResultMatchers content() {
        return MockMvcResultMatchers.jsonPath("$.response.content");
    }

    public static JsonPathResultMatchers contents(String path) {
        return MockMvcResultMatchers.jsonPath("$.response.content[*].".concat(path));
    }

    public static JsonPathResultMatchers contentSize() {
        return MockMvcResultMatchers.jsonPath("$.response.content.size()");
    }

    public static JsonPathResultMatchers firstContent(String path) {
        return MockMvcResultMatchers.jsonPath("$.response.content[0].".concat(path));
    }

    public static JsonPathResultMatchers subErrors() {
        return MockMvcResultMatchers.jsonPath("$.subErrors");
    }

}
