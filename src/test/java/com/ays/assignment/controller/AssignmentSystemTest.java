package com.ays.assignment.controller;

import com.ays.AbstractSystemTest;
import com.ays.assignment.model.Assignment;
import com.ays.assignment.model.AssignmentBuilder;
import com.ays.assignment.model.dto.request.*;
import com.ays.assignment.model.dto.response.AssignmentResponse;
import com.ays.assignment.model.dto.response.AssignmentSearchResponse;
import com.ays.assignment.model.dto.response.AssignmentSummaryResponse;
import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.entity.AssignmentEntityBuilder;
import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.assignment.model.mapper.AssignmentEntityToAssignmentMapper;
import com.ays.assignment.model.mapper.AssignmentToAssignmentResponseMapper;
import com.ays.assignment.model.mapper.AssignmentToAssignmentSearchResponseMapper;
import com.ays.assignment.model.mapper.AssignmentToAssignmentSummaryResponseMapper;
import com.ays.common.model.AysPage;
import com.ays.common.model.AysPhoneNumberBuilder;
import com.ays.common.model.dto.response.AysPageResponse;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.model.dto.response.AysResponseBuilder;
import com.ays.common.util.AysRandomUtil;
import com.ays.common.util.exception.model.AysError;
import com.ays.util.AysMockMvcRequestBuilders;
import com.ays.util.AysMockResultMatchersBuilders;
import com.ays.util.AysTestData;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

class AssignmentSystemTest extends AbstractSystemTest {

    private final AssignmentToAssignmentResponseMapper ASSIGNMENT_TO_ASSIGNMENT_RESPONSE_MAPPER = AssignmentToAssignmentResponseMapper.initialize();
    private final AssignmentToAssignmentSearchResponseMapper assignmentToAssignmentSearchResponseMapper = AssignmentToAssignmentSearchResponseMapper.initialize();
    private final AssignmentEntityToAssignmentMapper ASSIGNMENT_ENTITY_TO_ASSIGNMENT_MAPPER = AssignmentEntityToAssignmentMapper.initialize();
    private final AssignmentToAssignmentSummaryResponseMapper ASSIGNMENT_TO_ASSIGNMENT_SUMMARY_RESPONSE_MAPPER = AssignmentToAssignmentSummaryResponseMapper.initialize();

    private static final String BASE_PATH = "/api/v1";

    @Test
    void givenValidAssignmentSaveRequest_whenAssignmentSaved_thenReturnAssignmentSavedResponse() throws Exception {
        // Given
        AssignmentSaveRequest mockAssignmentSaveRequest = new AssignmentSaveRequestBuilder()
                .withPhoneNumber(new AysPhoneNumberBuilder().withValidFields().build())
                .build();


        // Then
        String endpoint = BASE_PATH.concat("/assignment");

        AysResponse<Void> mockAysResponse = AysResponse.SUCCESS;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, adminUserTokenOne.getAccessToken(), mockAssignmentSaveRequest))
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
                .withPhoneNumber(new AysPhoneNumberBuilder().withValidFields().build())
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/assignment");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, userTokenOne.getAccessToken(), mockAssignmentSaveRequest);

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

        // Given
        String mockAssignmentId = AysTestData.Assignment.VALID_ID_ONE;

        // When
        Assignment mockAssignment = new AssignmentBuilder()
                .withId(mockAssignmentId)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/assignment/").concat(mockAssignmentId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, adminUserTokenOne.getAccessToken());

        AssignmentResponse mockAssignmentResponse = ASSIGNMENT_TO_ASSIGNMENT_RESPONSE_MAPPER.map(mockAssignment);
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
    void givenValidAssignmentSearchRequest_whenAssignmentSearched_thenReturnAssignmentSearchResponse() throws Exception {
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
                        .post(endpoint, userTokenFour.getAccessToken(), mockSearchRequest))
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
                        .post(endpoint, adminUserTokenOne.getAccessToken(), mockAssignmentSearchRequest))
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

        List<AssignmentEntity> assignmentEntities = AssignmentEntityBuilder.generateValidAssignmentEntities(1);
        Page<AssignmentEntity> pageOfAssignmentEntities = new PageImpl<>(assignmentEntities);
        List<Assignment> assignments = ASSIGNMENT_ENTITY_TO_ASSIGNMENT_MAPPER.map(assignmentEntities);
        AysPage<Assignment> aysPageOfAssignments = AysPage.of(pageOfAssignmentEntities, assignments);
        AysPageResponse<Assignment> aysPageResponseOfAssignments = AysPageResponse.<Assignment>builder()
                .of(aysPageOfAssignments).build();

        // Then
        String endpoint = BASE_PATH.concat("/assignments");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, adminUserTokenOne.getAccessToken(), listRequest);

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
                .post(endpoint, userTokenOne.getAccessToken(), listRequest);

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
    void givenNothing_whenAssignmentApproved_thenReturnNothing() throws Exception {
        // Then
        String endpoint = BASE_PATH.concat("/assignment/approve");
        AysResponse<Void> mockAysResponse = AysResponse.SUCCESS;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, userTokenFive.getAccessToken()))
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
    void givenNothing_whenUserUnauthorizedForApproving_thenReturnAccessDeniedException() throws Exception {
        // Then
        String endpoint = BASE_PATH.concat("/assignment/approve");
        AysResponse<AysError> mockAysResponse = AysResponseBuilder.FORBIDDEN;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, adminUserTokenOne.getAccessToken()))
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
    void givenNothing_whenAssignmentStarted_thenReturnNothing() throws Exception {
        // Then
        String endpoint = BASE_PATH.concat("/assignment/start");
        AysResponse<Void> mockAysResponse = AysResponse.SUCCESS;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, userTokenFive.getAccessToken()))
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
    void givenNothing_whenUserUnauthorizedForStarting_thenReturnAccessDeniedException() throws Exception {
        // Then
        String endpoint = BASE_PATH.concat("/assignment/start");
        AysResponse<AysError> mockAysResponse = AysResponseBuilder.FORBIDDEN;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, adminUserTokenOne.getAccessToken()))
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
    void givenNothing_whenAssignmentRejected_thenReturnNothing() throws Exception {
        // Then
        String endpoint = BASE_PATH.concat("/assignment/reject");
        AysResponse<Void> mockAysResponse = AysResponse.SUCCESS;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, userTokenFour.getAccessToken()))
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
    void givenNothing_whenUserUnauthorizedForRejecting_thenReturnAccessDeniedException() throws Exception {
        // Then
        String endpoint = BASE_PATH.concat("/assignment/reject");
        AysResponse<AysError> mockAysResponse = AysResponseBuilder.FORBIDDEN;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, adminUserTokenOne.getAccessToken()))
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
    void givenNothing_whenAssignmentCompleted_thenReturnNothing() throws Exception {
        // Then
        String endpoint = BASE_PATH.concat("/assignment/complete");
        AysResponse<Void> mockAysResponse = AysResponse.SUCCESS;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, userTokenFive.getAccessToken()))
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
    void givenNothing_whenUserUnauthorizedForCompleting_thenReturnAccessDeniedException() throws Exception {
        // Then
        String endpoint = BASE_PATH.concat("/assignment/complete");
        AysResponse<AysError> mockAysResponse = AysResponseBuilder.FORBIDDEN;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, adminUserTokenOne.getAccessToken()))
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

        // Given
        String assignmentId = AysTestData.Assignment.VALID_ID_THREE;
        AssignmentUpdateRequest mockUpdateRequest = new AssignmentUpdateRequestBuilder()
                .withValidFields()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/assignment/".concat(assignmentId));
        AysResponse<Void> mockAysResponse = AysResponse.SUCCESS;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .put(endpoint, adminUserTokenTwo.getAccessToken(), mockUpdateRequest))
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
        String assignmentId = AysTestData.Assignment.VALID_ID_ONE;
        AssignmentUpdateRequest mockUpdateRequest = new AssignmentUpdateRequestBuilder()
                .withValidFields()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/assignment/".concat(assignmentId));
        AysResponse<AysError> mockAysResponse = AysResponseBuilder.FORBIDDEN;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .put(endpoint, userTokenOne.getAccessToken(), mockUpdateRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isForbidden())
                .andExpect(AysMockResultMatchersBuilders.time().isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus().value(mockAysResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess().value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response().doesNotExist());
    }

    @Test
    void givenValidAssignmentId_whenAssignmentDeleted_thenReturnAysResponseOfSuccess() throws Exception {

        // Given
        String assignmentId = AysTestData.Assignment.VALID_ID_TWO;

        // Then
        String endpoint = BASE_PATH.concat("/assignment/".concat(assignmentId));
        AysResponse<Void> mockAysResponse = AysResponse.SUCCESS;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .delete(endpoint, adminUserTokenTwo.getAccessToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time().isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus().value(mockAysResponse.getHttpStatus()
                        .getReasonPhrase()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess().value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response().doesNotExist());
    }

    @Test
    void givenValidAssignmentId_whenUserUnauthorizedForDeleting_thenThrowAccessDeniedException() throws Exception {

        // Given
        String assignmentId = AysTestData.Assignment.VALID_ID_ONE;

        // Then
        String endpoint = BASE_PATH.concat("/assignment/".concat(assignmentId));
        AysResponse<AysError> mockAysResponse = AysResponseBuilder.FORBIDDEN;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .delete(endpoint, userTokenOne.getAccessToken()))
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

        // When
        Assignment assignment = new AssignmentBuilder().withStatus(AssignmentStatus.RESERVED).build();

        // Then
        String endpoint = BASE_PATH.concat("/assignment/summary");
        AssignmentSummaryResponse mockResponse = ASSIGNMENT_TO_ASSIGNMENT_SUMMARY_RESPONSE_MAPPER.map(assignment);
        AysResponse<AssignmentSummaryResponse> mockAysResponse = AysResponse.successOf(mockResponse);
        mockMvc.perform(AysMockMvcRequestBuilders
                        .get(endpoint, userTokenThree.getAccessToken()))
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
                        .get(endpoint, adminUserTokenTwo.getAccessToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isForbidden())
                .andExpect(AysMockResultMatchersBuilders.time().isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus().value(mockAysResponse.getHttpStatus()
                        .name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess().value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response().doesNotExist());
    }
}
