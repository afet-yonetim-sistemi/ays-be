package org.ays.user.controller;

import org.ays.AbstractRestControllerTest;
import org.ays.auth.model.dto.request.AysLoginRequestV2;
import org.ays.auth.model.dto.request.AysLoginRequestV2Builder;
import org.ays.auth.model.dto.request.AysTokenInvalidateRequest;
import org.ays.auth.model.dto.request.AysTokenInvalidateRequestBuilder;
import org.ays.auth.model.dto.request.AysTokenRefreshRequest;
import org.ays.auth.model.dto.request.AysTokenRefreshRequestBuilder;
import org.ays.auth.model.dto.response.AysTokenResponse;
import org.ays.auth.model.mapper.AysTokenToAysTokenResponseMapper;
import org.ays.common.model.dto.response.AysResponse;
import org.ays.common.model.dto.response.AysResponseBuilder;
import org.ays.common.util.exception.model.AysErrorBuilder;
import org.ays.common.util.exception.model.AysErrorResponse;
import org.ays.user.model.enums.SourcePage;
import org.ays.user.service.UserAuthServiceV2;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.ays.util.AysValidTestData;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class UserAuthControllerV2Test extends AbstractRestControllerTest {

    @MockBean
    private UserAuthServiceV2 userAuthService;


    private final AysTokenToAysTokenResponseMapper aysTokenToAysTokenResponseMapper = AysTokenToAysTokenResponseMapper.initialize();


    private static final String BASE_PATH = "/api/v2/authentication";

    @Test
    void givenValidUserLoginRequest_whenTokensGeneratedSuccessfully_thenReturnTokenResponse() throws Exception {
        // Given
        AysLoginRequestV2 mockRequest = new AysLoginRequestV2Builder()
                .withEmailAddress(AysValidTestData.EMAIL)
                .withPassword(AysValidTestData.PASSWORD)
                .withSourcePage(SourcePage.INSTITUTION)
                .build();

        // When
        Mockito.when(userAuthService.authenticate(Mockito.any()))
                .thenReturn(mockUserToken);

        // Then
        String endpoint = BASE_PATH.concat("/token");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockRequest);

        AysTokenResponse mockTokenResponse = aysTokenToAysTokenResponseMapper.map(mockUserToken);
        AysResponse<AysTokenResponse> mockResponse = AysResponseBuilder.successOf(mockTokenResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.accessToken")
                        .value(mockResponse.getResponse().getAccessToken()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.accessTokenExpiresAt")
                        .value(mockResponse.getResponse().getAccessTokenExpiresAt()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.refreshToken")
                        .value(mockResponse.getResponse().getRefreshToken()));

        // Verify
        Mockito.verify(userAuthService, Mockito.times(1))
                .authenticate(Mockito.any());
    }

    @Test
    void givenValidTokenRefreshRequest_whenAccessTokenGeneratedSuccessfully_thenReturnTokenResponse() throws Exception {
        // Given
        AysTokenRefreshRequest mockTokenRefreshRequest = AysTokenRefreshRequestBuilder.VALID_FOR_USER;

        // When
        Mockito.when(userAuthService.refreshAccessToken(Mockito.anyString()))
                .thenReturn(mockUserToken);

        // Then
        String endpoint = BASE_PATH.concat("/token/refresh");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockTokenRefreshRequest);

        AysTokenResponse mockTokenResponse = aysTokenToAysTokenResponseMapper.map(mockUserToken);
        AysResponse<AysTokenResponse> mockResponse = AysResponseBuilder.successOf(mockTokenResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.accessToken")
                        .value(mockResponse.getResponse().getAccessToken()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.accessTokenExpiresAt")
                        .value(mockResponse.getResponse().getAccessTokenExpiresAt()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.refreshToken")
                        .value(mockResponse.getResponse().getRefreshToken()));

        // Verify
        Mockito.verify(userAuthService, Mockito.times(1))
                .refreshAccessToken(Mockito.anyString());
    }

    @Test
    void givenValidAysTokenInvalidateRequest_whenTokensInvalidated_thenReturnSuccessResponse() throws Exception {
        // Given
        AysTokenInvalidateRequest mockTokenInvalidateRequest = AysTokenInvalidateRequestBuilder.VALID_FOR_USER;

        // When
        Mockito.doNothing()
                .when(userAuthService)
                .invalidateTokens(Mockito.any());

        // Then
        String endpoint = BASE_PATH.concat("/token/invalidate");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken(), mockTokenInvalidateRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(userAuthService, Mockito.times(1))
                .invalidateTokens(Mockito.any());
    }

    @Test
    void givenValidAysTokenInvalidateRequest_whenUserUnauthorizedForTokensInvalidating_thenReturnAccessDeniedException() throws Exception {
        // Given
        AysTokenInvalidateRequest mockRequest = AysTokenInvalidateRequestBuilder.VALID_FOR_USER;

        // Then
        String endpoint = BASE_PATH.concat("/token/invalidate");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminUserToken.getAccessToken(), mockRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(userAuthService, Mockito.never())
                .invalidateTokens(Mockito.any());
    }

}
