package org.ays.common.filter;

import ch.qos.logback.classic.Level;
import org.awaitility.Awaitility;
import org.ays.AysEndToEndTest;
import org.ays.auth.model.response.AysUserResponse;
import org.ays.common.model.response.AysResponse;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.ays.util.AysValidTestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

class AysAuditLogFilterEndToEndTest extends AysEndToEndTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void givenValidRequestForPublicEndpoint_whenDoFilterSuccessfullyWith200StatusCode_thenSaveAuditLog() throws Exception {

        // Then
        String endpoint = "/public/actuator/info";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status()
                        .isOk());

        Awaitility.await()
                .atMost(1, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    // Verify
                    this.validateLastLogMessage("", endpoint, HttpStatus.OK);
                });
    }

    @Test
    void givenValidRequestForPrivateEndpoint_whenDoFilterSuccessfullyWith200StatusCode_thenSaveAuditLog() throws Exception {

        // Given
        String userId = AysValidTestData.SuperAdmin.ID;

        // Then
        String endpoint = "/api/institution/v1/user/".concat(userId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, superAdminToken.getAccessToken());

        AysResponse<AysUserResponse> mockResponse = AysResponse.successOf(new AysUserResponse());

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("id")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("emailAddress")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("firstName")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("lastName")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("phoneNumber.countryCode")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("phoneNumber.lineNumber")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("city")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("status")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("roles[*].id")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("roles[*].name")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("createdUser")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("createdAt")
                        .isNotEmpty());

        Awaitility.await()
                .atMost(1, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    // Verify
                    this.validateLastLogMessage(AysValidTestData.SuperAdmin.ID, endpoint, HttpStatus.OK);
                });
    }

    @Test
    void givenValidRequestForPrivateEndpoint_whenDoFilterSuccessfullyWith404StatusCode_thenSaveAuditLog() throws Exception {

        // Given
        String userId = "8734e9ad-906f-4fc0-8535-3589e410a22c";

        // Then
        String endpoint = "/api/institution/v1/user/".concat(userId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, superAdminToken.getAccessToken());

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isNotFound())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        Awaitility.await()
                .atMost(1, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    // Verify
                    this.validateLastLogMessage(AysValidTestData.SuperAdmin.ID, endpoint, HttpStatus.NOT_FOUND);
                });
    }


    private void validateLastLogMessage(String userId, String endpoint, HttpStatus httpStatus) {
        String logMessagePrefix = "Audit log saved: ";
        Optional<String> logMessage = logTracker.findMessage(Level.INFO, logMessagePrefix);
        Assertions.assertTrue(logMessage.isPresent());
        Assertions.assertTrue(logMessage.get().startsWith(logMessagePrefix));
        Assertions.assertTrue(logMessage.get().contains("\"id\":\""));
        Assertions.assertTrue(logMessage.get().contains("\"userId\":\"" + userId + "\""));
        Assertions.assertTrue(logMessage.get().contains("\"requestIpAddress\":\"127.0.0.1"));
        Assertions.assertTrue(logMessage.get().contains("\"requestReferer\":\"\""));
        Assertions.assertTrue(logMessage.get().contains("\"requestHttpMethod\":\"GET"));
        Assertions.assertTrue(logMessage.get().contains("\"requestPath\":\"" + endpoint + "\""));
        Assertions.assertTrue(logMessage.get().contains("\"requestBody\":\"\""));
        Assertions.assertTrue(logMessage.get().contains("\"responseHttpStatusCode\":" + httpStatus.value()));
        Assertions.assertTrue(logMessage.get().contains("\"responseBody\":\""));
        Assertions.assertTrue(logMessage.get().contains("\"requestedAt\":\""));
        Assertions.assertTrue(logMessage.get().contains("\"respondedAt\":\""));
    }

}
