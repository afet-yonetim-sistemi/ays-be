package com.ays.assignment.controller;

import com.ays.AbstractSystemTest;
import com.ays.assignment.model.Assignment;
import com.ays.assignment.model.AssignmentBuilder;
import com.ays.assignment.model.dto.request.AssignmentCancelRequest;
import com.ays.assignment.model.dto.request.AssignmentCancelRequestBuilder;
import com.ays.assignment.model.dto.request.AssignmentListRequest;
import com.ays.assignment.model.dto.request.AssignmentListRequestBuilder;
import com.ays.assignment.model.dto.request.AssignmentSaveRequest;
import com.ays.assignment.model.dto.request.AssignmentSaveRequestBuilder;
import com.ays.assignment.model.dto.request.AssignmentSearchRequest;
import com.ays.assignment.model.dto.request.AssignmentSearchRequestBuilder;
import com.ays.assignment.model.dto.request.AssignmentUpdateRequest;
import com.ays.assignment.model.dto.request.AssignmentUpdateRequestBuilder;
import com.ays.assignment.model.dto.response.AssignmentResponse;
import com.ays.assignment.model.dto.response.AssignmentSearchResponse;
import com.ays.assignment.model.dto.response.AssignmentSummaryResponse;
import com.ays.assignment.model.dto.response.AssignmentUserResponse;
import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.entity.AssignmentEntityBuilder;
import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.assignment.model.mapper.AssignmentEntityToAssignmentMapper;
import com.ays.assignment.model.mapper.AssignmentToAssignmentResponseMapper;
import com.ays.assignment.model.mapper.AssignmentToAssignmentSearchResponseMapper;
import com.ays.assignment.model.mapper.AssignmentToAssignmentSummaryResponseMapper;
import com.ays.assignment.model.mapper.AssignmentToAssignmentUserResponseMapper;
import com.ays.auth.model.AysToken;
import com.ays.common.model.AysPage;
import com.ays.common.model.dto.request.AysPhoneNumberRequestBuilder;
import com.ays.common.model.dto.response.AysPageResponse;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.model.dto.response.AysResponseBuilder;
import com.ays.common.util.AysRandomUtil;
import com.ays.common.util.exception.model.AysError;
import com.ays.institution.model.entity.InstitutionEntity;
import com.ays.institution.model.entity.InstitutionEntityBuilder;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.model.entity.UserEntityBuilder;
import com.ays.user.model.enums.UserSupportStatus;
import com.ays.util.AysMockMvcRequestBuilders;
import com.ays.util.AysMockResultMatchersBuilders;
import com.ays.util.AysValidTestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

class AssignmentSystemTest extends AbstractSystemTest {

    private final AssignmentToAssignmentResponseMapper assignmentToAssignmentResponseMapper = AssignmentToAssignmentResponseMapper.initialize();
    private final AssignmentToAssignmentSearchResponseMapper assignmentToAssignmentSearchResponseMapper = AssignmentToAssignmentSearchResponseMapper.initialize();
    private final AssignmentToAssignmentUserResponseMapper assignmentToAssignmentUserResponseMapper = AssignmentToAssignmentUserResponseMapper.initialize();
    private final AssignmentEntityToAssignmentMapper assignmentEntityToAssignmentMapper = AssignmentEntityToAssignmentMapper.initialize();
    private final AssignmentToAssignmentSummaryResponseMapper assignmentToAssignmentSummaryResponseMapper = AssignmentToAssignmentSummaryResponseMapper.initialize();


    private static final String BASE_PATH = "/api/v1";


    private AysToken mockUserToken;

    private void initialize(InstitutionEntity mockInstitutionEntity,
                            UserEntity mockUserEntity,
                            AssignmentEntity mockAssignmentEntity) {

        institutionRepository.save(mockInstitutionEntity);

        UserEntity userEntity = userRepository.save(mockUserEntity);

        assignmentRepository.save(mockAssignmentEntity);

        final Map<String, Object> claimsOfMockUser = userEntity.getClaims();
        this.mockUserToken = this.generate(claimsOfMockUser);
    }

    private void initialize(InstitutionEntity mockInstitutionEntity,
                            AssignmentEntity mockAssignmentEntity) {

        institutionRepository.save(mockInstitutionEntity);
        assignmentRepository.save(mockAssignmentEntity);
    }

    private void initialize(AssignmentEntity mockAssignmentEntity) {
        assignmentRepository.save(mockAssignmentEntity);
    }


    @Test
    void givenValidAssignmentSaveRequest_whenAssignmentSaved_thenReturnAssignmentSavedResponse() throws Exception {
        // Given
        AssignmentSaveRequest mockAssignmentSaveRequest = new AssignmentSaveRequestBuilder()
                .withValidFields()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/assignment");

        AysResponse<Void> mockAysResponse = AysResponse.SUCCESS;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, adminUserToken.getAccessToken(), mockAssignmentSaveRequest))
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
    void givenValidAssignmentSaveRequest_whenUserUnauthorizedForSaving_thenReturnAccessDeniedException() throws Exception {
        // Given
        AssignmentSaveRequest mockAssignmentSaveRequest = new AssignmentSaveRequestBuilder()
                .withPhoneNumber(new AysPhoneNumberRequestBuilder().withValidFields().build())
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/assignment");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, userToken.getAccessToken(), mockAssignmentSaveRequest);

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
    void whenUserAssignmentFound_thenReturnAssignmentUserResponse() throws Exception {

        // Initialize
        InstitutionEntity mockInstitutionEntity = new InstitutionEntityBuilder()
                .withValidFields()
                .build();
        UserEntity mockUserEntity = new UserEntityBuilder()
                .withValidFields()
                .withInstitutionId(mockInstitutionEntity.getId())
                .withInstitution(null)
                .withSupportStatus(UserSupportStatus.BUSY)
                .build();
        AssignmentEntity mockAssignmentEntity = new AssignmentEntityBuilder()
                .withValidFields()
                .withStatus(AssignmentStatus.ASSIGNED)
                .withInstitutionId(mockInstitutionEntity.getId())
                .withInstitution(null)
                .withUserId(mockUserEntity.getId())
                .withUser(null)
                .build();
        this.initialize(mockInstitutionEntity, mockUserEntity, mockAssignmentEntity);

        // When
        Assignment mockAssignment = new AssignmentBuilder()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/assignment");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockUserToken.getAccessToken());

        AssignmentUserResponse mockAssignmentUserResponse = assignmentToAssignmentUserResponseMapper.map(mockAssignment);
        AysResponse<AssignmentUserResponse> mockAysResponse = AysResponse.successOf(mockAssignmentUserResponse);
        mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockAysResponse.getHttpStatus().getReasonPhrase()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .exists());
    }

    @Test
    void whenUnauthorizedForGettingUserAssignment_thenReturnAccessDeniedException() throws Exception {

        // Then
        String endpoint = BASE_PATH.concat("/assignment");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, adminUserToken.getAccessToken());

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
    void givenValidAssignmentId_whenAssignmentFound_thenReturnAssignmentResponse() throws Exception {

        // Initialize
        InstitutionEntity mockInstitutionEntity = institutionRepository.findById(AysValidTestData.Institution.ID).get();
        AssignmentEntity mockAssignmentEntity = new AssignmentEntityBuilder()
                .withValidFields()
                .withStatus(AssignmentStatus.AVAILABLE)
                .withInstitutionId(mockInstitutionEntity.getId())
                .withInstitution(mockInstitutionEntity)
                .withUserId(null)
                .withUser(null)
                .build();
        this.initialize(mockInstitutionEntity, mockAssignmentEntity);

        // Given
        String mockAssignmentId = mockAssignmentEntity.getId();

        // When
        Assignment mockAssignment = new AssignmentBuilder()
                .withId(mockAssignmentId)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/assignment/").concat(mockAssignmentId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, adminUserToken.getAccessToken());

        AssignmentResponse mockAssignmentResponse = assignmentToAssignmentResponseMapper.map(mockAssignment);
        AysResponse<AssignmentResponse> mockAysResponse = AysResponse.successOf(mockAssignmentResponse);
        mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockAysResponse.getHttpStatus().getReasonPhrase()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .exists());
    }

    @Test
    void givenValidAssignmentId_whenUnauthorizedForGettingAssignmentById_thenReturnAccessDeniedException() throws Exception {

        // Given
        String mockAssignmentId = AysRandomUtil.generateUUID();

        // Then
        String endpoint = BASE_PATH.concat("/assignment/".concat(mockAssignmentId));
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
    void givenValidAssignmentSearchRequest_whenAssignmentSearched_thenReturnAssignmentSearchResponse() throws Exception {

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
        AssignmentEntity mockAssignmentEntity = new AssignmentEntityBuilder()
                .withValidFields()
                .withStatus(AssignmentStatus.AVAILABLE)
                .withInstitutionId(mockInstitutionEntity.getId())
                .withInstitution(null)
                .withUserId(null)
                .withUser(null)
                .build();
        this.initialize(mockInstitutionEntity, mockUserEntity, mockAssignmentEntity);

        // Given
        AssignmentSearchRequest mockSearchRequest = new AssignmentSearchRequestBuilder()
                .withValidFields()
                .build();
        Assignment mockAssignment = new AssignmentBuilder().build();

        // Then
        String endpoint = BASE_PATH.concat("/assignment/search");
        AssignmentSearchResponse mockSearchResponse = assignmentToAssignmentSearchResponseMapper.map(mockAssignment);
        AysResponse<AssignmentSearchResponse> mockAysResponse = AysResponse.successOf(mockSearchResponse);

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, mockUserToken.getAccessToken(), mockSearchRequest))
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

        // Verify
        Optional<UserEntity> userEntity = userRepository.findById(mockUserEntity.getId());
        Assertions.assertTrue(userEntity.isPresent());
        Assertions.assertEquals(UserSupportStatus.READY, userEntity.get().getSupportStatus());

        Optional<AssignmentEntity> assignmentEntity = assignmentRepository.findById(mockAssignmentEntity.getId());
        Assertions.assertTrue(assignmentEntity.isPresent());
        Assertions.assertEquals(AssignmentStatus.RESERVED, assignmentEntity.get().getStatus());
    }

    @Test
    void givenValidAssignmentSearchRequest_whenUserUnauthorizedForSearching_thenReturnAccessDeniedException() throws Exception {
        // Given
        AssignmentSearchRequest mockAssignmentSearchRequest = new AssignmentSearchRequestBuilder()
                .withValidFields()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/assignment/search");
        AysResponse<AysError> mockAysResponse = AysResponseBuilder.FORBIDDEN;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, adminUserToken.getAccessToken(), mockAssignmentSearchRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isForbidden())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockAysResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

    @Test
    void givenValidAssignmentListRequest_whenAssignmentsFound_thenReturnAssignmentsResponse() throws Exception {

        // Given
        AssignmentListRequest listRequest = new AssignmentListRequestBuilder().withValidValues().build();

        List<AssignmentEntity> assignmentEntities = List.of(new AssignmentEntityBuilder().withValidFields().build());
        Page<AssignmentEntity> pageOfAssignmentEntities = new PageImpl<>(assignmentEntities);
        List<Assignment> assignments = assignmentEntityToAssignmentMapper.map(assignmentEntities);
        AysPage<Assignment> aysPageOfAssignments = AysPage.of(pageOfAssignmentEntities, assignments);
        AysPageResponse<Assignment> aysPageResponseOfAssignments = AysPageResponse.<Assignment>builder()
                .of(aysPageOfAssignments).build();

        // Then
        String endpoint = BASE_PATH.concat("/assignments");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, adminUserToken.getAccessToken(), listRequest);

        AysResponse<AysPageResponse<Assignment>> response = AysResponse.successOf(aysPageResponseOfAssignments);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(response.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(response.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());
    }

    @Test
    void givenValidAssignmentListRequest_whenUserUnauthorizedForListing_thenReturnAccessDeniedException() throws Exception {

        // Given
        AssignmentListRequest listRequest = new AssignmentListRequestBuilder().withValidValues().build();

        // Then
        String endpoint = BASE_PATH.concat("/assignments");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, userToken.getAccessToken(), listRequest);

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
    void whenAssignmentApproved_thenReturnNothing() throws Exception {

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
        AssignmentEntity mockAssignmentEntity = new AssignmentEntityBuilder()
                .withValidFields()
                .withStatus(AssignmentStatus.RESERVED)
                .withInstitutionId(mockInstitutionEntity.getId())
                .withUserId(mockUserEntity.getId())
                .withInstitution(null)
                .withUserId(mockUserEntity.getId())
                .withUser(null)
                .build();
        this.initialize(mockInstitutionEntity, mockUserEntity, mockAssignmentEntity);

        // Then
        String endpoint = BASE_PATH.concat("/assignment/approve");
        AysResponse<Void> mockAysResponse = AysResponse.SUCCESS;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, mockUserToken.getAccessToken()))
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
        Assertions.assertEquals(UserSupportStatus.BUSY, userEntity.get().getSupportStatus());

        Optional<AssignmentEntity> assignmentEntity = assignmentRepository.findById(mockAssignmentEntity.getId());
        Assertions.assertTrue(assignmentEntity.isPresent());
        Assertions.assertEquals(AssignmentStatus.ASSIGNED, assignmentEntity.get().getStatus());
    }

    @Test
    void whenUserUnauthorizedForApproving_thenReturnAccessDeniedException() throws Exception {
        // Then
        String endpoint = BASE_PATH.concat("/assignment/approve");
        AysResponse<AysError> mockAysResponse = AysResponseBuilder.FORBIDDEN;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, adminUserToken.getAccessToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isForbidden())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockAysResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

    @Test
    void whenAssignmentStarted_thenReturnNothing() throws Exception {

        // Initialize
        InstitutionEntity mockInstitutionEntity = new InstitutionEntityBuilder()
                .withValidFields()
                .build();
        UserEntity mockUserEntity = new UserEntityBuilder()
                .withValidFields()
                .withInstitutionId(mockInstitutionEntity.getId())
                .withSupportStatus(UserSupportStatus.BUSY)
                .withInstitution(null)
                .build();
        AssignmentEntity mockAssignmentEntity = new AssignmentEntityBuilder()
                .withValidFields()
                .withStatus(AssignmentStatus.ASSIGNED)
                .withInstitutionId(mockInstitutionEntity.getId())
                .withInstitution(null)
                .withUserId(mockUserEntity.getId())
                .withUser(null)
                .build();
        this.initialize(mockInstitutionEntity, mockUserEntity, mockAssignmentEntity);

        // Then
        String endpoint = BASE_PATH.concat("/assignment/start");
        AysResponse<Void> mockAysResponse = AysResponse.SUCCESS;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, mockUserToken.getAccessToken()))
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
        Assertions.assertEquals(UserSupportStatus.ON_ROAD, userEntity.get().getSupportStatus());

        Optional<AssignmentEntity> assignmentEntity = assignmentRepository.findById(mockAssignmentEntity.getId());
        Assertions.assertTrue(assignmentEntity.isPresent());
        Assertions.assertEquals(AssignmentStatus.IN_PROGRESS, assignmentEntity.get().getStatus());
    }

    @Test
    void whenUserUnauthorizedForStarting_thenReturnAccessDeniedException() throws Exception {
        // Then
        String endpoint = BASE_PATH.concat("/assignment/start");
        AysResponse<AysError> mockAysResponse = AysResponseBuilder.FORBIDDEN;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, adminUserToken.getAccessToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isForbidden())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockAysResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

    @Test
    void whenAssignmentRejected_thenReturnNothing() throws Exception {

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
        AssignmentEntity mockAssignmentEntity = new AssignmentEntityBuilder()
                .withValidFields()
                .withStatus(AssignmentStatus.RESERVED)
                .withInstitutionId(mockInstitutionEntity.getId())
                .withInstitution(null)
                .withUserId(mockUserEntity.getId())
                .withUser(null)
                .build();
        this.initialize(mockInstitutionEntity, mockUserEntity, mockAssignmentEntity);

        // Then
        String endpoint = BASE_PATH.concat("/assignment/reject");
        AysResponse<Void> mockAysResponse = AysResponse.SUCCESS;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, mockUserToken.getAccessToken()))
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
        Assertions.assertEquals(UserSupportStatus.READY, userEntity.get().getSupportStatus());

        Optional<AssignmentEntity> assignmentEntity = assignmentRepository.findById(mockAssignmentEntity.getId());
        Assertions.assertTrue(assignmentEntity.isPresent());
        Assertions.assertEquals(AssignmentStatus.AVAILABLE, assignmentEntity.get().getStatus());
    }

    @Test
    void whenUserUnauthorizedForRejecting_thenReturnAccessDeniedException() throws Exception {
        // Then
        String endpoint = BASE_PATH.concat("/assignment/reject");
        AysResponse<AysError> mockAysResponse = AysResponseBuilder.FORBIDDEN;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, adminUserToken.getAccessToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isForbidden())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockAysResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

    @Test
    void whenAssignmentCompleted_thenReturnNothing() throws Exception {

        // Initialize
        InstitutionEntity mockInstitutionEntity = new InstitutionEntityBuilder()
                .withValidFields()
                .build();
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
        this.initialize(mockInstitutionEntity, mockUserEntity, mockAssignmentEntity);

        // Then
        String endpoint = BASE_PATH.concat("/assignment/complete");
        AysResponse<Void> mockAysResponse = AysResponse.SUCCESS;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, mockUserToken.getAccessToken()))
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
        Assertions.assertEquals(UserSupportStatus.READY, userEntity.get().getSupportStatus());

        Optional<AssignmentEntity> assignmentEntity = assignmentRepository.findById(mockAssignmentEntity.getId());
        Assertions.assertTrue(assignmentEntity.isPresent());
        Assertions.assertEquals(AssignmentStatus.DONE, assignmentEntity.get().getStatus());
    }

    @Test
    void whenUserUnauthorizedForCompleting_thenReturnAccessDeniedException() throws Exception {
        // Then
        String endpoint = BASE_PATH.concat("/assignment/complete");
        AysResponse<AysError> mockAysResponse = AysResponseBuilder.FORBIDDEN;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, adminUserToken.getAccessToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isForbidden())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockAysResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

    @Test
    void givenValidAssignmentCancelRequest_whenAssignmentCanceled_thenReturnNothing() throws Exception {

        // Initialize
        InstitutionEntity mockInstitutionEntity = new InstitutionEntityBuilder()
                .withValidFields()
                .build();
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
                .withUserId(mockUserEntity.getId())
                .withInstitution(null)
                .withUserId(mockUserEntity.getId())
                .withUser(null)
                .build();
        this.initialize(mockInstitutionEntity, mockUserEntity, mockAssignmentEntity);

        // Given
        AssignmentCancelRequest mockCancelRequest = new AssignmentCancelRequestBuilder()
                .withValidFields()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/assignment/cancel");
        AysResponse<Void> mockAysResponse = AysResponse.SUCCESS;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, mockUserToken.getAccessToken(), mockCancelRequest))
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
        Assertions.assertEquals(UserSupportStatus.BUSY, userEntity.get().getSupportStatus());

        Optional<AssignmentEntity> assignmentEntity = assignmentRepository.findById(mockAssignmentEntity.getId());
        Assertions.assertTrue(assignmentEntity.isPresent());
        Assertions.assertEquals(AssignmentStatus.AVAILABLE, assignmentEntity.get().getStatus());
    }

    @Test
    void givenValidAssignmentCancelRequest_whenUserUnauthorizedForCanceling_thenReturnAccessDeniedException() throws Exception {
        // Given
        AssignmentCancelRequest mockCancelRequest = new AssignmentCancelRequestBuilder()
                .withValidFields()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/assignment/cancel");
        AysResponse<AysError> mockAysResponse = AysResponseBuilder.FORBIDDEN;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, adminUserToken.getAccessToken(), mockCancelRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isForbidden())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockAysResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());
    }

    @Test
    void givenValidAssignmentIdAndAssignmentUpdateRequest_whenAssignmentUpdated_thenReturnAysResponseOfSuccess() throws Exception {

        // Initialize
        Optional<InstitutionEntity> mockInstitutionEntity = institutionRepository
                .findById(AysValidTestData.Institution.ID);
        AssignmentEntity mockAssignmentEntity = new AssignmentEntityBuilder()
                .withValidFields()
                .withStatus(AssignmentStatus.AVAILABLE)
                .withInstitutionId(mockInstitutionEntity.get().getId())
                .withInstitution(mockInstitutionEntity.get())
                .withUserId(null)
                .withUser(null)
                .build();
        this.initialize(mockAssignmentEntity);

        // Given
        String assignmentId = mockAssignmentEntity.getId();
        AssignmentUpdateRequest mockUpdateRequest = new AssignmentUpdateRequestBuilder()
                .withValidFields()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/assignment/".concat(assignmentId));
        AysResponse<Void> mockAysResponse = AysResponse.SUCCESS;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .put(endpoint, adminUserToken.getAccessToken(), mockUpdateRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time().isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus().value(mockAysResponse.getHttpStatus().getReasonPhrase()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess().value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response().doesNotExist());
    }

    @Test
    void givenValidAssignmentIdAndAssignmentUpdateRequest_whenUserUnauthorizedForUpdating_thenThrowAccessDeniedException() throws Exception {

        // Given
        String assignmentId = AysRandomUtil.generateUUID();
        AssignmentUpdateRequest mockUpdateRequest = new AssignmentUpdateRequestBuilder()
                .withValidFields()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/assignment/".concat(assignmentId));
        AysResponse<AysError> mockAysResponse = AysResponseBuilder.FORBIDDEN;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .put(endpoint, userToken.getAccessToken(), mockUpdateRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isForbidden())
                .andExpect(AysMockResultMatchersBuilders.time().isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus().value(mockAysResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess().value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response().doesNotExist());
    }

    @Test
    void givenValidAssignmentId_whenAssignmentDeleted_thenReturnAysResponseOfSuccess() throws Exception {

        // Initialize
        Optional<InstitutionEntity> mockInstitutionEntity = institutionRepository
                .findById(AysValidTestData.Institution.ID);
        AssignmentEntity mockAssignmentEntity = new AssignmentEntityBuilder()
                .withValidFields()
                .withStatus(AssignmentStatus.AVAILABLE)
                .withInstitutionId(mockInstitutionEntity.get().getId())
                .withInstitution(mockInstitutionEntity.get())
                .withUserId(null)
                .withUser(null)
                .build();
        this.initialize(mockAssignmentEntity);

        // Given
        String assignmentId = mockAssignmentEntity.getId();

        // Then
        String endpoint = BASE_PATH.concat("/assignment/".concat(assignmentId));
        AysResponse<Void> mockAysResponse = AysResponse.SUCCESS;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .delete(endpoint, adminUserToken.getAccessToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time().isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus().value(mockAysResponse.getHttpStatus()
                        .getReasonPhrase()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess().value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response().doesNotExist());

        // Verify
        Optional<AssignmentEntity> assignmentEntity = assignmentRepository.findById(mockAssignmentEntity.getId());
        Assertions.assertTrue(assignmentEntity.isEmpty());
    }

    @Test
    void givenValidAssignmentId_whenUserUnauthorizedForDeleting_thenThrowAccessDeniedException() throws Exception {

        // Given
        String assignmentId = AysRandomUtil.generateUUID();

        // Then
        String endpoint = BASE_PATH.concat("/assignment/".concat(assignmentId));
        AysResponse<AysError> mockAysResponse = AysResponseBuilder.FORBIDDEN;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .delete(endpoint, userToken.getAccessToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isForbidden())
                .andExpect(AysMockResultMatchersBuilders.time().isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus().value(mockAysResponse.getHttpStatus()
                        .name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess().value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response().doesNotExist());
    }

    @Test
    void whenUserHasAssignmentWithValidStatus_thenReturnAssignmentSummaryResponse() throws Exception {

        // Initialize
        InstitutionEntity mockInstitutionEntity = new InstitutionEntityBuilder()
                .withValidFields()
                .build();
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
                .withUserId(mockUserEntity.getId())
                .withInstitution(null)
                .withUserId(mockUserEntity.getId())
                .withUser(null)
                .build();
        this.initialize(mockInstitutionEntity, mockUserEntity, mockAssignmentEntity);

        // When
        Assignment assignment = new AssignmentBuilder()
                .withStatus(AssignmentStatus.RESERVED).build();

        // Then
        String endpoint = BASE_PATH.concat("/assignment/summary");
        AssignmentSummaryResponse mockResponse = assignmentToAssignmentSummaryResponseMapper.map(assignment);
        AysResponse<AssignmentSummaryResponse> mockAysResponse = AysResponse.successOf(mockResponse);
        mockMvc.perform(AysMockMvcRequestBuilders
                        .get(endpoint, mockUserToken.getAccessToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time().isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus().value(mockAysResponse.getHttpStatus()
                        .getReasonPhrase()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess().value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response().isNotEmpty());
    }

    @Test
    void whenUserUnauthorizedForGettingAssignmentSummary_thenThrowAccessDeniedException() throws Exception {

        // Then
        String endpoint = BASE_PATH.concat("/assignment/summary");
        AysResponse<AysError> mockAysResponse = AysResponseBuilder.FORBIDDEN;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .get(endpoint, adminUserToken.getAccessToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isForbidden())
                .andExpect(AysMockResultMatchersBuilders.time().isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus().value(mockAysResponse.getHttpStatus()
                        .name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess().value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response().doesNotExist());
    }
}
