package com.ays.user.controller;

import com.ays.AbstractSystemTest;
import com.ays.auth.model.dto.request.AysLoginRequest;
import com.ays.auth.model.dto.request.AysTokenInvalidateRequest;
import com.ays.auth.model.dto.request.AysTokenRefreshRequest;
import com.ays.auth.model.dto.response.AysTokenResponse;
import com.ays.auth.model.mapper.AysTokenToAysTokenResponseMapper;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.model.dto.response.AysResponseBuilder;
import com.ays.common.util.exception.model.AysError;
import com.ays.user.model.dto.request.AysUserLoginRequestBuilder;
import com.ays.util.AysMockMvcRequestBuilders;
import com.ays.util.AysMockResultMatchersBuilders;
import com.ays.util.AysTestData;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class UserAuthSystemTest extends AbstractSystemTest {

    private final AysTokenToAysTokenResponseMapper aysTokenToAysTokenResponseMapper = AysTokenToAysTokenResponseMapper.initialize();

    private static final String BASE_PATH = "/api/v1/authentication";

    @Test
    void givenValidUserLoginRequest_whenTokensGeneratedSuccessfully_thenReturnTokenResponse() throws Exception {
        // Given
        AysLoginRequest loginRequest = new AysUserLoginRequestBuilder()
                .withUsername(AysTestData.User.VALID_USERNAME_ONE)
                .withPassword(AysTestData.User.VALID_PASSWORD_ONE)
                .build();

        // Then
        AysTokenResponse tokenResponse = aysTokenToAysTokenResponseMapper.map(userToken);
        AysResponse<AysTokenResponse> response = AysResponseBuilder.successOf(tokenResponse);
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(BASE_PATH.concat("/token"), loginRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(response.getHttpStatus().getReasonPhrase()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(response.getIsSuccess()))
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
                .refreshToken(userToken.getRefreshToken())
                .build();

        // Then
        AysTokenResponse tokenResponse = aysTokenToAysTokenResponseMapper.map(userToken);
        AysResponse<AysTokenResponse> response = AysResponseBuilder.successOf(tokenResponse);
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(BASE_PATH.concat("/token/refresh"), tokenRefreshRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(response.getHttpStatus().getReasonPhrase()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(response.getIsSuccess()))
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
                .refreshToken(userToken.getRefreshToken())
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/token/invalidate");
        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, userToken.getAccessToken(), tokenInvalidateRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

    @Test
    void givenValidAysTokenInvalidateRequest_whenUserUnauthorizedForTokensInvalidating_thenReturnAccessDeniedException() throws Exception {
        // Given
        AysTokenInvalidateRequest tokenInvalidateRequest = AysTokenInvalidateRequest.builder()
                .refreshToken(userToken.getRefreshToken())
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/token/invalidate");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, adminUserToken.getAccessToken(), tokenInvalidateRequest);

        AysResponse<AysError> response = AysResponseBuilder.FORBIDDEN;
        mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isForbidden())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(response.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(response.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

}
