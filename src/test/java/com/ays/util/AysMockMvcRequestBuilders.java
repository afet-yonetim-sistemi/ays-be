package com.ays.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Slf4j
@UtilityClass
public class AysMockMvcRequestBuilders {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public MockHttpServletRequestBuilder post(String endpoint, Object requestBody) {
        return MockMvcRequestBuilders.post(endpoint)
                .content(GSON.toJson(requestBody))
                .contentType(MediaType.APPLICATION_JSON);
    }

}
