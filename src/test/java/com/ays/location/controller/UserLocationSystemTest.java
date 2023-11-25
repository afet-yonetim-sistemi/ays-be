package com.ays.location.controller;

import com.ays.AbstractSystemTest;
import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.entity.AssignmentEntityBuilder;
import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.auth.model.AysToken;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.model.dto.response.AysResponseBuilder;
import com.ays.common.util.exception.model.AysError;
import com.ays.institution.model.entity.InstitutionEntity;
import com.ays.location.model.dto.request.UserLocationSaveRequest;
import com.ays.location.model.dto.request.UserLocationSaveRequestBuilder;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.model.entity.UserEntityBuilder;
import com.ays.user.model.enums.UserSupportStatus;
import com.ays.util.AysMockMvcRequestBuilders;
import com.ays.util.AysMockResultMatchersBuilders;
import com.ays.util.AysValidTestData;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

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
        AysResponse<Void> mockAysResponse = AysResponse.SUCCESS;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(BASE_PATH, mockUserToken.getAccessToken(), mockUserLocationSaveRequest))
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
    void givenValidUserSupportStatusUpdateRequest_whenAdminRole_thenReturnAccessDeniedException() throws Exception {
        // Given
        UserLocationSaveRequest mockUserLocationSaveRequest = new UserLocationSaveRequestBuilder()
                .withValidFields()
                .build();

        // Then
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(BASE_PATH, adminUserToken.getAccessToken(), mockUserLocationSaveRequest);

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
