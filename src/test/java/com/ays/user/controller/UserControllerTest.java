package com.ays.user.controller;

import com.ays.AbstractRestControllerTest;
import com.ays.assignment.model.dto.request.AssignmentUpdateRequest;
import com.ays.assignment.model.dto.request.AssignmentUpdateRequestBuilder;
import com.ays.common.model.AysPage;
import com.ays.common.model.AysSorting;
import com.ays.common.model.dto.request.AysPhoneNumberFilterRequest;
import com.ays.common.model.dto.request.AysPhoneNumberFilterRequestBuilder;
import com.ays.common.model.dto.request.AysPhoneNumberRequest;
import com.ays.common.model.dto.request.AysPhoneNumberRequestBuilder;
import com.ays.common.model.dto.response.AysPageResponse;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.model.dto.response.AysResponseBuilder;
import com.ays.common.util.AysRandomUtil;
import com.ays.common.util.exception.model.AysError;
import com.ays.common.util.exception.model.AysErrorBuilder;
import com.ays.user.model.User;
import com.ays.user.model.UserBuilder;
import com.ays.user.model.dto.request.UserListRequest;
import com.ays.user.model.dto.request.UserListRequestBuilder;
import com.ays.user.model.dto.request.UserSaveRequest;
import com.ays.user.model.dto.request.UserSaveRequestBuilder;
import com.ays.user.model.dto.request.UserUpdateRequest;
import com.ays.user.model.dto.request.UserUpdateRequestBuilder;
import com.ays.user.model.dto.response.UserResponse;
import com.ays.user.model.dto.response.UserSavedResponse;
import com.ays.user.model.dto.response.UserSavedResponseBuilder;
import com.ays.user.model.dto.response.UsersResponse;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.model.entity.UserEntityBuilder;
import com.ays.user.model.enums.UserRole;
import com.ays.user.model.enums.UserStatus;
import com.ays.user.model.mapper.UserEntityToUserMapper;
import com.ays.user.model.mapper.UserToUserResponseMapper;
import com.ays.user.model.mapper.UserToUsersResponseMapper;
import com.ays.user.service.UserSaveService;
import com.ays.user.service.UserService;
import com.ays.util.AysMockMvcRequestBuilders;
import com.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
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
        UserSavedResponse mockUserSavedResponse = new UserSavedResponseBuilder()
                .withUsername(mockUser.getUsername())
                .withPassword(mockUser.getPassword())
                .build();
        AysResponse<UserSavedResponse> mockResponse = AysResponseBuilder.successOf(mockUserSavedResponse);
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, mockAdminUserToken.getAccessToken(), mockUserSaveRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockResponse.getIsSuccess()))
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

    @Test
    void givenInvalidUserSaveRequestWithNameContainingNumber_whenNameIsNotValid_thenReturnValidationError() throws Exception {
        // Given
        String invalidName = "John 1234";
        UserSaveRequest mockRequest = new UserSaveRequestBuilder()
                .withValidFields()
                .withFirstName(invalidName)
                .withLastName(invalidName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user");

        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, mockAdminUserToken.getAccessToken(), mockRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockErrorResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.header()
                        .value(mockErrorResponse.getHeader()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockErrorResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userSaveService, Mockito.never())
                .saveUser(Mockito.any(UserSaveRequest.class));
    }

    @Test
    void givenInvalidUserSaveRequestWithNameContainingForbiddenSpecialChars_whenNameIsNotValid_thenReturnValidationError() throws Exception {
        // Given
        String invalidName = "John *^%$#";
        AssignmentUpdateRequest mockRequest = new AssignmentUpdateRequestBuilder()
                .withValidFields()
                .withFirstName(invalidName)
                .withLastName(invalidName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user");

        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, mockAdminUserToken.getAccessToken(), mockRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockErrorResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.header()
                        .value(mockErrorResponse.getHeader()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockErrorResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist())
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

        AysResponse<AysError> mockResponse = AysResponseBuilder.FORBIDDEN;
        mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isForbidden())
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
        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, mockAdminUserToken.getAccessToken(), mockUserSaveRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockErrorResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.header()
                        .value(mockErrorResponse.getHeader()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockErrorResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userSaveService, Mockito.times(0))
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
        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, mockAdminUserToken.getAccessToken(), mockUserSaveRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockErrorResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.header()
                        .value(mockErrorResponse.getHeader()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockErrorResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        Mockito.verify(userSaveService, Mockito.times(0))
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
        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, mockAdminUserToken.getAccessToken(), mockUserSaveRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockErrorResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.header()
                        .value(mockErrorResponse.getHeader()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockErrorResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        Mockito.verify(userSaveService, Mockito.times(0))
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
        List<UsersResponse> mockUsersResponses = userToUsersResponseMapper.map(mockAysPageOfUsers.getContent());
        AysPageResponse<UsersResponse> pageOfUsersResponse = AysPageResponse.<UsersResponse>builder()
                .of(mockAysPageOfUsers)
                .content(mockUsersResponses)
                .build();
        AysResponse<AysPageResponse<UsersResponse>> mockResponse = AysResponse.successOf(pageOfUsersResponse);
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, mockAdminUserToken.getAccessToken(), mockUserListRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockResponse.getHttpStatus().getReasonPhrase()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.content[0].createdAt")
                        .isNotEmpty());

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
        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, mockAdminUserToken.getAccessToken(), mockUserListRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockErrorResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.header()
                        .value(mockErrorResponse.getHeader()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockErrorResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

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
        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, mockAdminUserToken.getAccessToken(), mockUserListRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockErrorResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.header()
                        .value(mockErrorResponse.getHeader()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockErrorResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        Mockito.verify(userService, Mockito.never())
                .getAllUsers(Mockito.any(UserListRequest.class));
    }

    @Test
    void givenInvalidUserListRequest_whenFilterPhoneNumberFieldIsInvalid_thenReturnValidationError() throws Exception {

        // Given
        AysPhoneNumberFilterRequest invalidPhoneNumber = new AysPhoneNumberFilterRequestBuilder()
                .withValidValues()
                .withCountryCode("90")
                .withLineNumber("123456789")
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
        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, mockAdminUserToken.getAccessToken(), mockUserListRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockErrorResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.header()
                        .value(mockErrorResponse.getHeader()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockErrorResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

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

        AysResponse<AysError> mockResponse = AysResponseBuilder.FORBIDDEN;
        mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isForbidden())
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
    void givenValidUserId_whenUserFound_thenReturnUserResponse() throws Exception {
        // Given
        String mockUserId = AysRandomUtil.generateUUID();

        // When
        User mockUser = new UserBuilder().build();
        Mockito.when(userService.getUserById(mockUserId))
                .thenReturn(mockUser);

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(mockUserId);
        UserResponse mockUserResponse = userToUserResponseMapper.map(mockUser);
        AysResponse<UserResponse> mockAysResponse = AysResponse.successOf(mockUserResponse);
        mockMvc.perform(AysMockMvcRequestBuilders
                        .get(endpoint, mockAdminUserToken.getAccessToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockAysResponse.getHttpStatus().getReasonPhrase()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

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

        AysResponse<AysError> mockResponse = AysResponseBuilder.FORBIDDEN;
        mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isForbidden())
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
        AysResponse<Void> mockAysResponse = AysResponse.SUCCESS;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .put(endpoint, mockAdminUserToken.getAccessToken(), mockUpdateRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockAysResponse.getHttpStatus().getReasonPhrase()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

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

        AysResponse<AysError> mockResponse = AysResponseBuilder.FORBIDDEN;
        mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isForbidden())
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
    void givenValidUserId_whenUserDeleted_thenReturnAysResponseOfSuccess() throws Exception {
        // Given
        String mockUserId = AysRandomUtil.generateUUID();

        // When
        Mockito.doNothing().when(userService).deleteUser(Mockito.anyString());

        // Then
        String endpoint = BASE_PATH.concat("/user/".concat(mockUserId));
        AysResponse<Void> mockAysResponse = AysResponse.SUCCESS;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .delete(endpoint, mockAdminUserToken.getAccessToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockAysResponse.getHttpStatus().getReasonPhrase()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

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

        AysResponse<AysError> mockResponse = AysResponseBuilder.FORBIDDEN;
        mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isForbidden())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }
}
