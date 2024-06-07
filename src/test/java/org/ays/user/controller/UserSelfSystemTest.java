package org.ays.user.controller;

import io.jsonwebtoken.Claims;
import org.ays.AbstractSystemTest;
import org.ays.auth.model.AysToken;
import org.ays.common.model.dto.response.AysResponse;
import org.ays.common.util.exception.model.AysErrorBuilder;
import org.ays.common.util.exception.model.AysErrorResponse;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.institution.model.entity.InstitutionEntityBuilder;
import org.ays.user.model.User;
import org.ays.user.model.UserBuilder;
import org.ays.user.model.dto.request.UserSupportStatusUpdateRequest;
import org.ays.user.model.dto.request.UserSupportStatusUpdateRequestBuilder;
import org.ays.user.model.dto.response.UserSelfResponse;
import org.ays.user.model.entity.UserEntity;
import org.ays.user.model.entity.UserEntityBuilder;
import org.ays.user.model.enums.UserSupportStatus;
import org.ays.user.model.mapper.UserToUserSelfResponseMapper;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

class UserSelfSystemTest extends AbstractSystemTest {

    private final UserToUserSelfResponseMapper userToUserSelfResponseMapper = UserToUserSelfResponseMapper.initialize();


    private static final String BASE_PATH = "/api/v1/user-self";


    private AysToken mockUserToken;

    @Test
    void whenUserFound_thenReturnUserSelfResponse() throws Exception {
        // Initialize
        InstitutionEntity institutionEntity = institutionRepository.save(
                new InstitutionEntityBuilder()
                        .withValidFields()
                        .build()
        );

        UserEntity userEntity = userRepository.save(
                new UserEntityBuilder()
                        .withValidFields()
                        .withInstitutionId(institutionEntity.getId())
                        .withInstitution(null)
                        .withSupportStatus(UserSupportStatus.READY)
                        .build()
        );

        final Claims claimsOfMockUser = userEntity.getClaims();
        this.mockUserToken = this.generate(claimsOfMockUser);

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
        String accessToken = adminUserToken.getAccessToken();

        // Then
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(BASE_PATH, accessToken);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());
    }


    @Test
    void givenValidUserSupportStatusUpdateRequest_whenUserRole_thenReturnSuccess() throws Exception {
        // Initialize
        InstitutionEntity institutionEntity = institutionRepository.save(
                new InstitutionEntityBuilder()
                        .withValidFields()
                        .build()
        );

        UserEntity userEntity = userRepository.save(
                new UserEntityBuilder()
                        .withValidFields()
                        .withInstitutionId(institutionEntity.getId())
                        .withInstitution(null)
                        .withSupportStatus(UserSupportStatus.READY)
                        .build()
        );

        final Claims claimsOfMockUser = userEntity.getClaims();
        this.mockUserToken = this.generate(claimsOfMockUser);

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

        AysErrorResponse mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());
    }

}
