package com.ays.user.controller;

import com.ays.AbstractSystemTest;
import com.ays.common.model.AysPage;
import com.ays.common.model.dto.request.AysPhoneNumberRequest;
import com.ays.common.model.dto.request.AysPhoneNumberRequestBuilder;
import com.ays.common.model.dto.response.AysPageResponse;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.model.dto.response.AysResponseBuilder;
import com.ays.common.util.AysRandomUtil;
import com.ays.common.util.exception.model.AysError;
import com.ays.common.util.exception.model.AysErrorBuilder;
import com.ays.institution.model.entity.InstitutionEntity;
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
import com.ays.user.model.enums.UserSupportStatus;
import com.ays.user.model.mapper.UserEntityToUserMapper;
import com.ays.user.model.mapper.UserToUserResponseMapper;
import com.ays.user.model.mapper.UserToUsersResponseMapper;
import com.ays.util.AysMockMvcRequestBuilders;
import com.ays.util.AysMockResultMatchersBuilders;
import com.ays.util.AysValidTestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

class UserSystemTest extends AbstractSystemTest {

    private final UserToUsersResponseMapper userToUsersResponseMapper = UserToUsersResponseMapper.initialize();
    private final UserToUserResponseMapper userToUserResponseMapper = UserToUserResponseMapper.initialize();
    private final UserEntityToUserMapper userEntityToUserMapper = UserEntityToUserMapper.initialize();


    private static final String BASE_PATH = "/api/v1";


    private void initialize(UserEntity mockUserEntity) {
        userRepository.save(mockUserEntity);
    }


    @Test
    void givenValidUserSaveRequest_whenUserSaved_thenReturnUserSavedResponse() throws Exception {
        // Given
        UserSaveRequest userSaveRequest = new UserSaveRequestBuilder()
                .withPhoneNumber(new AysPhoneNumberRequestBuilder().withValidFields().build())
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user");
        UserSavedResponse userSavedResponse = new UserSavedResponseBuilder().build();
        AysResponse<UserSavedResponse> response = AysResponseBuilder.successOf(userSavedResponse);
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, adminUserToken.getAccessToken(), userSaveRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(response.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(response.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.username")
                        .isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.password")
                        .isNotEmpty());
    }

    @Test
    void givenValidUserSaveRequest_whenUserUnauthorizedForSaving_thenReturnAccessDeniedException() throws Exception {
        // Given
        UserSaveRequest userSaveRequest = new UserSaveRequestBuilder()
                .withPhoneNumber(new AysPhoneNumberRequestBuilder().withValidFields().build())
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, userToken.getAccessToken(), userSaveRequest);

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
        AysPhoneNumberRequest phoneNumber = new AysPhoneNumberRequestBuilder()
                .withCountryCode("ABC")
                .withLineNumber("ABC").build();
        UserSaveRequest userSaveRequest = new UserSaveRequestBuilder()
                .withValidFields()
                .withPhoneNumber(phoneNumber).build();

        // Then
        String endpoint = BASE_PATH.concat("/user");
        AysError errorResponse = AysErrorBuilder.VALIDATION_ERROR;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, adminUserToken.getAccessToken(), userSaveRequest))
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
        AysPhoneNumberRequest phoneNumber = new AysPhoneNumberRequestBuilder()
                .withCountryCode("456786745645")
                .withLineNumber("6546467456435548676845321346656654").build();
        UserSaveRequest userSaveRequest = new UserSaveRequestBuilder()
                .withValidFields()
                .withPhoneNumber(phoneNumber).build();

        // Then
        String endpoint = BASE_PATH.concat("/user");
        AysError errorResponse = AysErrorBuilder.VALIDATION_ERROR;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, adminUserToken.getAccessToken(), userSaveRequest))
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
        UserListRequest userListRequest = new UserListRequestBuilder()
                .withValidValues()
                .withFilter(null)
                .withSort(null)
                .build();

        // When
        Page<UserEntity> userEntities = new PageImpl<>(
                List.of(new UserEntityBuilder().withValidFields().build())
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
                        .post(endpoint, adminUserToken.getAccessToken(), userListRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(response.getHttpStatus().getReasonPhrase()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(response.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.content[0].createdAt")
                        .exists());
    }

    @Test
    void givenValidUserListRequest_whenUserUnauthorizedForListing_thenReturnAccessDeniedException() throws Exception {
        // Given
        UserListRequest userListRequest = new UserListRequestBuilder().withValidValues().build();

        // Then
        String endpoint = BASE_PATH.concat("/users");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, userToken.getAccessToken(), userListRequest);

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
        String userId = AysValidTestData.User.ID;
        User user = new UserBuilder()
                .withId(userId)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(userId);
        UserResponse userResponse = userToUserResponseMapper.map(user);
        AysResponse<UserResponse> response = AysResponse.successOf(userResponse);
        mockMvc.perform(AysMockMvcRequestBuilders
                        .get(endpoint, adminUserToken.getAccessToken()))
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
        String userId = AysValidTestData.User.ID;

        // Then
        String endpoint = BASE_PATH.concat("/user/".concat(userId));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, userToken.getAccessToken());

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

        // Initialize
        InstitutionEntity mockInstitutionEntity = institutionRepository
                .findById(AysValidTestData.Institution.ID).get();
        UserEntity mockUserEntity = new UserEntityBuilder()
                .withValidFields()
                .withInstitutionId(mockInstitutionEntity.getId())
                .withInstitution(mockInstitutionEntity)
                .withSupportStatus(UserSupportStatus.READY)
                .build();
        this.initialize(mockUserEntity);

        // Given
        String userId = mockUserEntity.getId();
        UserUpdateRequest mockUpdateRequest = new UserUpdateRequestBuilder()
                .withRole(UserRole.VOLUNTEER)
                .withStatus(UserStatus.PASSIVE).build();

        // Then
        String endpoint = BASE_PATH.concat("/user/".concat(userId));
        AysResponse<Void> mockAysResponse = AysResponse.SUCCESS;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .put(endpoint, adminUserToken.getAccessToken(), mockUpdateRequest))
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
        String userId = AysRandomUtil.generateUUID();
        UserUpdateRequest mockUpdateRequest = new UserUpdateRequestBuilder()
                .withRole(UserRole.VOLUNTEER)
                .withStatus(UserStatus.PASSIVE).build();

        // Then
        String endpoint = BASE_PATH.concat("/user/".concat(userId));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, userToken.getAccessToken(), mockUpdateRequest);

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

        // Initialize
        InstitutionEntity mockInstitutionEntity = institutionRepository
                .findById(AysValidTestData.Institution.ID).get();
        UserEntity mockUserEntity = new UserEntityBuilder()
                .withValidFields()
                .withInstitutionId(mockInstitutionEntity.getId())
                .withInstitution(mockInstitutionEntity)
                .withSupportStatus(UserSupportStatus.READY)
                .build();
        this.initialize(mockUserEntity);

        // Given
        String userId = mockUserEntity.getId();

        // Then
        String endpoint = BASE_PATH.concat("/user/".concat(userId));
        AysResponse<Void> mockAysResponse = AysResponse.SUCCESS;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .delete(endpoint, adminUserToken.getAccessToken()))
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

        // Verify
        Optional<UserEntity> userEntity = userRepository.findById(mockUserEntity.getId());
        Assertions.assertTrue(userEntity.isPresent());
        Assertions.assertEquals(UserStatus.DELETED, userEntity.get().getStatus());
    }

    @Test
    void givenValidValidUserId_whenUserUnauthorizedForDeleting_thenReturnAccessDeniedException() throws Exception {
        // Given
        String userId = AysRandomUtil.generateUUID();

        // Then
        String endpoint = BASE_PATH.concat("/user/".concat(userId));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, userToken.getAccessToken());

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
