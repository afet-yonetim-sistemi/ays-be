package org.ays.auth.filter;

import org.ays.AysEndToEndTest;
import org.ays.auth.model.enums.AysSourcePage;
import org.ays.auth.model.request.AysLoginRequest;
import org.ays.auth.model.request.AysLoginRequestBuilder;
import org.ays.auth.model.response.AysTokenResponse;
import org.ays.auth.model.response.AysTokenResponseBuilder;
import org.ays.auth.model.response.AysUserResponse;
import org.ays.common.model.response.AysResponse;
import org.ays.common.model.response.AysResponseBuilder;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.ays.util.AysValidTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class AysBearerTokenAuthenticationFilterEndToEndTest extends AysEndToEndTest {

    @Autowired
    private MockMvc mockMvc;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("ays.rate-limit.authorized.enabled", () -> "true");
        registry.add("ays.rate-limit.unauthorized.enabled", () -> "true");
    }


    @Test
    void givenValidRequest_whenRateLimitNotExceededInHealthCheckEndpoint_thenReturnSuccess() throws Exception {

        // Then
        String endpoint = "/public/actuator/info";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint);

        for (int requestCount = 1; requestCount <= 10; requestCount++) {

            mockMvc.perform(mockHttpServletRequestBuilder)
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(MockMvcResultMatchers.status()
                            .isOk());
        }

    }

    @Test
    void givenValidRequest_whenRateLimitExceededInPublicEndpoint_thenReturnTooManyRequestsError() throws Exception {

        // Given
        AysLoginRequest loginRequest = new AysLoginRequestBuilder()
                .withEmailAddress(AysValidTestData.SuperAdmin.EMAIL_ADDRESS)
                .withPassword(AysValidTestData.PASSWORD)
                .withSourcePage(AysSourcePage.INSTITUTION)
                .build();

        // Then
        String endpoint = "/api/v1/authentication/token";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, loginRequest);

        for (int requestCount = 1; requestCount <= 10; requestCount++) {

            if (requestCount > 5) {

                mockMvc.perform(mockHttpServletRequestBuilder)
                        .andExpect(AysMockResultMatchersBuilders.status()
                                .isTooManyRequests());

                continue;
            }

            AysResponse<AysTokenResponse> mockResponse = AysResponseBuilder
                    .successOf(new AysTokenResponseBuilder().build());
            aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                    .andExpect(AysMockResultMatchersBuilders.status()
                            .isOk())
                    .andExpect(AysMockResultMatchersBuilders.response()
                            .isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.response.accessToken")
                            .isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.response.accessTokenExpiresAt")
                            .isNotEmpty())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.response.refreshToken")
                            .isNotEmpty());
        }

    }

    @Test
    void givenValidRequest_whenRateLimitExceededInPrivateEndpoint_thenReturnTooManyRequestsError() throws Exception {

        // Given
        String userId = AysValidTestData.SuperAdmin.ID;

        // Then
        String endpoint = "/api/v1/user/".concat(userId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, superAdminToken.getAccessToken());

        for (int requestCount = 1; requestCount <= 25; requestCount++) {

            if (requestCount > 20) {

                mockMvc.perform(mockHttpServletRequestBuilder)
                        .andExpect(AysMockResultMatchersBuilders.status()
                                .isTooManyRequests());

                continue;
            }

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
        }

    }

}
