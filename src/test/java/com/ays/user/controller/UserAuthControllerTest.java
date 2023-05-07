package com.ays.user.controller;

import com.ays.AbstractRestControllerTest;
import com.ays.auth.model.dto.request.AysLoginRequest;
import com.ays.auth.model.dto.request.AysTokenRefreshRequest;
import com.ays.auth.model.dto.request.AysTokenRefreshRequestBuilder;
import com.ays.auth.model.dto.response.AysTokenResponse;
import com.ays.auth.model.mapper.AysTokenToAysTokenResponseMapper;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.model.dto.response.AysResponseBuilder;
import com.ays.user.model.dto.request.AysUserLoginRequestBuilder;
import com.ays.user.service.UserAuthService;
import com.ays.util.AysMockMvcRequestBuilders;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserAuthControllerTest extends AbstractRestControllerTest {

    @MockBean
    private UserAuthService userAuthService;


    private final AysTokenToAysTokenResponseMapper AYS_TOKEN_TO_AYS_TOKEN_RESPONSE_MAPPER = AysTokenToAysTokenResponseMapper.initialize();

    private static final String BASE_PATH = "/api/v1/authentication";

    @Test
    void givenValidUserLoginRequest_whenTokensGeneratedSuccessfully_thenReturnTokenResponse() throws Exception {
        // Given
        AysLoginRequest mockRequest = new AysUserLoginRequestBuilder().build();

        // when
        Mockito.when(userAuthService.authenticate(Mockito.any())).thenReturn(mockUserToken);

        // then
        AysTokenResponse mockResponse = AYS_TOKEN_TO_AYS_TOKEN_RESPONSE_MAPPER.map(mockUserToken);
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

        Mockito.verify(userAuthService, Mockito.times(1)).authenticate(Mockito.any());
    }

    @Test
    void givenValidTokenRefreshRequest_whenAccessTokenGeneratedSuccessfully_thenReturnTokenResponse() throws Exception {
        // Given
        AysTokenRefreshRequest mockRequest = AysTokenRefreshRequestBuilder.VALID_FOR_USER;

        // When
        Mockito.when(userAuthService.refreshAccessToken(Mockito.any())).thenReturn(mockUserToken);

        // Then
        AysTokenResponse mockResponse = AYS_TOKEN_TO_AYS_TOKEN_RESPONSE_MAPPER.map(mockUserToken);
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

        Mockito.verify(userAuthService, Mockito.times(1)).refreshAccessToken(Mockito.any());

    }
}