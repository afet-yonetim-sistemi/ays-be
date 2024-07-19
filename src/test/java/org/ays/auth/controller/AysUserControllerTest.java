package org.ays.auth.controller;


import org.ays.AysRestControllerTest;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.AysUserBuilder;
import org.ays.auth.model.mapper.AysUserToResponseMapper;
import org.ays.auth.model.mapper.AysUserToUsersResponseMapper;
import org.ays.auth.model.request.AysUserListRequest;
import org.ays.auth.model.request.AysUserListRequestBuilder;
import org.ays.auth.model.request.AysUserUpdateRequest;
import org.ays.auth.model.request.AysPhoneNumberRequestBuilder;
import org.ays.auth.model.request.AysUserUpdateRequestBuilder;
import org.ays.auth.model.response.AysUserResponse;
import org.ays.auth.model.response.AysUsersResponse;
import org.ays.auth.service.AysUserReadService;
import org.ays.auth.service.AysUserUpdateService;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageBuilder;
import org.ays.common.model.response.AysErrorResponse;
import org.ays.common.model.response.AysPageResponse;
import org.ays.common.model.response.AysResponse;
import org.ays.common.model.response.AysResponseBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.common.util.exception.model.AysErrorBuilder;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.Set;

class AysUserControllerTest extends AysRestControllerTest {

    @MockBean
    private AysUserUpdateService userUpdateService;

    @MockBean
    private AysUserReadService userReadService;


    private final AysUserToUsersResponseMapper userToUsersResponseMapper = AysUserToUsersResponseMapper.initialize();
    private final AysUserToResponseMapper userToResponseMapper = AysUserToResponseMapper.initialize();


    private static final String BASE_PATH = "/api/v1";


    @Test
    void givenValidUserListRequest_whenUsersFound_thenReturnAysPageResponseOfUsersResponse() throws Exception {
        // Given
        AysUserListRequest mockListRequest = new AysUserListRequestBuilder()
                .withValidValues()
                .build();

        //When
        List<AysUser> mockUsers = List.of(
                new AysUserBuilder().withValidValues().build()
        );

        AysPage<AysUser> mockUserPage = AysPageBuilder
                .from(mockUsers, mockListRequest.getPageable());

        Mockito.when(userReadService.findAll(Mockito.any(AysUserListRequest.class)))
                .thenReturn(mockUserPage);

        // Then
        String endpoint = BASE_PATH.concat("/users");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken(), mockListRequest);

        List<AysUsersResponse> mockUsersResponse = userToUsersResponseMapper.map(mockUsers);
        AysPageResponse<AysUsersResponse> pageOfResponse = AysPageResponse.<AysUsersResponse>builder()
                .of(mockUserPage)
                .content(mockUsersResponse)
                .build();
        AysResponse<AysPageResponse<AysUsersResponse>> mockResponse = AysResponse
                .successOf(pageOfResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userReadService, Mockito.times(1))
                .findAll(Mockito.any(AysUserListRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Test user 1234",
            "User *^%$#",
            " Test",
            "? User",
            "J"
    })
    void givenUserListRequest_whenFirstNameDoesNotValid_thenReturnValidationError(String invalidName) throws Exception {

        // Given
        AysUserListRequest mockListRequest = new AysUserListRequestBuilder()
                .withValidValues()
                .withFirstName(invalidName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/users");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken(), mockListRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userReadService, Mockito.never())
                .findAll(Mockito.any(AysUserListRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Test user 1234",
            "User *^%$#",
            " Test",
            "? User",
            "J"
    })
    void givenUserListRequest_whenLastNameDoesNotValid_thenReturnValidationError(String invalidName) throws Exception {

        // Given
        AysUserListRequest mockListRequest = new AysUserListRequestBuilder()
                .withValidValues()
                .withLastName(invalidName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/users");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken(), mockListRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userReadService, Mockito.never())
                .findAll(Mockito.any(AysUserListRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "City user 1234",
            "City *^%$#",
            " Test",
            "? User",
            "J"
    })
    void givenUserListRequest_whenCityDoesNotValid_thenReturnValidationError(String invalidName) throws Exception {

        // Given
        AysUserListRequest mockListRequest = new AysUserListRequestBuilder()
                .withValidValues()
                .withCity(invalidName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/users");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken(), mockListRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userReadService, Mockito.never())
                .findAll(Mockito.any(AysUserListRequest.class));
    }

    @Test
    void givenValidUserListRequest_whenUserUnauthorized_thenReturnAccessDeniedException() throws Exception {
        // Given
        AysUserListRequest mockListRequest = new AysUserListRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/users");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken(), mockListRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(userReadService, Mockito.never())
                .findAll(Mockito.any(AysUserListRequest.class));
    }


    @Test
    void givenValidUserId_whenUserFound_thenReturnAysUserResponse() throws Exception {

        // Given
        String mockUserId = AysRandomUtil.generateUUID();

        // When
        AysUser mockUser = new AysUserBuilder()
                .withValidValues()
                .withId(mockUserId)
                .build();

        Mockito.when(userReadService.findById(mockUserId))
                .thenReturn(mockUser);

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(mockUserId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockAdminToken.getAccessToken());

        AysUserResponse mockUserResponse = userToResponseMapper
                .map(mockUser);
        AysResponse<AysUserResponse> mockResponse = AysResponse
                .successOf(mockUserResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userReadService, Mockito.times(1))
                .findById(mockUserId);
    }

    @Test
    void givenUserId_whenUnauthorizedForGettingUserById_thenReturnAccessDeniedException() throws Exception {

        // Given
        String mockUserId = AysRandomUtil.generateUUID();

        // Then
        String endpoint = BASE_PATH.concat("/user/".concat(mockUserId));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockUserToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(userReadService, Mockito.never())
                .findById(mockUserId);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "A",
            "493268349068342"
    })
    void givenInvalidId_whenIdNotValid_thenReturnValidationError(String invalidId) throws Exception {

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(invalidId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockAdminToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userReadService, Mockito.never())
                .findById(Mockito.anyString());
    }


    @Test
    void givenValidIdAndUserUpdateRequest_whenUserUpdated_thenReturnSuccess() throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        String mockEmailAddress = "test@email.com";
        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .withPhoneNumber(new AysPhoneNumberRequestBuilder().withValidValues().build())
                .withEmailAddress(mockEmailAddress)
                .build();

        // When
        Mockito.doNothing()
                .when(userUpdateService)
                .update(Mockito.any(), Mockito.any(AysUserUpdateRequest.class));

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminToken.getAccessToken(), mockUpdateRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(userUpdateService, Mockito.times(1))
                .update(Mockito.anyString(), Mockito.any(AysUserUpdateRequest.class));
    }

    @Test
    void givenValidIdAndUserUpdateRequest_whenUserUnauthorized_thenReturnAccessDeniedException() throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        String mockEmailAddress = "test@email.com";
        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .withPhoneNumber(new AysPhoneNumberRequestBuilder().withValidValues().build())
                .withEmailAddress(mockEmailAddress)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockUserToken.getAccessToken(), mockUpdateRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.FORBIDDEN;
        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(userUpdateService, Mockito.never())
                .update(Mockito.anyString(), Mockito.any(AysUserUpdateRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "A",
            "493268349068342"
    })
    void givenInvalidIdAndValidUserUpdateRequest_whenIdNotValid_thenReturnValidationError(String invalidId) throws Exception {

        // Given
        String mockEmailAddress = "test@email.com";
        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .withEmailAddress(mockEmailAddress)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(invalidId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminToken.getAccessToken(), mockUpdateRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userUpdateService, Mockito.never())
                .update(Mockito.anyString(), Mockito.any(AysUserUpdateRequest.class));
    }

    @Test
    void givenValidIdAndInvalidUserUpdateRequest_whenRoleIdsAreNull_thenReturnValidationError() throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        String mockEmailAddress = "test@email.com";
        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .withEmailAddress(mockEmailAddress)
                .withRoleIds(null)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminToken.getAccessToken(), mockUpdateRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userUpdateService, Mockito.never())
                .update(Mockito.anyString(), Mockito.any(AysUserUpdateRequest.class));
    }

    @Test
    void givenValidIdAndInvalidUserUpdateRequest_whenRoleIdsAreEmpty_thenReturnValidationError() throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        String mockEmailAddress = "test@email.com";
        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .withEmailAddress(mockEmailAddress)
                .withRoleIds(Set.of())
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminToken.getAccessToken(), mockUpdateRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userUpdateService, Mockito.never())
                .update(Mockito.anyString(), Mockito.any(AysUserUpdateRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "55aed4c4facb4b66bdb5-309eaaef4453"
    })
    void givenValidIdAndInvalidUserUpdateRequest_whenRoleIdIsNotValid_thenReturnValidationError(String roleId) throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        String mockEmailAddress = "test@email.com";
        AysUserUpdateRequest mockUpdateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .withEmailAddress(mockEmailAddress)
                .withRoleIds(Set.of(roleId))
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminToken.getAccessToken(), mockUpdateRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userUpdateService, Mockito.never())
                .update(Mockito.anyString(), Mockito.any(AysUserUpdateRequest.class));
    }

}
