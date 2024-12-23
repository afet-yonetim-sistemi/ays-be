package org.ays.auth.controller;

import org.ays.AysEndToEndTest;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.AysUserBuilder;
import org.ays.auth.model.enums.AysSourcePage;
import org.ays.auth.model.request.AysForgotPasswordRequestBuilder;
import org.ays.auth.model.request.AysLoginRequest;
import org.ays.auth.model.request.AysLoginRequestBuilder;
import org.ays.auth.model.request.AysPasswordCreateRequest;
import org.ays.auth.model.request.AysPasswordCreateRequestBuilder;
import org.ays.auth.model.request.AysPasswordForgotRequest;
import org.ays.auth.model.request.AysTokenInvalidateRequest;
import org.ays.auth.model.request.AysTokenRefreshRequest;
import org.ays.auth.model.response.AysTokenResponse;
import org.ays.auth.model.response.AysTokenResponseBuilder;
import org.ays.auth.port.AysRoleReadPort;
import org.ays.auth.port.AysUserReadPort;
import org.ays.auth.port.AysUserSavePort;
import org.ays.common.model.response.AysResponse;
import org.ays.common.model.response.AysResponseBuilder;
import org.ays.institution.model.Institution;
import org.ays.institution.model.InstitutionBuilder;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.ays.util.AysValidTestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

class AysAuthEndToEndTest extends AysEndToEndTest {

    @Autowired
    private AysUserSavePort userSavePort;

    @Autowired
    private AysUserReadPort userReadPort;

    @Autowired
    private AysRoleReadPort roleReadPort;


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
                        .doesNotHaveJsonPath())
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
                        .doesNotHaveJsonPath())
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

        AysResponse<Void> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }


    @Test
    void givenValidForgotPasswordRequest_whenUserHasNotPasswordAndPasswordCreatedWithLatestValueAndPasswordCreateMailSent_thenReturnSuccessResponse() throws Exception {

        // Initialize
        Institution institution = new InstitutionBuilder()
                .withId(AysValidTestData.Admin.INSTITUTION_ID)
                .build();

        List<AysRole> roles = roleReadPort.findAllActivesByInstitutionId(institution.getId());

        AysUser user = userSavePort.save(
                new AysUserBuilder()
                        .withValidValues()
                        .withoutId()
                        .withRoles(roles)
                        .withInstitution(institution)
                        .build()
        );

        // Given
        AysPasswordForgotRequest mockForgotPasswordRequest = new AysForgotPasswordRequestBuilder()
                .withEmailAddress(user.getEmailAddress())
                .build();

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
        AysUser userFromDatabase = userReadPort.findById(user.getId())
                .orElseThrow();

        AysUser.Password passwordFromDatabase = userFromDatabase.getPassword();

        Assertions.assertNotNull(passwordFromDatabase);
        Assertions.assertNotNull(passwordFromDatabase.getValue());
        Assertions.assertNotNull(passwordFromDatabase.getForgotAt());
        Assertions.assertTrue(passwordFromDatabase.getForgotAt().isAfter(LocalDateTime.now().minusMinutes(1)));
        Assertions.assertNotNull(passwordFromDatabase.getCreatedUser());
        Assertions.assertNotNull(passwordFromDatabase.getCreatedAt());
        Assertions.assertNull(passwordFromDatabase.getUpdatedUser());
        Assertions.assertNull(passwordFromDatabase.getUpdatedAt());
        Assertions.assertEquals("AYS", passwordFromDatabase.getCreatedUser());
    }

    @Test
    void givenValidForgotPasswordRequest_whenUserHasPasswordAndPasswordCreatedWithLatestValueAndPasswordCreateMailSent_thenReturnSuccessResponse() throws Exception {

        // Initialize
        Institution institution = new InstitutionBuilder()
                .withId(AysValidTestData.Admin.INSTITUTION_ID)
                .build();

        List<AysRole> roles = roleReadPort.findAllActivesByInstitutionId(institution.getId());

        AysUser.Password password = new AysUserBuilder.PasswordBuilder()
                .withValidValues()
                .withoutId()
                .build();

        AysUser user = userSavePort.save(
                new AysUserBuilder()
                        .withValidValues()
                        .withoutId()
                        .withRoles(roles)
                        .withInstitution(institution)
                        .withPassword(password)
                        .build()
        );

        // Given
        AysPasswordForgotRequest mockForgotPasswordRequest = new AysForgotPasswordRequestBuilder()
                .withEmailAddress(user.getEmailAddress())
                .build();

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
        AysUser userFromDatabase = userReadPort.findById(user.getId())
                .orElseThrow();

        AysUser.Password passwordFromDatabase = userFromDatabase.getPassword();

        Assertions.assertNotNull(passwordFromDatabase);
        Assertions.assertNotNull(passwordFromDatabase.getValue());
        Assertions.assertEquals(password.getValue(), passwordFromDatabase.getValue());
        Assertions.assertNotNull(passwordFromDatabase.getForgotAt());
        Assertions.assertTrue(passwordFromDatabase.getForgotAt().isAfter(LocalDateTime.now().minusMinutes(1)));
        Assertions.assertNotNull(passwordFromDatabase.getCreatedUser());
        Assertions.assertNotNull(passwordFromDatabase.getCreatedAt());
        Assertions.assertNull(passwordFromDatabase.getUpdatedUser());
        Assertions.assertNull(passwordFromDatabase.getUpdatedAt());
        Assertions.assertEquals("AYS", passwordFromDatabase.getCreatedUser());
    }


    @Test
    void givenValidId_whenCheckPasswordIdSuccessfully_thenReturnSuccessResponse() throws Exception {

        // Initialize
        Institution institution = new InstitutionBuilder()
                .withId(AysValidTestData.Admin.INSTITUTION_ID)
                .build();

        AysRole role = roleReadPort.findAllActivesByInstitutionId(institution.getId())
                .stream()
                .findFirst()
                .orElseThrow();

        AysUser.Password password = new AysUserBuilder.PasswordBuilder()
                .withoutId()
                .withForgotAt(LocalDateTime.now().minusMinutes(15))
                .build();

        AysUser user = userSavePort.save(
                new AysUserBuilder()
                        .withValidValues()
                        .withoutId()
                        .withRoles(List.of(role))
                        .withInstitution(institution)
                        .withPassword(password)
                        .build()
        );

        // Given
        String mockId = user.getPassword().getId();

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
    }


    @Test
    void givenValidPasswordCreateRequest_whenPasswordCreated_thenReturnSuccessResponse() throws Exception {

        // Initialize
        Institution institution = new InstitutionBuilder()
                .withId(AysValidTestData.Admin.INSTITUTION_ID)
                .build();

        AysRole role = roleReadPort.findAllActivesByInstitutionId(institution.getId())
                .stream()
                .findFirst()
                .orElseThrow();

        AysUser.Password password = new AysUserBuilder.PasswordBuilder()
                .withoutId()
                .withForgotAt(LocalDateTime.now().minusMinutes(15))
                .build();

        AysUser user = userSavePort.save(
                new AysUserBuilder()
                        .withValidValues()
                        .withoutId()
                        .withRoles(List.of(role))
                        .withInstitution(institution)
                        .withPassword(password)
                        .build()
        );

        // Given
        String mockId = user.getPassword().getId();

        AysPasswordCreateRequest mockPasswordCreateRequest = new AysPasswordCreateRequestBuilder()
                .withValidValues()
                .build();

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
        AysUser userFromDatabase = userReadPort.findById(user.getId())
                .orElseThrow();

        AysUser.Password passwordFromDatabase = userFromDatabase.getPassword();

        Assertions.assertNotNull(passwordFromDatabase);
        Assertions.assertNotEquals(mockId, passwordFromDatabase.getId());
        Assertions.assertNotNull(passwordFromDatabase.getValue());
        Assertions.assertNull(passwordFromDatabase.getForgotAt());
        Assertions.assertNotNull(passwordFromDatabase.getCreatedUser());
        Assertions.assertNotNull(passwordFromDatabase.getCreatedAt());
        Assertions.assertNull(passwordFromDatabase.getUpdatedUser());
        Assertions.assertNull(passwordFromDatabase.getUpdatedAt());
        Assertions.assertEquals("AYS", passwordFromDatabase.getCreatedUser());
    }

}
