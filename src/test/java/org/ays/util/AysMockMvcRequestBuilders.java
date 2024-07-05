package org.ays.util;

import lombok.experimental.UtilityClass;
import org.ays.common.util.AysJsonUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@UtilityClass
public class AysMockMvcRequestBuilders {

    public MockHttpServletRequestBuilder post(String endpoint, Object requestBody) {
        return MockMvcRequestBuilders.post(endpoint)
                .content(AysJsonUtil.toJson(requestBody))
                .contentType(MediaType.APPLICATION_JSON);
    }

    public MockHttpServletRequestBuilder post(String endpoint, String token) {
        return MockMvcRequestBuilders.post(endpoint)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getTokenWithBearerPrefix(token))
                .contentType(MediaType.APPLICATION_JSON);
    }

    public MockHttpServletRequestBuilder get(String endpoint) {
        return MockMvcRequestBuilders.get(endpoint);
    }

    public MockHttpServletRequestBuilder get(String endpoint, String token) {
        return MockMvcRequestBuilders.get(endpoint)
                .header(HttpHeaders.AUTHORIZATION, getTokenWithBearerPrefix(token));
    }

    public MockHttpServletRequestBuilder get(String endpoint, String token, Object requestBody) {
        return MockMvcRequestBuilders.get(endpoint)
                .header(HttpHeaders.AUTHORIZATION, getTokenWithBearerPrefix(token))
                .content(AysJsonUtil.toJson(requestBody))
                .contentType(MediaType.APPLICATION_JSON);
    }

    public MockHttpServletRequestBuilder post(String endpoint, String token, Object requestBody) {
        return MockMvcRequestBuilders.post(endpoint)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getTokenWithBearerPrefix(token))
                .content(AysJsonUtil.toJson(requestBody))
                .contentType(MediaType.APPLICATION_JSON);
    }

    public MockHttpServletRequestBuilder put(String endpoint, String token, Object requestBody) {
        return MockMvcRequestBuilders.put(endpoint)
                .header(HttpHeaders.AUTHORIZATION, getTokenWithBearerPrefix(token))
                .content(AysJsonUtil.toJson(requestBody))
                .contentType(MediaType.APPLICATION_JSON);
    }

    public MockHttpServletRequestBuilder delete(String endpoint, String token) {
        return MockMvcRequestBuilders.delete(endpoint)
                .header(HttpHeaders.AUTHORIZATION, getTokenWithBearerPrefix(token));
    }

    public MockHttpServletRequestBuilder delete(String endpoint, String token, Object requestBody) {
        return MockMvcRequestBuilders.delete(endpoint)
                .header(HttpHeaders.AUTHORIZATION, getTokenWithBearerPrefix(token))
                .content(AysJsonUtil.toJson(requestBody))
                .contentType(MediaType.APPLICATION_JSON);
    }

    public MockHttpServletRequestBuilder patch(String endpoint, String token) {
        return MockMvcRequestBuilders.patch(endpoint)
                .header(HttpHeaders.AUTHORIZATION, getTokenWithBearerPrefix(token))
                .contentType(MediaType.APPLICATION_JSON);
    }

    private String getTokenWithBearerPrefix(String token) {
        return "Bearer " + token;
    }
}
