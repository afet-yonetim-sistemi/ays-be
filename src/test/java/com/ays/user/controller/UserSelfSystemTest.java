package com.ays.user.controller;

import com.ays.AbstractSystemTest;
import com.ays.auth.model.AysToken;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.util.exception.model.AysError;
import com.ays.common.util.exception.model.AysErrorBuilder;
import com.ays.institution.model.entity.InstitutionEntity;
import com.ays.institution.model.entity.InstitutionEntityBuilder;
import com.ays.user.model.User;
import com.ays.user.model.UserBuilder;
import com.ays.user.model.dto.request.UserSupportStatusUpdateRequest;
import com.ays.user.model.dto.request.UserSupportStatusUpdateRequestBuilder;
import com.ays.user.model.dto.response.UserSelfResponse;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.model.entity.UserEntityBuilder;
import com.ays.user.model.enums.UserSupportStatus;
import com.ays.user.model.mapper.UserToUserSelfResponseMapper;
import com.ays.util.AysMockMvcRequestBuilders;
import com.ays.util.AysMockResultMatchersBuilders;
import com.ays.util.AysValidTestData;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Map;

class UserSelfSystemTest extends AbstractSystemTest {

    private final UserToUserSelfResponseMapper userToUserSelfResponseMapper = UserToUserSelfResponseMapper.initialize();


    private static final String BASE_PATH = "/api/v1/user-self";


    private AysToken mockUserToken;

    private void initialize(InstitutionEntity mockInstitutionEntity,
                            UserEntity mockUserEntity) {

        institutionRepository.save(mockInstitutionEntity);

        UserEntity userEntity = userRepository.save(mockUserEntity);

        final Map<String, Object> claimsOfMockUser = userEntity.getClaims();
        this.mockUserToken = this.generate(claimsOfMockUser);
    }

    @Test
    void whenUserFound_thenReturnUserSelfResponse() throws Exception {
        // Initialize
        InstitutionEntity mockInstitutionEntity = new InstitutionEntityBuilder()
                .withValidFields()
                .build();
        UserEntity mockUserEntity = new UserEntityBuilder()
                .withValidFields()
                .withInstitutionId(mockInstitutionEntity.getId())
                .withInstitution(null)
                .withSupportStatus(UserSupportStatus.READY)
                .build();
        this.initialize(mockInstitutionEntity, mockUserEntity);

        // When
        User mockUser = new UserBuilder().build();

        // Then
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(BASE_PATH, mockUserToken.getAccessToken());

        UserSelfResponse mockUserSelfResponse = userToUserSelfResponseMapper.map(mockUser);
        AysResponse<UserSelfResponse> mockAysResponse = AysResponse.successOf(mockUserSelfResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockAysResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());
    }

    @Test
    void givenInvalidAccessToken_whenUserUnauthorizedForGetting_thenReturnAccessDeniedException() throws Exception {
        // Given
        String mockAccessToken = adminUserToken.getAccessToken();

        // Then
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(BASE_PATH, mockAccessToken);

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());
    }


    @Test
    void givenValidUserSupportStatusUpdateRequest_whenUserRole_thenReturnSuccess() throws Exception {
        // Initialize
        InstitutionEntity mockInstitutionEntity = institutionRepository
                .findById(AysValidTestData.Institution.ID).get();
        UserEntity mockUserEntity = new UserEntityBuilder()
                .withValidFields()
                .withInstitutionId(mockInstitutionEntity.getId())
                .withInstitution(mockInstitutionEntity)
                .withSupportStatus(UserSupportStatus.IDLE)
                .build();
        this.initialize(mockInstitutionEntity, mockUserEntity);

        // Given
        UserSupportStatus userSupportStatus = UserSupportStatus.READY;

        UserSupportStatusUpdateRequest mockUserSupportStatusUpdateRequest = new UserSupportStatusUpdateRequestBuilder()
                .withSupportStatus(userSupportStatus).build();

        // Then
        String endpoint = BASE_PATH.concat("/status/support");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockUserToken.getAccessToken(), mockUserSupportStatusUpdateRequest);

        AysResponse<Void> mockResponse = AysResponse.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

    @Test
    void givenValidUserSupportStatusUpdateRequest_whenAdminRole_thenReturnAccessDeniedException() throws Exception {
        // Given
        UserSupportStatus userSupportStatus = UserSupportStatus.READY;

        UserSupportStatusUpdateRequest mockUserSupportStatusUpdateRequest = new UserSupportStatusUpdateRequestBuilder()
                .withSupportStatus(userSupportStatus).build();

        // Then
        String endpoint = BASE_PATH.concat("/status/support");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, adminUserToken.getAccessToken(), mockUserSupportStatusUpdateRequest);

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());
    }

}
