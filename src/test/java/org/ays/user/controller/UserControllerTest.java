package org.ays.user.controller;

import org.ays.AbstractRestControllerTest;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysSorting;
import org.ays.common.model.dto.request.AysPhoneNumberFilterRequest;
import org.ays.common.model.dto.request.AysPhoneNumberFilterRequestBuilder;
import org.ays.common.model.dto.request.AysPhoneNumberRequest;
import org.ays.common.model.dto.request.AysPhoneNumberRequestBuilder;
import org.ays.common.model.dto.response.AysPageResponse;
import org.ays.common.model.dto.response.AysResponse;
import org.ays.common.model.dto.response.AysResponseBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.common.util.exception.model.AysError;
import org.ays.common.util.exception.model.AysErrorBuilder;
import org.ays.user.model.User;
import org.ays.user.model.UserBuilder;
import org.ays.user.model.dto.request.UserListRequest;
import org.ays.user.model.dto.request.UserListRequestBuilder;
import org.ays.user.model.dto.request.UserSaveRequest;
import org.ays.user.model.dto.request.UserSaveRequestBuilder;
import org.ays.user.model.dto.request.UserUpdateRequest;
import org.ays.user.model.dto.request.UserUpdateRequestBuilder;
import org.ays.user.model.dto.response.UserResponse;
import org.ays.user.model.dto.response.UserSavedResponse;
import org.ays.user.model.dto.response.UserSavedResponseBuilder;
import org.ays.user.model.dto.response.UsersResponse;
import org.ays.user.model.entity.UserEntity;
import org.ays.user.model.entity.UserEntityBuilder;
import org.ays.user.model.enums.UserRole;
import org.ays.user.model.enums.UserStatus;
import org.ays.user.model.mapper.UserEntityToUserMapper;
import org.ays.user.model.mapper.UserToUserResponseMapper;
import org.ays.user.model.mapper.UserToUsersResponseMapper;
import org.ays.user.service.UserSaveService;
import org.ays.user.service.UserService;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

class UserControllerTest extends AbstractRestControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private UserSaveService userSaveService;


    private final UserToUsersResponseMapper userToUsersResponseMapper = UserToUsersResponseMapper.initialize();
    private final UserToUserResponseMapper userToUserResponseMapper = UserToUserResponseMapper.initialize();
    private final UserEntityToUserMapper userEntityToUserMapper = UserEntityToUserMapper.initialize();


    private static final String BASE_PATH = "/api/v1";

    @Test
    void givenValidUserSaveRequest_whenUserSaved_thenReturnUserSavedResponse() throws Exception {
        // Given
        UserSaveRequest mockUserSaveRequest = new UserSaveRequestBuilder()
                .withPhoneNumber(new AysPhoneNumberRequestBuilder().withValidFields().build())
                .build();

        // When
        User mockUser = new UserBuilder()
                .withUsername("123456")
                .withPassword("987654")
                .withStatus(UserStatus.ACTIVE).build();
        Mockito.when(userSaveService.saveUser(Mockito.any(UserSaveRequest.class)))
                .thenReturn(mockUser);

        // Then
        String endpoint = BASE_PATH.concat("/user");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminUserToken.getAccessToken(), mockUserSaveRequest);

        UserSavedResponse mockUserSavedResponse = new UserSavedResponseBuilder()
                .withUsername(mockUser.getUsername())
                .withPassword(mockUser.getPassword())
                .build();
        AysResponse<UserSavedResponse> mockResponse = AysResponseBuilder.successOf(mockUserSavedResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.username")
                        .value(mockResponse.getResponse().getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.password")
                        .value(mockResponse.getResponse().getPassword()));

        // Verify
        Mockito.verify(userSaveService, Mockito.times(1))
                .saveUser(Mockito.any(UserSaveRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "John 1234",
            "John *^%$#",
            " John",
            "? John",
            "J"
    })
    void givenInvalidUserSaveRequestWithParametrizedInvalidNames_whenNamesAreNotValid_thenReturnValidationError(String invalidName) throws Exception {
        // Given
        UserSaveRequest mockRequest = new UserSaveRequestBuilder()
                .withValidFields()
                .withFirstName(invalidName)
                .withLastName(invalidName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminUserToken.getAccessToken(), mockRequest);

        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userSaveService, Mockito.never())
                .saveUser(Mockito.any(UserSaveRequest.class));
    }

    @Test
    void givenValidUserSaveRequest_whenUserUnauthorizedForSaving_thenReturnAccessDeniedException() throws Exception {
        // Given
        UserSaveRequest mockUserSaveRequest = new UserSaveRequestBuilder()
                .withPhoneNumber(new AysPhoneNumberRequestBuilder().withValidFields().build())
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken(), mockUserSaveRequest);

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());
    }

    @Test
    void givenPhoneNumberWithAlphanumericCharacter_whenPhoneNumberIsNotValid_thenReturnValidationError() throws Exception {
        // Given
        AysPhoneNumberRequest mockPhoneNumber = new AysPhoneNumberRequestBuilder()
                .withCountryCode("ABC")
                .withLineNumber("ABC").build();
        UserSaveRequest mockUserSaveRequest = new UserSaveRequestBuilder()
                .withValidFields()
                .withPhoneNumber(mockPhoneNumber).build();

        // Then
        String endpoint = BASE_PATH.concat("/user");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminUserToken.getAccessToken(), mockUserSaveRequest);

        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userSaveService, Mockito.never())
                .saveUser(Mockito.any(UserSaveRequest.class));
    }

    @Test
    void givenPhoneNumberWithInvalidLength_whenPhoneNumberIsNotValid_thenReturnValidationError() throws Exception {
        // Given
        AysPhoneNumberRequest mockPhoneNumber = new AysPhoneNumberRequestBuilder()
                .withCountryCode("456786745645")
                .withLineNumber("6546467456435548676845321346656654").build();
        UserSaveRequest mockUserSaveRequest = new UserSaveRequestBuilder()
                .withValidFields()
                .withPhoneNumber(mockPhoneNumber).build();

        // Then
        String endpoint = BASE_PATH.concat("/user");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminUserToken.getAccessToken(), mockUserSaveRequest);

        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userSaveService, Mockito.never())
                .saveUser(Mockito.any(UserSaveRequest.class));
    }

    @Test
    void givenPhoneNumberWithInvalidOperator_whenPhoneNumberIsNotValid_thenReturnValidationError() throws Exception {
        // Given
        final String invalidOperator = "123";
        AysPhoneNumberRequest mockPhoneNumber = new AysPhoneNumberRequestBuilder()
                .withCountryCode("90")
                .withLineNumber(invalidOperator + "6327218").build();
        UserSaveRequest mockUserSaveRequest = new UserSaveRequestBuilder()
                .withValidFields()
                .withPhoneNumber(mockPhoneNumber).build();

        // Then
        String endpoint = BASE_PATH.concat("/user");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminUserToken.getAccessToken(), mockUserSaveRequest);

        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userSaveService, Mockito.never())
                .saveUser(Mockito.any(UserSaveRequest.class));
    }

    @Test
    void givenValidUserListRequest_whenUsersFound_thenReturnUsersResponse() throws Exception {
        // Given
        UserListRequest mockUserListRequest = new UserListRequestBuilder().withValidValues().build();

        // When
        Page<UserEntity> mockUserEntities = new PageImpl<>(
                List.of(new UserEntityBuilder().withValidFields().build())
        );
        List<User> mockUsers = userEntityToUserMapper.map(mockUserEntities.getContent());
        AysPage<User> mockAysPageOfUsers = AysPage.of(mockUserListRequest.getFilter(), mockUserEntities, mockUsers);
        Mockito.when(userService.getAllUsers(Mockito.any(UserListRequest.class)))
                .thenReturn(mockAysPageOfUsers);

        // Then
        String endpoint = BASE_PATH.concat("/users");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminUserToken.getAccessToken(), mockUserListRequest);

        List<UsersResponse> mockUsersResponses = userToUsersResponseMapper.map(mockAysPageOfUsers.getContent());
        AysPageResponse<UsersResponse> pageOfUsersResponse = AysPageResponse.<UsersResponse>builder()
                .of(mockAysPageOfUsers)
                .content(mockUsersResponses)
                .build();
        AysResponse<AysPageResponse<UsersResponse>> mockResponse = AysResponse.successOf(pageOfUsersResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.content[0].createdAt")
                        .isNotEmpty());

        // Verify
        Mockito.verify(userService, Mockito.times(1))
                .getAllUsers(Mockito.any(UserListRequest.class));
    }

    @Test
    void givenInvalidUserListRequest_whenSortFieldIsInvalid_thenReturnValidationError() throws Exception {

        // Given
        Sort invalidSort = Sort.by(Sort.Direction.ASC, "firstName");
        UserListRequest mockUserListRequest = new UserListRequestBuilder()
                .withValidValues()
                .withSort(AysSorting.of(invalidSort))
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/users");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminUserToken.getAccessToken(), mockUserListRequest);

        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userService, Mockito.never())
                .getAllUsers(Mockito.any(UserListRequest.class));
    }

    @Test
    void givenInvalidUserListRequest_whenFilterFirstNameFieldIsInvalid_thenReturnValidationError() throws Exception {

        // Given
        UserListRequest.Filter invalidFilter = new UserListRequestBuilder.FilterBuilder()
                .withValidValues()
                .withFirstName("John 1234")
                .build();
        UserListRequest mockUserListRequest = new UserListRequestBuilder()
                .withValidValues()
                .withFilter(invalidFilter)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/users");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminUserToken.getAccessToken(), mockUserListRequest);

        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userService, Mockito.never())
                .getAllUsers(Mockito.any(UserListRequest.class));
    }

    @Test
    void givenInvalidUserListRequest_whenFilterPhoneNumberFieldIsInvalid_thenReturnValidationError() throws Exception {

        // Given
        AysPhoneNumberFilterRequest invalidPhoneNumber = new AysPhoneNumberFilterRequestBuilder()
                .withValidValues()
                .withCountryCode("90")
                .withLineNumber("23")
                .build();
        UserListRequest.Filter filter = new UserListRequestBuilder.FilterBuilder()
                .withValidValues()
                .withPhoneNumber(invalidPhoneNumber)
                .build();
        UserListRequest mockUserListRequest = new UserListRequestBuilder()
                .withValidValues()
                .withFilter(filter)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/users");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminUserToken.getAccessToken(), mockUserListRequest);

        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userService, Mockito.never())
                .getAllUsers(Mockito.any(UserListRequest.class));
    }

    @Test
    void givenValidUserListRequest_whenUserUnauthorizedForListing_thenReturnAccessDeniedException() throws Exception {
        // Given
        UserListRequest mockUserListRequest = new UserListRequestBuilder().withValidValues().build();

        // Then
        String endpoint = BASE_PATH.concat("/users");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken(), mockUserListRequest);

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(userService, Mockito.never())
                .getAllUsers(Mockito.any(UserListRequest.class));
    }

    @Test
    void givenValidUserId_whenUserFound_thenReturnUserResponse() throws Exception {
        // Given
        String mockUserId = AysRandomUtil.generateUUID();

        // When
        User mockUser = new UserBuilder().build();
        Mockito.when(userService.getUserById(mockUserId))
                .thenReturn(mockUser);

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(mockUserId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockAdminUserToken.getAccessToken());

        UserResponse mockUserResponse = userToUserResponseMapper.map(mockUser);
        AysResponse<UserResponse> mockResponse = AysResponse.successOf(mockUserResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userService, Mockito.times(1))
                .getUserById(mockUserId);
    }

    @Test
    void givenValidValidUserId_whenUserUnauthorizedForGetting_thenReturnAccessDeniedException() throws Exception {
        // Given
        String mockUserId = AysRandomUtil.generateUUID();

        // Then
        String endpoint = BASE_PATH.concat("/user/".concat(mockUserId));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockUserToken.getAccessToken());

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(userService, Mockito.never())
                .getUserById(mockUserId);
    }


    @Test
    void givenValidUserIdAndUserUpdateRequest_whenUserUpdated_thenReturnAysResponseOfSuccess() throws Exception {

        // Given
        String mockUserId = AysRandomUtil.generateUUID();
        UserUpdateRequest mockUpdateRequest = new UserUpdateRequestBuilder()
                .withRole(UserRole.VOLUNTEER)
                .withStatus(UserStatus.ACTIVE).build();

        // When
        Mockito.doNothing().when(userService)
                .updateUser(Mockito.anyString(), Mockito.any(UserUpdateRequest.class));

        // Then
        String endpoint = BASE_PATH.concat("/user/".concat(mockUserId));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminUserToken.getAccessToken(), mockUpdateRequest);

        AysResponse<Void> mockResponse = AysResponse.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(userService, Mockito.times(1))
                .updateUser(Mockito.anyString(), Mockito.any(UserUpdateRequest.class));
    }

    @Test
    void givenValidUserIdAndUserUpdateRequest_whenUserUnauthorizedForUpdating_thenReturnAccessDeniedException() throws Exception {
        // Given
        String mockUserId = AysRandomUtil.generateUUID();
        UserUpdateRequest mockUpdateRequest = new UserUpdateRequestBuilder()
                .withRole(UserRole.VOLUNTEER)
                .withStatus(UserStatus.ACTIVE).build();

        // Then
        String endpoint = BASE_PATH.concat("/user/".concat(mockUserId));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockUserToken.getAccessToken(), mockUpdateRequest);

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(userService, Mockito.never())
                .updateUser(Mockito.anyString(), Mockito.any(UserUpdateRequest.class));
    }

    @Test
    void givenValidUserId_whenUserDeleted_thenReturnAysResponseOfSuccess() throws Exception {
        // Given
        String mockUserId = AysRandomUtil.generateUUID();

        // When
        Mockito.doNothing().when(userService).deleteUser(Mockito.anyString());

        // Then
        String endpoint = BASE_PATH.concat("/user/".concat(mockUserId));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .delete(endpoint, mockAdminUserToken.getAccessToken());

        AysResponse<Void> mockResponse = AysResponse.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(userService, Mockito.times(1))
                .deleteUser(Mockito.anyString());
    }

    @Test
    void givenValidValidUserId_whenUserUnauthorizedForDeleting_thenReturnAccessDeniedException() throws Exception {
        // Given
        String mockUserId = AysRandomUtil.generateUUID();

        // Then
        String endpoint = BASE_PATH.concat("/user/".concat(mockUserId));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockUserToken.getAccessToken());

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(userService, Mockito.never())
                .deleteUser(Mockito.anyString());
    }
}
