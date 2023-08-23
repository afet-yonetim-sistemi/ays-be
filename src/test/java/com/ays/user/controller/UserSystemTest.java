package com.ays.user.controller;

import com.ays.AbstractSystemTest;
import com.ays.common.model.AysPage;
import com.ays.common.model.AysPhoneNumber;
import com.ays.common.model.AysPhoneNumberBuilder;
import com.ays.common.model.dto.response.AysPageResponse;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.model.dto.response.AysResponseBuilder;
import com.ays.common.util.exception.model.AysError;
import com.ays.common.util.exception.model.AysErrorBuilder;
import com.ays.user.model.User;
import com.ays.user.model.UserBuilder;
import com.ays.user.model.dto.request.*;
import com.ays.user.model.dto.response.UserResponse;
import com.ays.user.model.dto.response.UsersResponse;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.model.entity.UserEntityBuilder;
import com.ays.user.model.enums.UserRole;
import com.ays.user.model.enums.UserStatus;
import com.ays.user.model.mapper.UserEntityToUserMapper;
import com.ays.user.model.mapper.UserToUserResponseMapper;
import com.ays.user.model.mapper.UserToUsersResponseMapper;
import com.ays.util.AysMockMvcRequestBuilders;
import com.ays.util.AysMockResultMatchersBuilders;
import com.ays.util.AysTestData;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

class UserSystemTest extends AbstractSystemTest {

    private final UserToUsersResponseMapper userToUsersResponseMapper = UserToUsersResponseMapper.initialize();
    private final UserToUserResponseMapper userToUserResponseMapper = UserToUserResponseMapper.initialize();
    private final UserEntityToUserMapper userEntityToUserMapper = UserEntityToUserMapper.initialize();


    private static final String BASE_PATH = "/api/v1";

    @Test
    void givenValidUserSaveRequest_whenUserSaved_thenReturnUserSavedResponse() throws Exception {
        // Given
        UserSaveRequest userSaveRequest = new UserSaveRequestBuilder()
                .withPhoneNumber(new AysPhoneNumberBuilder().withValidFields().build())
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user");
        AysResponse<Void> response = AysResponseBuilder.SUCCESS;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, adminUserTokenOne.getAccessToken(), userSaveRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(response.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(response.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

    @Test
    void givenValidUserSaveRequest_whenUserUnauthorizedForSaving_thenReturnAccessDeniedException() throws Exception {
        // Given
        UserSaveRequest userSaveRequest = new UserSaveRequestBuilder()
                .withPhoneNumber(new AysPhoneNumberBuilder().withValidFields().build())
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, userTokenOne.getAccessToken(), userSaveRequest);

        AysResponse<AysError> response = AysResponseBuilder.FORBIDDEN;
        mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isForbidden())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(response.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(response.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

    @Test
    void givenPhoneNumberWithAlphanumericCharacter_whenPhoneNumberIsNotValid_thenReturnValidationError() throws Exception {
        // Given
        AysPhoneNumber phoneNumber = new AysPhoneNumberBuilder()
                .withCountryCode("ABC")
                .withLineNumber("ABC").build();
        UserSaveRequest userSaveRequest = new UserSaveRequestBuilder()
                .withValidFields()
                .withPhoneNumber(phoneNumber).build();

        // Then
        String endpoint = BASE_PATH.concat("/user");
        AysError errorResponse = AysErrorBuilder.VALIDATION_ERROR;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, adminUserTokenOne.getAccessToken(), userSaveRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(errorResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.header()
                        .value(errorResponse.getHeader()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(errorResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());
    }

    @Test
    void givenPhoneNumberWithInvalidLength_whenPhoneNumberIsNotValid_thenReturnValidationError() throws Exception {
        // Given
        AysPhoneNumber phoneNumber = new AysPhoneNumberBuilder()
                .withCountryCode("456786745645")
                .withLineNumber("6546467456435548676845321346656654").build();
        UserSaveRequest userSaveRequest = new UserSaveRequestBuilder()
                .withValidFields()
                .withPhoneNumber(phoneNumber).build();

        // Then
        String endpoint = BASE_PATH.concat("/user");
        AysError errorResponse = AysErrorBuilder.VALIDATION_ERROR;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, adminUserTokenOne.getAccessToken(), userSaveRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(errorResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.header()
                        .value(errorResponse.getHeader()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(errorResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());
    }

    @Test
    void givenValidUserListRequest_whenUsersFound_thenReturnUsersResponse() throws Exception {
        // Given
        UserListRequest userListRequest = new UserListRequestBuilder().withValidValues().build();

        // When
        Page<UserEntity> userEntities = new PageImpl<>(
                UserEntityBuilder.generateValidUserEntities(1)
        );
        List<User> users = userEntityToUserMapper.map(userEntities.getContent());
        AysPage<User> pageOfUsers = AysPage.of(userEntities, users);

        // Then
        String endpoint = BASE_PATH.concat("/users");
        List<UsersResponse> usersResponses = userToUsersResponseMapper.map(pageOfUsers.getContent());
        AysPageResponse<UsersResponse> pageOfUsersResponse = AysPageResponse.<UsersResponse>builder()
                .of(pageOfUsers)
                .content(usersResponses)
                .build();
        AysResponse<AysPageResponse<UsersResponse>> response = AysResponse.successOf(pageOfUsersResponse);
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, adminUserTokenOne.getAccessToken(), userListRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(response.getHttpStatus().getReasonPhrase()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(response.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());
    }

    @Test
    void givenValidUserListRequest_whenUserUnauthorizedForListing_thenReturnAccessDeniedException() throws Exception {
        // Given
        UserListRequest userListRequest = new UserListRequestBuilder().withValidValues().build();

        // Then
        String endpoint = BASE_PATH.concat("/users");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, userTokenOne.getAccessToken(), userListRequest);

        AysResponse<AysError> response = AysResponseBuilder.FORBIDDEN;
        mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isForbidden())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(response.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(response.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

    @Test
    void givenValidUserId_whenUserFound_thenReturnUserResponse() throws Exception {
        // Given
        String userId = AysTestData.User.VALID_ID_ONE;
        User user = new UserBuilder()
                .withId(userId)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(userId);
        UserResponse userResponse = userToUserResponseMapper.map(user);
        AysResponse<UserResponse> response = AysResponse.successOf(userResponse);
        mockMvc.perform(AysMockMvcRequestBuilders
                        .get(endpoint, adminUserTokenOne.getAccessToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(response.getHttpStatus().getReasonPhrase()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(response.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());
    }

    @Test
    void givenValidValidUserId_whenUserUnauthorizedForGetting_thenReturnAccessDeniedException() throws Exception {
        // Given
        String userId = AysTestData.User.VALID_ID_ONE;

        // Then
        String endpoint = BASE_PATH.concat("/user/".concat(userId));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, userTokenOne.getAccessToken());

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
        String userId = AysTestData.User.VALID_ID_TWO;
        UserUpdateRequest mockUpdateRequest = new UserUpdateRequestBuilder()
                .withRole(UserRole.VOLUNTEER)
                .withStatus(UserStatus.PASSIVE).build();

        // Then
        String endpoint = BASE_PATH.concat("/user/".concat(userId));
        AysResponse<Void> mockAysResponse = AysResponse.SUCCESS;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .put(endpoint, adminUserTokenOne.getAccessToken(), mockUpdateRequest))
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
    }

    @Test
    void givenValidUserIdAndUserUpdateRequest_whenUserUnauthorizedForUpdating_thenReturnAccessDeniedException() throws Exception {
        // Given
        String userId = AysTestData.User.VALID_ID_TWO;
        UserUpdateRequest mockUpdateRequest = new UserUpdateRequestBuilder()
                .withRole(UserRole.VOLUNTEER)
                .withStatus(UserStatus.PASSIVE).build();

        // Then
        String endpoint = BASE_PATH.concat("/user/".concat(userId));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, userTokenTwo.getAccessToken(), mockUpdateRequest);

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
        String userId = AysTestData.User.VALID_ID_THREE;

        // Then
        String endpoint = BASE_PATH.concat("/user/".concat(userId));
        AysResponse<Void> mockAysResponse = AysResponse.SUCCESS;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .delete(endpoint, adminUserTokenTwo.getAccessToken()))
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
    }

    @Test
    void givenValidValidUserId_whenUserUnauthorizedForDeleting_thenReturnAccessDeniedException() throws Exception {
        // Given
        String userId = AysTestData.User.VALID_ID_THREE;

        // Then
        String endpoint = BASE_PATH.concat("/user/".concat(userId));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, userTokenThree.getAccessToken());

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
