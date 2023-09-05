package com.ays.assignment.controller;

import com.ays.AbstractSystemTest;
import com.ays.assignment.model.Assignment;
import com.ays.assignment.model.AssignmentBuilder;
import com.ays.assignment.model.dto.request.AssignmentSaveRequest;
import com.ays.assignment.model.dto.request.AssignmentSaveRequestBuilder;
import com.ays.assignment.model.dto.request.AssignmentSearchRequest;
import com.ays.assignment.model.dto.request.AssignmentSearchRequestBuilder;
import com.ays.assignment.model.dto.response.AssignmentResponse;
import com.ays.assignment.model.dto.response.AssignmentSearchResponse;
import com.ays.assignment.model.mapper.AssignmentToAssignmentResponseMapper;
import com.ays.assignment.model.mapper.AssignmentToAssignmentSearchResponseMapper;
import com.ays.common.model.AysPhoneNumberBuilder;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.model.dto.response.AysResponseBuilder;
import com.ays.common.util.AysRandomUtil;
import com.ays.common.util.exception.model.AysError;
import com.ays.util.AysMockMvcRequestBuilders;
import com.ays.util.AysMockResultMatchersBuilders;
import com.ays.util.AysTestData;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

class AssignmentSystemTest extends AbstractSystemTest {

    private static final AssignmentToAssignmentResponseMapper ASSIGNMENT_TO_ASSIGNMENT_RESPONSE_MAPPER = AssignmentToAssignmentResponseMapper.initialize();
    private final AssignmentToAssignmentSearchResponseMapper assignmentToAssignmentSearchResponseMapper = AssignmentToAssignmentSearchResponseMapper.initialize();


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

}
