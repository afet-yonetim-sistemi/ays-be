package org.ays.auth.controller;

import org.ays.AysEndToEndTest;
import org.ays.auth.model.enums.AysSourcePage;
import org.ays.auth.model.request.AysForgotPasswordRequest;
import org.ays.auth.model.request.AysForgotPasswordRequestBuilder;
import org.ays.auth.model.request.AysLoginRequest;
import org.ays.auth.model.request.AysLoginRequestBuilder;
import org.ays.auth.model.request.AysTokenInvalidateRequest;
import org.ays.auth.model.request.AysTokenRefreshRequest;
import org.ays.auth.model.response.AysTokenResponse;
import org.ays.auth.model.response.AysTokenResponseBuilder;
import org.ays.common.model.response.AysResponse;
import org.ays.common.model.response.AysResponseBuilder;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.ays.util.AysValidTestData;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class AysAuthEndToEndTest extends AysEndToEndTest {

    private static final String BASE_PATH = "/api/v1/authentication";


    @Test
    void givenValidUserLoginRequest_whenTokensGeneratedSuccessfully_thenReturnTokenResponse() throws Exception {
        // Given
        AysLoginRequest loginRequest = new AysLoginRequestBuilder()
                .withEmailAddress(AysValidTestData.SuperAdmin.EMAIL_ADDRESS)
                .withPassword(AysValidTestData.PASSWORD)
                .withSourcePage(AysSourcePage.INSTITUTION)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/token");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, loginRequest);

        AysResponse<AysTokenResponse> mockResponse = AysResponseBuilder
                .successOf(new AysTokenResponseBuilder().build());

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andDo(MockMvcResultHandlers.print())
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
                .refreshToken(userToken.getRefreshToken())
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
                .refreshToken(userToken.getRefreshToken())
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/token/invalidate");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, userToken.getAccessToken(), tokenInvalidateRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }


    @Test
    void givenValidForgotPasswordRequest_whenSendPasswordCreateMail_thenReturnSuccessResponse() throws Exception {
        // Given
        AysForgotPasswordRequest mockForgotPasswordRequest = new AysForgotPasswordRequestBuilder()
                .withEmailAddress(AysValidTestData.User.EMAIL_ADDRESS)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/password/forgot");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockForgotPasswordRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }


    @Test
    void givenValidId_whenCheckPasswordIdSuccessfully_thenReturnSuccessResponse() throws Exception {
        // Given
        String mockId = "40fb7a46-40bd-46cb-b44f-1f47162133b1";

        // Then
        String endpoint = BASE_PATH.concat("/password/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint);

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

}
