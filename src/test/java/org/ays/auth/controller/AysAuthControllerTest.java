package org.ays.auth.controller;

import org.ays.AysRestControllerTest;
import org.ays.auth.model.enums.AysSourcePage;
import org.ays.auth.model.mapper.AysTokenToResponseMapper;
import org.ays.auth.model.request.AysForgotPasswordRequestBuilder;
import org.ays.auth.model.request.AysLoginRequest;
import org.ays.auth.model.request.AysLoginRequestBuilder;
import org.ays.auth.model.request.AysPasswordCreateRequest;
import org.ays.auth.model.request.AysPasswordCreateRequestBuilder;
import org.ays.auth.model.request.AysPasswordForgotRequest;
import org.ays.auth.model.request.AysTokenInvalidateRequest;
import org.ays.auth.model.request.AysTokenInvalidateRequestBuilder;
import org.ays.auth.model.request.AysTokenRefreshRequest;
import org.ays.auth.model.request.AysTokenRefreshRequestBuilder;
import org.ays.auth.model.response.AysTokenResponse;
import org.ays.auth.service.AysAuthService;
import org.ays.auth.service.AysUserPasswordService;
import org.ays.common.model.response.AysErrorResponse;
import org.ays.common.model.response.AysErrorResponseBuilder;
import org.ays.common.model.response.AysResponse;
import org.ays.common.model.response.AysResponseBuilder;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.ays.util.AysValidTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class AysAuthControllerTest extends AysRestControllerTest {


    @MockBean
    private AysAuthService authService;

    @MockBean
    private AysUserPasswordService userPasswordService;


    private final AysTokenToResponseMapper tokenToResponseMapper = AysTokenToResponseMapper.initialize();


    private static final String BASE_PATH = "/api/v1/authentication";


    @ParameterizedTest
    @ValueSource(strings = {
            "abcdef@mail.com",
            "abc+def@archive.com",
            "john.doe123@example.co.uk",
            "admin_123@example.org",
            "admin-test@ays.com",
    })
    void givenValidLoginRequestWithValidEmailAddress_whenTokensGeneratedSuccessfully_thenReturnTokenResponse(String mockEmailAddress) throws Exception {
        // Given
        AysLoginRequest mockLoginRequest = new AysLoginRequestBuilder()
                .withEmailAddress(mockEmailAddress)
                .withPassword(AysValidTestData.PASSWORD)
                .withSourcePage(AysSourcePage.INSTITUTION)
                .build();

        // When
        Mockito.when(authService.authenticate(Mockito.any()))
                .thenReturn(mockUserToken);

        // Then
        String endpoint = BASE_PATH.concat("/token");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockLoginRequest);

        AysTokenResponse mockTokenResponse = tokenToResponseMapper.map(mockUserToken);
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
        Mockito.verify(authService, Mockito.times(1))
                .authenticate(Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "g",
            "gfh2j"
    })
    void givenInvalidLoginRequestWithInvalidPassword_whenPasswordDoesNotValid_thenReturnValidationError(String mockPassword) throws Exception {
        // Given
        AysLoginRequest mockLoginRequest = new AysLoginRequestBuilder()
                .withEmailAddress(AysValidTestData.User.EMAIL_ADDRESS)
                .withPassword(mockPassword)
                .withSourcePage(AysSourcePage.INSTITUTION)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/token");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockLoginRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(authService, Mockito.never())
                .authenticate(Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "abc.def@mail.c",
            "abc.def@mail#archive.com",
            "abc.def@mail",
            "abcdef@mail..com",
            "abc-@mail.com",
            "admin@test@ays.com",
            "admintest@ays..com",
            "username@gmail..co.uk",
            "user@ example.com",
            "user@-example.com",
            "user@example-.com",
            "(user)@example.com",
            "user@[192.168.1.1",
            "user@exam ple.com",
            "user@.com",
            ".user@example.com",
            "  user@example.com",
            "user@example.com ",
            " user@example.com "
    })
    void givenInvalidLoginRequestWithInvalidEmailAddress_whenEmailsAreNotValid_thenReturnValidationError(String mockEmailAddress) throws Exception {
        // Given
        AysLoginRequest mockLoginRequest = new AysLoginRequestBuilder()
                .withEmailAddress(mockEmailAddress)
                .withPassword(AysValidTestData.PASSWORD)
                .withSourcePage(AysSourcePage.INSTITUTION)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/token");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockLoginRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(authService, Mockito.never())
                .authenticate(Mockito.any());
    }

    @Test
    void givenValidTokenRefreshRequest_whenAccessTokenGeneratedSuccessfully_thenReturnTokenResponse() throws Exception {
        // Given
        AysTokenRefreshRequest mockTokenRefreshRequest = AysTokenRefreshRequestBuilder.VALID_FOR_USER;

        // When
        Mockito.when(authService.refreshAccessToken(Mockito.anyString()))
                .thenReturn(mockUserToken);

        // Then
        String endpoint = BASE_PATH.concat("/token/refresh");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockTokenRefreshRequest);

        AysTokenResponse mockTokenResponse = tokenToResponseMapper.map(mockUserToken);
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
        Mockito.verify(authService, Mockito.times(1))
                .refreshAccessToken(Mockito.anyString());
    }

    @Test
    void givenValidAysTokenInvalidateRequest_whenTokensInvalidated_thenReturnSuccessResponse() throws Exception {
        // Given
        AysTokenInvalidateRequest mockTokenInvalidateRequest = AysTokenInvalidateRequestBuilder.VALID_FOR_USER;

        // When
        Mockito.doNothing()
                .when(authService)
                .invalidateTokens(Mockito.any());

        // Then
        String endpoint = BASE_PATH.concat("/token/invalidate");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken(), mockTokenInvalidateRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(authService, Mockito.times(1))
                .invalidateTokens(Mockito.any());
    }


    @Test
    void givenValidForgotPasswordRequest_whenSendPasswordCreateMail_thenReturnSuccessResponse() throws Exception {
        // Given
        AysPasswordForgotRequest mockForgotPasswordRequest = new AysForgotPasswordRequestBuilder()
                .withEmailAddress(AysValidTestData.SuperAdmin.EMAIL_ADDRESS)
                .build();

        // When
        Mockito.doNothing()
                .when(userPasswordService)
                .forgotPassword(Mockito.any(AysPasswordForgotRequest.class));

        // Then
        String endpoint = BASE_PATH.concat("/password/forgot");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockForgotPasswordRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(userPasswordService, Mockito.times(1))
                .forgotPassword(Mockito.any(AysPasswordForgotRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "abc.def@mail.c",
            "abc.def@mail#archive.com",
            "abc.def@mail",
            "abcdef@mail..com",
            "abc-@mail.com",
            "admin@test@ays.com",
            "admintest@ays..com",
            "username@gmail..co.uk",
            "user@ example.com",
            "user@-example.com",
            "user@example-.com",
            "(user)@example.com",
            "user@[192.168.1.1",
            "user@exam ple.com",
            "user@.com",
            ".user@example.com",
            "  user@example.com",
            "user@example.com ",
            " user@example.com "
    })
    void givenForgotPasswordRequestWithInvalidEmailAddress_whenEmailDoesNotValid_thenReturnValidationError(String mockEmailAddress) throws Exception {

        // Given
        AysPasswordForgotRequest mockForgotPasswordRequest = new AysForgotPasswordRequestBuilder()
                .withEmailAddress(mockEmailAddress)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/password/forgot");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockForgotPasswordRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userPasswordService, Mockito.never())
                .forgotPassword(Mockito.any(AysPasswordForgotRequest.class));
    }

    @Test
    void givenForgotPasswordRequestWithoutEmailAddress_whenEmailIsNull_thenReturnValidationError() throws Exception {

        // Given
        AysPasswordForgotRequest mockForgotPasswordRequest = new AysForgotPasswordRequestBuilder()
                .withEmailAddress(null)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/password/forgot");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockForgotPasswordRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userPasswordService, Mockito.never())
                .forgotPassword(Mockito.any(AysPasswordForgotRequest.class));
    }


    @Test
    void givenValidId_whenCheckPasswordIdSuccessfully_thenReturnSuccessResponse() throws Exception {
        // Given
        String mockId = "40fb7a46-40bd-46cb-b44f-1f47162133b1";

        // When
        Mockito.doNothing()
                .when(userPasswordService)
                .checkPasswordChangingValidity(Mockito.anyString());

        // Then
        String endpoint = BASE_PATH.concat("/password/").concat(mockId).concat("/validity");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint);

        AysResponse<Void> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(userPasswordService, Mockito.times(1))
                .checkPasswordChangingValidity(Mockito.anyString());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "A",
            "493268349068342"
    })
    void givenId_whenIdDoesNotValid_thenReturnValidationError(String invalidId) throws Exception {

        // Then
        String endpoint = BASE_PATH.concat("/password/").concat(invalidId).concat("/validity");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userPasswordService, Mockito.never())
                .checkPasswordChangingValidity(Mockito.anyString());
    }


    @Test
    void givenValidPasswordCreateRequest_whenPasswordCreated_thenReturnSuccessResponse() throws Exception {
        // Given
        String mockId = "1fa43c75-7a7a-4041-8cef-03be8429dd30";
        AysPasswordCreateRequest mockPasswordCreateRequest = new AysPasswordCreateRequestBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.doNothing()
                .when(userPasswordService)
                .createPassword(Mockito.anyString(), Mockito.any(AysPasswordCreateRequest.class));

        // Then
        String endpoint = BASE_PATH.concat("/password/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockPasswordCreateRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(userPasswordService, Mockito.times(1))
                .createPassword(Mockito.anyString(), Mockito.any(AysPasswordCreateRequest.class));
    }

    @Test
    void givenPasswordCreateRequest_whenPasswordsNotEqual_thenReturnValidationError() throws Exception {

        // Given
        String mockId = "1fa43c75-7a7a-4041-8cef-03be8429dd30";
        AysPasswordCreateRequest mockPasswordCreateRequest = new AysPasswordCreateRequestBuilder()
                .withPassword("password")
                .withPasswordRepeat("password1")
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/password/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockPasswordCreateRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userPasswordService, Mockito.never())
                .createPassword(Mockito.anyString(), Mockito.any(AysPasswordCreateRequest.class));
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "g",
            "gfh2j"
    })
    void givenPasswordCreateRequest_whenPasswordDoesNotValid_thenReturnValidationError(String mockPassword) throws Exception {

        // Given
        String mockId = "1fa43c75-7a7a-4041-8cef-03be8429dd30";
        AysPasswordCreateRequest mockPasswordCreateRequest = new AysPasswordCreateRequestBuilder()
                .withPassword(mockPassword)
                .withPasswordRepeat(mockPassword)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/password/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockPasswordCreateRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userPasswordService, Mockito.never())
                .createPassword(Mockito.anyString(), Mockito.any(AysPasswordCreateRequest.class));
    }

}
