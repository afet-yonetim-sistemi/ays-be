package org.ays.location.controller;

import org.ays.AbstractSystemTest;
import org.ays.assignment.model.entity.AssignmentEntity;
import org.ays.assignment.model.entity.AssignmentEntityBuilder;
import org.ays.assignment.model.enums.AssignmentStatus;
import org.ays.auth.model.AysToken;
import org.ays.common.model.dto.response.AysResponse;
import org.ays.common.util.exception.model.AysError;
import org.ays.common.util.exception.model.AysErrorBuilder;
import org.ays.institution.model.entity.InstitutionEntity;
import org.ays.location.model.dto.request.UserLocationSaveRequest;
import org.ays.location.model.dto.request.UserLocationSaveRequestBuilder;
import org.ays.user.model.entity.UserEntity;
import org.ays.user.model.entity.UserEntityBuilder;
import org.ays.user.model.enums.UserSupportStatus;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.ays.util.AysValidTestData;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Map;

class UserLocationSystemTest extends AbstractSystemTest {

    private static final String BASE_PATH = "/api/v1/user/location";


    private AysToken mockUserToken;

    private void initialize(UserEntity mockUserEntity,
                            AssignmentEntity mockAssignmentEntity) {

        UserEntity userEntity = userRepository.save(mockUserEntity);

        assignmentRepository.save(mockAssignmentEntity);

        final Map<String, Object> claimsOfMockUser = userEntity.getClaims();
        this.mockUserToken = this.generate(claimsOfMockUser);
    }


    @Test
    void givenValidUserLocationSaveRequest_whenSavedOrUpdatedLocation_thenReturnSuccess() throws Exception {
        // Initialize
        InstitutionEntity mockInstitutionEntity = institutionRepository
                .findById(AysValidTestData.Institution.ID).get();
        UserEntity mockUserEntity = new UserEntityBuilder()
                .withValidFields()
                .withInstitutionId(mockInstitutionEntity.getId())
                .withInstitution(null)
                .withSupportStatus(UserSupportStatus.ON_ROAD)
                .build();
        AssignmentEntity mockAssignmentEntity = new AssignmentEntityBuilder()
                .withValidFields()
                .withStatus(AssignmentStatus.IN_PROGRESS)
                .withInstitutionId(mockInstitutionEntity.getId())
                .withInstitution(null)
                .withUserId(mockUserEntity.getId())
                .withUser(null)
                .build();
        this.initialize(mockUserEntity, mockAssignmentEntity);


        // Given
        UserLocationSaveRequest mockUserLocationSaveRequest = new UserLocationSaveRequestBuilder()
                .withValidFields()
                .build();

        // Then
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(BASE_PATH, mockUserToken.getAccessToken(), mockUserLocationSaveRequest);

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
        UserLocationSaveRequest mockUserLocationSaveRequest = new UserLocationSaveRequestBuilder()
                .withValidFields()
                .build();

        // Then
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(BASE_PATH, adminUserToken.getAccessToken(), mockUserLocationSaveRequest);

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());
    }

}
