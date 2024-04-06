package org.ays.admin_user.controller;

import org.ays.AbstractSystemTest;
import org.ays.auth.model.dto.request.AysLoginRequest;
import org.ays.auth.model.dto.request.AysLoginRequestBuilder;
import org.ays.auth.model.dto.request.AysTokenInvalidateRequest;
import org.ays.auth.model.dto.request.AysTokenRefreshRequest;
import org.ays.auth.model.dto.response.AysTokenResponse;
import org.ays.auth.model.dto.response.AysTokenResponseBuilder;
import org.ays.common.model.dto.response.AysResponse;
import org.ays.common.model.dto.response.AysResponseBuilder;
import org.ays.common.util.exception.model.AysErrorBuilder;
import org.ays.common.util.exception.model.AysErrorResponse;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.ays.util.AysValidTestData;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class AdminUserAuthSystemTest extends AbstractSystemTest {

    private static final String BASE_PATH = "/api/v1/authentication/admin";

    @Test
    void givenValidLoginRequest_whenTokensGeneratedSuccessfully_thenReturnTokenResponse() throws Exception {
        // Given
        AysLoginRequest loginRequest = new AysLoginRequestBuilder()
                .withUsername(AysValidTestData.AdminUser.USERNAME)
                .withPassword(AysValidTestData.AdminUser.PASSWORD).build();

        // Then
        String endpoint = BASE_PATH.concat("/token");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, loginRequest);

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

    @Test
    void givenValidTokenRefreshRequest_whenAccessTokenGeneratedSuccessfully_thenReturnTokenResponse() throws Exception {
        // Given
        AysTokenRefreshRequest tokenRefreshRequest = AysTokenRefreshRequest.builder()
                .refreshToken(adminUserToken.getRefreshToken())
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/token/refresh");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, tokenRefreshRequest);

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

    @Test
    void givenValidAysTokenInvalidateRequest_whenTokensInvalidated_thenReturnSuccessResponse() throws Exception {
        // Given
        AysTokenInvalidateRequest tokenInvalidateRequest = AysTokenInvalidateRequest.builder()
                .refreshToken(adminUserToken.getRefreshToken())
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/token/invalidate");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, adminUserToken.getAccessToken(), tokenInvalidateRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

    @Test
    void givenValidAysTokenInvalidateRequest_whenUserUnauthorizedForTokensInvalidating_thenReturnAccessDeniedException() throws Exception {
        // Given
        AysTokenInvalidateRequest tokenInvalidateRequest = AysTokenInvalidateRequest.builder()
                .refreshToken(adminUserToken.getRefreshToken())
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/token/invalidate");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, userToken.getAccessToken(), tokenInvalidateRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());
    }

}
