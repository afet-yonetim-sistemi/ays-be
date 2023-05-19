package com.ays.admin_user.controller;

import com.ays.AbstractRestControllerTest;
import com.ays.admin_user.model.dto.request.AdminUserRegisterRequest;
import com.ays.admin_user.model.dto.request.AdminUserRegisterRequestBuilder;
import com.ays.admin_user.service.AdminUserAuthService;
import com.ays.admin_user.service.AdminUserRegisterService;
import com.ays.auth.model.dto.request.*;
import com.ays.auth.model.dto.response.AysTokenResponse;
import com.ays.auth.model.mapper.AysTokenToAysTokenResponseMapper;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.model.dto.response.AysResponseBuilder;
import com.ays.common.util.exception.model.AysError;
import com.ays.util.AysMockMvcRequestBuilders;
import com.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class AdminUserAuthControllerTest extends AbstractRestControllerTest {

    @MockBean
    private AdminUserAuthService adminUserAuthService;

    @MockBean
    private AdminUserRegisterService adminUserRegisterService;


    private static final AysTokenToAysTokenResponseMapper AYS_TOKEN_TO_AYS_TOKEN_RESPONSE_MAPPER = AysTokenToAysTokenResponseMapper.initialize();

    private static final String BASE_PATH = "/api/v1/authentication/admin";

    @Test
    void givenValidAdminUserRegisterRequest_whenAdminUserRegistered_thenReturnSuccessResponse() throws Exception {
        // Given
        AdminUserRegisterRequest mockRequest = new AdminUserRegisterRequestBuilder()
                .withValidFields().build();

        // When
        Mockito.doNothing().when(adminUserRegisterService).register(Mockito.any());

        // Then
        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(BASE_PATH.concat("/register"), mockRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.time").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value(mockResponse.getHttpStatus().getReasonPhrase()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(mockResponse.getIsSuccess()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").doesNotExist());

        Mockito.verify(adminUserRegisterService, Mockito.times(1)).register(Mockito.any());
    }

    @Test
    void givenValidLoginRequest_whenTokensGeneratedSuccessfully_thenReturnTokenResponse() throws Exception {
        // Given
        AysLoginRequest mockRequest = new AysLoginRequestBuilder().build();

        // When
        Mockito.when(adminUserAuthService.authenticate(Mockito.any())).thenReturn(mockAdminUserToken);

        // Then
        AysTokenResponse mockResponse = AYS_TOKEN_TO_AYS_TOKEN_RESPONSE_MAPPER.map(mockAdminUserToken);
        AysResponse<AysTokenResponse> mockAysResponse = AysResponseBuilder.successOf(mockResponse);
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(BASE_PATH.concat("/token"), mockRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.time").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value(mockAysResponse.getHttpStatus().getReasonPhrase()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(mockAysResponse.getIsSuccess()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.accessToken").value(mockAysResponse.getResponse().getAccessToken()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.accessTokenExpiresAt").value(mockAysResponse.getResponse().getAccessTokenExpiresAt()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.refreshToken").value(mockAysResponse.getResponse().getRefreshToken()));

        Mockito.verify(adminUserAuthService, Mockito.times(1)).authenticate(Mockito.any());
    }

    @Test
    void givenValidTokenRefreshRequest_whenAccessTokenGeneratedSuccessfully_thenReturnTokenResponse() throws Exception {
        // Given
        AysTokenRefreshRequest mockRequest = AysTokenRefreshRequestBuilder.VALID_FOR_ADMIN;

        // When
        Mockito.when(adminUserAuthService.refreshAccessToken(Mockito.any())).thenReturn(mockAdminUserToken);

        // Then
        AysTokenResponse mockResponse = AYS_TOKEN_TO_AYS_TOKEN_RESPONSE_MAPPER.map(mockAdminUserToken);
        AysResponse<AysTokenResponse> mockAysResponse = AysResponseBuilder.successOf(mockResponse);
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(BASE_PATH.concat("/token/refresh"), mockRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.time").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value(mockAysResponse.getHttpStatus().getReasonPhrase()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(mockAysResponse.getIsSuccess()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.accessToken").value(mockAysResponse.getResponse().getAccessToken()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.accessTokenExpiresAt").value(mockAysResponse.getResponse().getAccessTokenExpiresAt()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.refreshToken").value(mockAysResponse.getResponse().getRefreshToken()));

        Mockito.verify(adminUserAuthService, Mockito.times(1)).refreshAccessToken(Mockito.any());
    }


    @Test
    void givenValidAysTokenInvalidateRequest_whenTokensInvalidated_thenReturnSuccessResponse() throws Exception {
        // Given
        AysTokenInvalidateRequest mockRequest = AysTokenInvalidateRequestBuilder.VALID_FOR_ADMIN;

        // When
        Mockito.doNothing().when(adminUserAuthService).invalidateTokens(Mockito.any());

        // Then
        String endpoint = BASE_PATH.concat("/token/invalidate");
        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, mockAdminUserToken.getAccessToken(), mockRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.time")
                        .isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus")
                        .value(mockResponse.getHttpStatus().getReasonPhrase()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess")
                        .value(mockResponse.getIsSuccess()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response")
                        .doesNotExist());

        Mockito.verify(adminUserAuthService, Mockito.times(1))
                .invalidateTokens(Mockito.any());
    }

    @Test
    void givenValidAysTokenInvalidateRequest_whenUserUnauthorizedForTokensInvalidating_thenReturnAccessDeniedException() throws Exception {
        // Given
        AysTokenInvalidateRequest mockRequest = AysTokenInvalidateRequestBuilder.VALID_FOR_ADMIN;

        // Then
        String endpoint = BASE_PATH.concat("/token/invalidate");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken(), mockRequest);

        AysResponse<AysError> mockResponse = AysResponseBuilder.UNAUTHORIZED;
        mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isUnauthorized())
                .andExpect(AysMockResultMatchersBuilders.time().isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus().value(mockResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess().value(mockResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response().doesNotExist());
    }

}
