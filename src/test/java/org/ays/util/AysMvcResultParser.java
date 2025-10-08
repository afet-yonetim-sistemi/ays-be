package org.ays.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

@UtilityClass
public final class AysMvcResultParser {


    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }


    public static <T> Optional<T> response(MvcResult mvcResult, Class<T> response) {
        try {
            final String contentAsString = mvcResult.getResponse().getContentAsString();
            final String responseField = OBJECT_MAPPER.readTree(contentAsString).get("response").toString();
            return Optional.ofNullable(OBJECT_MAPPER.readValue(responseField, response));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

}
