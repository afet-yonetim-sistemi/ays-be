package org.ays.common.controller;

import org.ays.AysEndToEndTest;
import org.ays.util.AysMockMvcRequestBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class AysActuatorEndToEndTest extends AysEndToEndTest {

    @Value("${info.application.name}")
    private String applicationName;

    @Value("${info.application.description}")
    private String applicationDescription;

    @Value("${info.application.version}")
    private String applicationVersion;

    @Value("${info.build.number}")
    private String buildNumber;

    private static final String BASE_PATH = "/public/actuator";

    @Test
    void whenActuatorInfoExist_thenReturnApplicationAndBuildInfo() throws Exception {

        // Then
        String endpoint = BASE_PATH.concat("/info");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint);

        aysMockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(MockMvcResultMatchers.jsonPath("$.application")
                        .exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.application.name")
                        .value(applicationName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.application.description")
                        .value(applicationDescription))
                .andExpect(MockMvcResultMatchers.jsonPath("$.application.version")
                        .value(applicationVersion))
                .andExpect(MockMvcResultMatchers.jsonPath("$.build")
                        .exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.build.version")
                        .doesNotHaveJsonPath())
                .andExpect(MockMvcResultMatchers.jsonPath("$.build.number")
                        .value(buildNumber))
                .andExpect(MockMvcResultMatchers.jsonPath("$.build.artifact")
                        .doesNotHaveJsonPath())
                .andExpect(MockMvcResultMatchers.jsonPath("$.build.name")
                        .doesNotHaveJsonPath())
                .andExpect(MockMvcResultMatchers.jsonPath("$.build.time")
                        .doesNotHaveJsonPath())
                .andExpect(MockMvcResultMatchers.jsonPath("$.build.group")
                        .doesNotHaveJsonPath());
    }
}
