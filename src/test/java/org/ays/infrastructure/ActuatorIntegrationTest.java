package org.ays.infrastructure;

import org.ays.AysInfrastructureIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class ActuatorIntegrationTest extends AysInfrastructureIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    BuildProperties buildProperties;

    @Test
    void givenBuildInfoProperties_whenGetActuatorInfo_thenReturnBuildNumber() throws Exception {

        // Given
        String expectedBuildNumber = buildProperties.get("buildNumber");

        // Then
        mockMvc.perform(get("/public/actuator/info"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.build").exists())
                .andExpect(jsonPath("$.build.buildNumber").exists())
                .andExpect(jsonPath("$.build.buildNumber").value(expectedBuildNumber));
    }
}
