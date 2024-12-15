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
        logWatcher = new ListAppender<>();
        logWatcher.start();
        ((Logger) LoggerFactory.getLogger(AysAuditLogRepositoryImpl.class))
                .addAppender(logWatcher);
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
                    Assertions.assertEquals(Level.DEBUG, this.getLastLogLevel());
                    String lastLogMessage = this.getLastLogMessage();
                    Assertions.assertTrue(lastLogMessage.contains("Audit log saved: "));
                    Assertions.assertTrue(lastLogMessage.contains("\"id\":\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"user_id\":\"\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"request_ip_address\":\"127.0.0.1"));
                    Assertions.assertTrue(lastLogMessage.contains("\"request_referer\":\"\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"request_http_method\":\"GET"));
                    Assertions.assertTrue(lastLogMessage.contains("\"request_path\":\"" + endpoint + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"request_body\":\"\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"response_http_status_code\":200"));
                    Assertions.assertTrue(lastLogMessage.contains("\"response_body\":\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"requested_at\":\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"responded_at\":\""));
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
                    Assertions.assertEquals(Level.DEBUG, this.getLastLogLevel());
                    String lastLogMessage = this.getLastLogMessage();
                    Assertions.assertTrue(lastLogMessage.contains("Audit log saved: "));
                    Assertions.assertTrue(lastLogMessage.contains("\"id\":\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"user_id\":\"" + AysValidTestData.SuperAdmin.ID + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"request_ip_address\":\"127.0.0.1"));
                    Assertions.assertTrue(lastLogMessage.contains("\"request_referer\":\"\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"request_http_method\":\"GET"));
                    Assertions.assertTrue(lastLogMessage.contains("\"request_path\":\"" + endpoint + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"request_body\":\"\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"response_http_status_code\":200"));
                    Assertions.assertTrue(lastLogMessage.contains("\"response_body\":\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"requested_at\":\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"responded_at\":\""));
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
                    Assertions.assertEquals(Level.DEBUG, this.getLastLogLevel());
                    String lastLogMessage = this.getLastLogMessage();
                    Assertions.assertTrue(lastLogMessage.contains("Audit log saved: "));
                    Assertions.assertTrue(lastLogMessage.contains("\"id\":\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"user_id\":\"" + AysValidTestData.SuperAdmin.ID + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"request_ip_address\":\"127.0.0.1"));
                    Assertions.assertTrue(lastLogMessage.contains("\"request_referer\":\"\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"request_http_method\":\"GET"));
                    Assertions.assertTrue(lastLogMessage.contains("\"request_path\":\"" + endpoint + "\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"request_body\":\"\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"response_http_status_code\":404"));
                    Assertions.assertTrue(lastLogMessage.contains("\"response_body\":\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"requested_at\":\""));
                    Assertions.assertTrue(lastLogMessage.contains("\"responded_at\":\""));
                });
    }

    private String getLastLogMessage() {
        if (logWatcher.list.isEmpty()) {
            return null;
        }

        int logSize = logWatcher.list.size();
        return logWatcher.list.get(logSize - 1).getFormattedMessage();
    }

    private Level getLastLogLevel() {

        if (logWatcher.list.isEmpty()) {
            return null;
        }

        int logSize = logWatcher.list.size();
        return logWatcher.list.get(logSize - 1).getLevel();
    }

}
