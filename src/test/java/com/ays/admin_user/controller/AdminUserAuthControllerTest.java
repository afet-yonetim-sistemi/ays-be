package com.ays.admin_user.controller;

import com.ays.AbstractRestControllerTest;
import com.ays.admin_user.model.dto.request.AdminUserRegisterRequest;
import com.ays.admin_user.model.dto.request.AdminUserRegisterRequestBuilder;
import com.ays.admin_user.service.AdminUserAuthService;
import com.ays.auth.model.AysToken;
import com.ays.auth.model.AysTokenBuilder;
import com.ays.auth.model.dto.request.AysLoginRequest;
import com.ays.auth.model.dto.request.AysLoginRequestBuilder;
import com.ays.auth.model.dto.request.AysTokenRefreshRequest;
import com.ays.auth.model.dto.request.AysTokenRefreshRequestBuilder;
import com.ays.auth.model.dto.response.AysTokenResponse;
import com.ays.auth.model.mapper.AysTokenToAysTokenResponseMapper;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.model.dto.response.AysResponseBuilder;
import com.ays.util.AysMockMvcRequestBuilders;
import com.ays.util.AysTestData;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminUserAuthControllerTest extends AbstractRestControllerTest {

    @MockBean
    private AdminUserAuthService adminUserAuthService;

    private final AysTokenToAysTokenResponseMapper aysTokenToAysTokenResponseMapper = AysTokenToAysTokenResponseMapper.initialize();

    private static final String BASE_PATH = "/api/v1/authentication/admin";

    @Test
    void givenValidAdminUserRegisterRequest_whenAdminUserRegistered_thenReturnSuccessResponse() throws Exception {
        // Given
        AdminUserRegisterRequest mockRequest = new AdminUserRegisterRequestBuilder()
                .withEmail(AysTestData.VALID_EMAIL).build();

        // When
        Mockito.doNothing().when(adminUserAuthService).register(Mockito.any());

        // Then
        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(BASE_PATH.concat("/register"), mockRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.time").isNotEmpty())
                .andExpect(jsonPath("$.httpStatus").value(mockResponse.getHttpStatus().getReasonPhrase()))
                .andExpect(jsonPath("$.isSuccess").value(mockResponse.getIsSuccess()))
                .andExpect(jsonPath("$.response").doesNotExist());

        Mockito.verify(adminUserAuthService, Mockito.times(1)).register(Mockito.any());
    }

    @Test
    void givenValidLoginRequest_whenTokensGeneratedSuccessfully_thenReturnTokenResponse() throws Exception {
        // Given
        AysLoginRequest mockRequest = new AysLoginRequestBuilder().build();

        // When
        AysToken mockAysToken = AysTokenBuilder.VALID_FOR_ADMIN;
        Mockito.when(adminUserAuthService.authenticate(Mockito.any())).thenReturn(mockAysToken);

        // Then
        AysTokenResponse mockResponse = aysTokenToAysTokenResponseMapper.map(mockAysToken);
        AysResponse<AysTokenResponse> mockAysResponse = AysResponseBuilder.successOf(mockResponse);
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(BASE_PATH.concat("/token"), mockRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.time").isNotEmpty())
                .andExpect(jsonPath("$.httpStatus").value(mockAysResponse.getHttpStatus().getReasonPhrase()))
                .andExpect(jsonPath("$.isSuccess").value(mockAysResponse.getIsSuccess()))
                .andExpect(jsonPath("$.response").isNotEmpty())
                .andExpect(jsonPath("$.response.accessToken").value(mockAysResponse.getResponse().getAccessToken()))
                .andExpect(jsonPath("$.response.accessTokenExpiresAt").value(mockAysResponse.getResponse().getAccessTokenExpiresAt()))
                .andExpect(jsonPath("$.response.refreshToken").value(mockAysResponse.getResponse().getRefreshToken()));

        Mockito.verify(adminUserAuthService, Mockito.times(1)).authenticate(Mockito.any());
    }

    @Test
    void givenValidTokenRefreshRequest_whenAccessTokenGeneratedSuccessfully_thenReturnTokenResponse() throws Exception {
        // Given
        AysTokenRefreshRequest mockRequest = AysTokenRefreshRequestBuilder.VALID_FOR_ADMIN;

        // When
        AysToken mockAysToken = AysTokenBuilder.VALID_FOR_ADMIN;
        Mockito.when(adminUserAuthService.refreshAccessToken(Mockito.any())).thenReturn(mockAysToken);

        // Then
        AysTokenResponse mockResponse = aysTokenToAysTokenResponseMapper.map(mockAysToken);
        AysResponse<AysTokenResponse> mockAysResponse = AysResponseBuilder.successOf(mockResponse);
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(BASE_PATH.concat("/token/refresh"), mockRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.time").isNotEmpty())
                .andExpect(jsonPath("$.httpStatus").value(mockAysResponse.getHttpStatus().getReasonPhrase()))
                .andExpect(jsonPath("$.isSuccess").value(mockAysResponse.getIsSuccess()))
                .andExpect(jsonPath("$.response").isNotEmpty())
                .andExpect(jsonPath("$.response.accessToken").value(mockAysResponse.getResponse().getAccessToken()))
                .andExpect(jsonPath("$.response.accessTokenExpiresAt").value(mockAysResponse.getResponse().getAccessTokenExpiresAt()))
                .andExpect(jsonPath("$.response.refreshToken").value(mockAysResponse.getResponse().getRefreshToken()));

        Mockito.verify(adminUserAuthService, Mockito.times(1)).refreshAccessToken(Mockito.any());

    }

}
