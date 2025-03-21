package org.ays.common.filter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.awaitility.Awaitility;
import org.ays.AysEndToEndTest;
import org.ays.auth.model.response.AysUserResponse;
import org.ays.common.model.response.AysResponse;
import org.ays.common.repository.impl.AysAuditLogRepositoryImpl;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.ays.util.AysValidTestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.concurrent.TimeUnit;

class AysAuditLogFilterEndToEndTest extends AysEndToEndTest {

    @Autowired
    private MockMvc mockMvc;


    private ListAppender<ILoggingEvent> logWatcher;

    @BeforeEach
    void start() {
        this.logWatcher = new ListAppender<>();
        this.logWatcher.start();
        ((Logger) LoggerFactory.getLogger(AysAuditLogRepositoryImpl.class))
                .addAppender(this.logWatcher);
    }

    @AfterEach
    void detach() {
        ((Logger) LoggerFactory.getLogger(AysAuditLogRepositoryImpl.class))
                .detachAndStopAllAppenders();
    }


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
                    Assertions.assertEquals(Level.DEBUG, this.getLastLogLevel());
                });
    }

    @Test
    void givenValidRequestForPrivateEndpoint_whenDoFilterSuccessfullyWith200StatusCode_thenSaveAuditLog() throws Exception {

        // Given
        String userId = AysValidTestData.SuperAdmin.ID;

        // Then
        String endpoint = "/api/v1/user/".concat(userId);
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
                    Assertions.assertEquals(Level.DEBUG, this.getLastLogLevel());
                });
    }

    @Test
    void givenValidRequestForPrivateEndpoint_whenDoFilterSuccessfullyWith404StatusCode_thenSaveAuditLog() throws Exception {

        // Given
        String userId = "8734e9ad-906f-4fc0-8535-3589e410a22c";

        // Then
        String endpoint = "/api/v1/user/".concat(userId);
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
                    Assertions.assertEquals(Level.DEBUG, this.getLastLogLevel());
                });
    }


    private void validateLastLogMessage(String userId, String endpoint, HttpStatus httpStatus) {
        String lastLogMessage = this.getLastLogMessage();
        Assertions.assertNotNull(lastLogMessage);
        Assertions.assertTrue(lastLogMessage.startsWith("Audit log saved: "));
        Assertions.assertTrue(lastLogMessage.contains("\"id\":\""));
        Assertions.assertTrue(lastLogMessage.contains("\"userId\":\"" + userId + "\""));
        Assertions.assertTrue(lastLogMessage.contains("\"requestIpAddress\":\"127.0.0.1"));
        Assertions.assertTrue(lastLogMessage.contains("\"requestReferer\":\"\""));
        Assertions.assertTrue(lastLogMessage.contains("\"requestHttpMethod\":\"GET"));
        Assertions.assertTrue(lastLogMessage.contains("\"requestPath\":\"" + endpoint + "\""));
        Assertions.assertTrue(lastLogMessage.contains("\"requestBody\":\"\""));
        Assertions.assertTrue(lastLogMessage.contains("\"responseHttpStatusCode\":" + httpStatus.value()));
        Assertions.assertTrue(lastLogMessage.contains("\"responseBody\":\""));
        Assertions.assertTrue(lastLogMessage.contains("\"requestedAt\":\""));
        Assertions.assertTrue(lastLogMessage.contains("\"respondedAt\":\""));
    }

    private String getLastLogMessage() {

        if (this.logWatcher.list.isEmpty()) {
            return null;
        }

        int logSize = this.logWatcher.list.size();
        return this.logWatcher.list.get(logSize - 1).getFormattedMessage();
    }

    private Level getLastLogLevel() {

        if (this.logWatcher.list.isEmpty()) {
            return null;
        }

        int logSize = this.logWatcher.list.size();
        return this.logWatcher.list.get(logSize - 1).getLevel();
    }

}
