package com.ays.assignment.controller;

import com.ays.AbstractRestControllerTest;
import com.ays.assignment.model.Assignment;
import com.ays.assignment.model.AssignmentBuilder;
import com.ays.assignment.model.dto.request.*;
import com.ays.assignment.model.dto.response.AssignmentResponse;
import com.ays.assignment.model.dto.response.AssignmentSearchResponse;
import com.ays.assignment.model.dto.response.AssignmentSummaryResponse;
import com.ays.assignment.model.dto.response.AssignmentUserResponse;
import com.ays.assignment.model.dto.response.AssignmentsResponse;
import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.entity.AssignmentEntityBuilder;
import com.ays.assignment.model.mapper.AssignmentEntityToAssignmentMapper;
import com.ays.assignment.model.mapper.AssignmentToAssignmentResponseMapper;
import com.ays.assignment.model.mapper.AssignmentToAssignmentSearchResponseMapper;
import com.ays.assignment.model.mapper.AssignmentToAssignmentSummaryResponseMapper;
import com.ays.assignment.model.mapper.AssignmentToAssignmentUserResponseMapper;
import com.ays.assignment.model.mapper.AssignmentToAssignmentsResponseMapper;
import com.ays.assignment.service.AssignmentConcludeService;
import com.ays.assignment.service.AssignmentSaveService;
import com.ays.assignment.service.AssignmentSearchService;
import com.ays.assignment.service.AssignmentService;
import com.ays.common.model.AysPage;
import com.ays.common.model.AysPhoneNumberBuilder;
import com.ays.common.model.dto.response.AysPageResponse;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.model.dto.response.AysResponseBuilder;
import com.ays.common.util.AysRandomUtil;
import com.ays.common.util.exception.model.AysError;
import com.ays.common.util.exception.model.AysErrorBuilder;
import com.ays.util.AysMockMvcRequestBuilders;
import com.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;


class AssignmentControllerTest extends AbstractRestControllerTest {

    @MockBean
    private AssignmentSaveService assignmentSaveService;

    @MockBean
    private AssignmentService assignmentService;

    @MockBean
    private AssignmentSearchService assignmentSearchService;

    @MockBean
    private AssignmentConcludeService assignmentConcludeService;


    private final AssignmentEntityToAssignmentMapper assignmentEntityToAssignmentMapper = AssignmentEntityToAssignmentMapper.initialize();
    private final AssignmentToAssignmentSearchResponseMapper assignmentToAssignmentSearchResponseMapper = AssignmentToAssignmentSearchResponseMapper.initialize();
    private final AssignmentToAssignmentResponseMapper assignmentToAssignmentResponseMapper = AssignmentToAssignmentResponseMapper.initialize();
    private final AssignmentToAssignmentsResponseMapper assignmentToAssignmentsResponseMapper = AssignmentToAssignmentsResponseMapper.initialize();
    private final AssignmentToAssignmentSummaryResponseMapper assignmentToAssignmentSummaryResponseMapper = AssignmentToAssignmentSummaryResponseMapper.initialize();
    private final AssignmentToAssignmentUserResponseMapper assignmentToAssignmentUserResponseMapper = AssignmentToAssignmentUserResponseMapper.initialize();


    private static final String BASE_PATH = "/api/v1";


    @Test
    void givenValidAssignmentSaveRequest_whenAssignmentSaved_thenReturnAssignmentSavedResponse() throws Exception {
        // Given
        AssignmentSaveRequest mockAssignmentSaveRequest = new AssignmentSaveRequestBuilder()
                .withPhoneNumber(new AysPhoneNumberBuilder().withValidFields().build())
                .build();

        // When
        Mockito.doNothing().when(assignmentSaveService).saveAssignment(Mockito.any(AssignmentSaveRequest.class));

        // Then
        String endpoint = BASE_PATH.concat("/assignment");

        AysResponse<Void> mockAysResponse = AysResponse.SUCCESS;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, mockAdminUserToken.getAccessToken(), mockAssignmentSaveRequest))
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

        Mockito.verify(assignmentSaveService, Mockito.times(1))
                .saveAssignment(Mockito.any(AssignmentSaveRequest.class));

    }

    @Test
    void givenInvalidAssignmentSaveRequestWithNameContainingNumber_whenNameIsNotValid_thenReturnValidationError() throws Exception {
        // Given
        String invalidName = "John 1234";
        AssignmentSaveRequest mockRequest = new AssignmentSaveRequestBuilder()
                .withValidFields()
                .withFirstName(invalidName)
                .withLastName(invalidName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/assignment");

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

        Mockito.verify(assignmentSaveService, Mockito.never())
                .saveAssignment(Mockito.any(AssignmentSaveRequest.class));
    }

    @Test
    void givenInvalidAssignmentSaveRequestWithNameContainingForbiddenSpecialChars_whenNameIsNotValid_thenReturnValidationError() throws Exception {
        // Given
        String invalidName = "John *^%$#";
        AssignmentSaveRequest mockRequest = new AssignmentSaveRequestBuilder()
                .withValidFields()
                .withFirstName(invalidName)
                .withLastName(invalidName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/assignment");

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

        Mockito.verify(assignmentSaveService, Mockito.never())
                .saveAssignment(Mockito.any(AssignmentSaveRequest.class));
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
                .post(endpoint, mockUserToken.getAccessToken(), mockAssignmentSaveRequest);

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

        // When
        Assignment mockAssignment = new AssignmentBuilder().build();
        Mockito.when(assignmentService.getUserAssignment())
                .thenReturn(mockAssignment);


        // Then
        String endpoint = BASE_PATH.concat("/assignment");
        AssignmentUserResponse mockAssignmentUserResponse = assignmentToAssignmentUserResponseMapper.map(mockAssignment);
        AysResponse<AssignmentUserResponse> mockAysResponse = AysResponse.successOf(mockAssignmentUserResponse);
        mockMvc.perform(AysMockMvcRequestBuilders
                        .get(endpoint, mockUserToken.getAccessToken()))
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

        Mockito.verify(assignmentService, Mockito.times(1))
                .getUserAssignment();

    }


    @Test
    void whenUnauthorizedForGettingUserAssignment_thenReturnAccessDeniedException() throws Exception {

        // Then
        String endpoint = BASE_PATH.concat("/assignment");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockAdminUserToken.getAccessToken());

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
        String mockAssignmentId = AysRandomUtil.generateUUID();

        // When
        Assignment mockAssignment = new AssignmentBuilder().build();
        Mockito.when(assignmentService.getAssignmentById(mockAssignmentId))
                .thenReturn(mockAssignment);


        // Then
        String endpoint = BASE_PATH.concat("/assignment/").concat(mockAssignmentId);
        AssignmentResponse mockAssignmentResponse = assignmentToAssignmentResponseMapper.map(mockAssignment);
        AysResponse<AssignmentResponse> mockAysResponse = AysResponse.successOf(mockAssignmentResponse);
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

        Mockito.verify(assignmentService, Mockito.times(1))
                .getAssignmentById(mockAssignmentId);

    }


    @Test
    void givenValidAssignmentId_whenUnauthorizedForGettingAssignmentById_thenReturnAccessDeniedException() throws Exception {

        // Given
        String mockAssignmentId = AysRandomUtil.generateUUID();

        // Then
        String endpoint = BASE_PATH.concat("/assignment/".concat(mockAssignmentId));
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
    void givenValidAssignmentSearchRequest_whenAssignmentSearched_thenReturnAssignmentSearchResponse() throws Exception {
        // Given
        AssignmentSearchRequest mockSearchRequest = new AssignmentSearchRequestBuilder()
                .withValidFields()
                .build();

        // When
        Assignment mockAssignment = new AssignmentBuilder().build();
        Mockito.when(assignmentSearchService.searchAssignment(Mockito.any(AssignmentSearchRequest.class)))
                .thenReturn(mockAssignment);

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

        Mockito.verify(assignmentSearchService, Mockito.times(1))
                .searchAssignment(Mockito.any(AssignmentSearchRequest.class));
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
                        .post(endpoint, mockAdminUserToken.getAccessToken(), mockAssignmentSearchRequest))
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
    void givenValidAssignmentListRequest_whenAssignmentsFound_thenReturnAysPageResponseOfAssignmentsResponse() throws Exception {

        // Given
        AssignmentListRequest mockListRequest = new AssignmentListRequestBuilder().withValidValues().build();

        // When
        List<AssignmentEntity> mockAssignmentEntities = AssignmentEntityBuilder.generateValidAssignmentEntities(1);
        Page<AssignmentEntity> mockPageAssignmentEntities = new PageImpl<>(mockAssignmentEntities);
        List<Assignment> mockAssignments = assignmentEntityToAssignmentMapper.map(mockAssignmentEntities);
        AysPage<Assignment> mockAysPageOfAssignments = AysPage
                .of(mockListRequest.getFilter(), mockPageAssignmentEntities, mockAssignments);

        Mockito.when(assignmentService.getAssignments(Mockito.any(AssignmentListRequest.class)))
                .thenReturn(mockAysPageOfAssignments);

        // Then
        String endpoint = BASE_PATH.concat("/assignments");
        List<AssignmentsResponse> mockAssignmentsResponse = assignmentToAssignmentsResponseMapper.map(mockAssignments);
        AysPageResponse<AssignmentsResponse> pageOfAssignmentsResponse = AysPageResponse.<AssignmentsResponse>builder()
                .of(mockAysPageOfAssignments)
                .content(mockAssignmentsResponse)
                .build();

        AysResponse<AysPageResponse<AssignmentsResponse>> mockAysResponse = AysResponse.successOf(pageOfAssignmentsResponse);
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, mockAdminUserToken.getAccessToken(), mockListRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time().isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus().value(mockAysResponse.getHttpStatus().getReasonPhrase()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess().value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response().isNotEmpty());

        Mockito.verify(assignmentService, Mockito.times(1))
                .getAssignments(Mockito.any(AssignmentListRequest.class));
    }

    @Test
    void givenValidAssignmentListRequest_whenUserUnauthorizedForListing_thenReturnAccessDeniedException() throws Exception {
        // Given
        AssignmentListRequest mockListRequest = new AssignmentListRequestBuilder().withValidValues().build();

        // Then
        String endpoint = BASE_PATH.concat("/assignments");
        AysResponse<AysError> mockResponse = AysResponseBuilder.FORBIDDEN;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, mockUserToken.getAccessToken(), mockListRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isForbidden())
                .andExpect(AysMockResultMatchersBuilders.time().isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus().value(mockResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess().value(mockResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response().doesNotExist());
    }

    @Test
    void givenValidAssignmentIdAndAssignmentUpdateRequest_whenAssignmentUpdated_thenReturnAysResponseOfSuccess() throws Exception {
        // Given
        String mockAssignmentId = AysRandomUtil.generateUUID();
        AssignmentUpdateRequest mockUpdateRequest = new AssignmentUpdateRequestBuilder()
                .withValidFields()
                .build();

        // When
        Mockito.doNothing().when(assignmentService).updateAssignment(mockAssignmentId, mockUpdateRequest);

        // Then
        String endpoint = BASE_PATH.concat("/assignment/".concat(mockAssignmentId));
        AysResponse<Void> mockAysResponse = AysResponse.SUCCESS;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .put(endpoint, mockAdminUserToken.getAccessToken(), mockUpdateRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time().isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus().value(mockAysResponse.getHttpStatus().getReasonPhrase()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess().value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response().doesNotExist());

        Mockito.verify(assignmentService, Mockito.times(1)).updateAssignment(
                Mockito.anyString(), Mockito.any(AssignmentUpdateRequest.class));
    }

    @Test
    void givenInvalidAssignmentUpdateRequestWithNameContainingNumber_whenNameIsNotValid_thenReturnValidationError() throws Exception {
        // Given
        String mockAssignmentId = AysRandomUtil.generateUUID();
        String invalidName = "John 1234";
        AssignmentUpdateRequest mockRequest = new AssignmentUpdateRequestBuilder()
                .withValidFields()
                .withFirstName(invalidName)
                .withLastName(invalidName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/assignment/".concat(mockAssignmentId));

        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .put(endpoint, mockAdminUserToken.getAccessToken(), mockRequest))
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

        Mockito.verify(assignmentService, Mockito.never())
                .updateAssignment(Mockito.any(String.class), Mockito.any(AssignmentUpdateRequest.class));
    }

    @Test
    void givenInvalidAssignmentUpdateRequestWithNameContainingForbiddenSpecialChars_whenNameIsNotValid_thenReturnValidationError() throws Exception {
        // Given
        String mockAssignmentId = AysRandomUtil.generateUUID();
        String invalidName = "John *^%$#";
        AssignmentUpdateRequest mockRequest = new AssignmentUpdateRequestBuilder()
                .withValidFields()
                .withFirstName(invalidName)
                .withLastName(invalidName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/assignment/".concat(mockAssignmentId));

        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .put(endpoint, mockAdminUserToken.getAccessToken(), mockRequest))
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

        Mockito.verify(assignmentService, Mockito.never())
                .updateAssignment(Mockito.any(String.class), Mockito.any(AssignmentUpdateRequest.class));
    }

    @Test
    void givenValidAssignmentIdAndAssignmentUpdateRequest_whenUserUnauthorizedForUpdating_thenThrowAccessDeniedException() throws Exception {

        // Given
        String mockAssignmentId = AysRandomUtil.generateUUID();
        AssignmentUpdateRequest mockUpdateRequest = new AssignmentUpdateRequestBuilder()
                .withValidFields()
                .build();

        // When
        Mockito.doNothing().when(assignmentService).updateAssignment(mockAssignmentId, mockUpdateRequest);

        // Then
        String endpoint = BASE_PATH.concat("/assignment/".concat(mockAssignmentId));
        AysResponse<AysError> mockAysResponse = AysResponseBuilder.FORBIDDEN;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .put(endpoint, mockUserToken.getAccessToken(), mockUpdateRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isForbidden())
                .andExpect(AysMockResultMatchersBuilders.time().isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus().value(mockAysResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess().value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response().doesNotExist());

        Mockito.verifyNoInteractions(assignmentService);
    }

    @Test
    void givenValidAssignmentId_whenAssignmentDeleted_thenReturnAysResponseOfSuccess() throws Exception {

        // Given
        String mockAssignmentId = AysRandomUtil.generateUUID();

        // When
        Mockito.doNothing().when(assignmentService).deleteAssignment(mockAssignmentId);

        // Then
        String endpoint = BASE_PATH.concat("/assignment/".concat(mockAssignmentId));
        AysResponse<Void> mockAysResponse = AysResponse.SUCCESS;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .delete(endpoint, mockAdminUserToken.getAccessToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time().isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus().value(mockAysResponse.getHttpStatus()
                        .getReasonPhrase()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess().value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response().doesNotExist());

        Mockito.verify(assignmentService, Mockito.times(1)).deleteAssignment(mockAssignmentId);
    }

    @Test
    void givenValidAssignmentId_whenUserUnauthorizedForDeleting_thenThrowAccessDeniedException() throws Exception {

        // Given
        String mockAssignmentId = AysRandomUtil.generateUUID();

        // When
        Mockito.doNothing().when(assignmentService).deleteAssignment(mockAssignmentId);

        // Then
        String endpoint = BASE_PATH.concat("/assignment/".concat(mockAssignmentId));
        AysResponse<AysError> mockAysResponse = AysResponseBuilder.FORBIDDEN;
        mockMvc.perform(AysMockMvcRequestBuilders
                        .delete(endpoint, mockUserToken.getAccessToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isForbidden())
                .andExpect(AysMockResultMatchersBuilders.time().isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus().value(mockAysResponse.getHttpStatus()
                        .name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess().value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response().doesNotExist());

        Mockito.verifyNoInteractions(assignmentService);
    }

    @Test
    void whenAssignmentApproved_thenReturnNothing() throws Exception {
        // When
        Mockito.doNothing().when(assignmentConcludeService).approve();

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

        Mockito.verify(assignmentConcludeService, Mockito.times(1))
                .approve();
    }

    @Test
    void givenVoid_whenUserUnauthorizedForApproving_thenReturnAccessDeniedException() throws Exception {

        // Then
        String endpoint = BASE_PATH.concat("/assignment/approve");
        AysResponse<AysError> mockAysResponse = AysResponseBuilder.FORBIDDEN;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, mockAdminUserToken.getAccessToken()))
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

        Mockito.verify(assignmentConcludeService, Mockito.times(0))
                .approve();
    }

    @Test
    void whenAssignmentStarted_thenReturnNothing() throws Exception {
        // When
        Mockito.doNothing().when(assignmentConcludeService).start();

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

        Mockito.verify(assignmentConcludeService, Mockito.times(1))
                .start();
    }

    @Test
    void givenVoid_whenUserUnauthorizedForStarting_thenReturnAccessDeniedException() throws Exception {

        // Then
        String endpoint = BASE_PATH.concat("/assignment/start");
        AysResponse<AysError> mockAysResponse = AysResponseBuilder.FORBIDDEN;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, mockAdminUserToken.getAccessToken()))
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

        Mockito.verify(assignmentConcludeService, Mockito.times(0))
                .start();
    }

    @Test
    void whenAssignmentRejected_thenReturnNothing() throws Exception {
        // When
        Mockito.doNothing().when(assignmentConcludeService).reject();

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

        Mockito.verify(assignmentConcludeService, Mockito.times(1))
                .reject();
    }

    @Test
    void givenVoid_whenUserUnauthorizedForRejecting_thenReturnAccessDeniedException() throws Exception {

        // Then
        String endpoint = BASE_PATH.concat("/assignment/reject");
        AysResponse<AysError> mockAysResponse = AysResponseBuilder.FORBIDDEN;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, mockAdminUserToken.getAccessToken()))
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

        Mockito.verify(assignmentConcludeService, Mockito.times(0))
                .reject();
    }

    @Test
    void whenAssignmentCompleted_thenReturnNothing() throws Exception {
        // When
        Mockito.doNothing().when(assignmentConcludeService).complete();

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

        Mockito.verify(assignmentConcludeService, Mockito.times(1))
                .complete();
    }

    @Test
    void givenVoid_whenUserUnauthorizedForCompleting_thenReturnAccessDeniedException() throws Exception {

        // Then
        String endpoint = BASE_PATH.concat("/assignment/complete");
        AysResponse<AysError> mockAysResponse = AysResponseBuilder.FORBIDDEN;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, mockAdminUserToken.getAccessToken()))
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

        Mockito.verify(assignmentConcludeService, Mockito.times(0))
                .complete();
    }

    @Test
    void givenValidAssignmentCancelRequest_whenAssignmentCanceled_thenReturnNothing() throws Exception {
        // Given
        AssignmentCancelRequest mockCancelRequest = new AssignmentCancelRequestBuilder()
                .withValidFields()
                .build();

        // When
        Mockito.doNothing().when(assignmentConcludeService).cancel();

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

        Mockito.verify(assignmentConcludeService, Mockito.times(1))
                .cancel();
    }

    @Test
    void givenInvalidAssignmentCancelRequest_whenReasonHasLessThan40Chars_thenReturnValidationError() throws Exception {
        // Given
        AssignmentCancelRequest mockCancelRequest = new AssignmentCancelRequestBuilder()
                .withReason("invalid-reason")
                .build();

        // When
        Mockito.doNothing().when(assignmentConcludeService).cancel();

        // Then
        String endpoint = BASE_PATH.concat("/assignment/cancel");
        AysError mockAysResponse = AysErrorBuilder.VALIDATION_ERROR;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, mockUserToken.getAccessToken(), mockCancelRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockAysResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockAysResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        Mockito.verify(assignmentConcludeService, Mockito.never())
                .cancel();
    }

    @Test
    void givenValidAssignmentCancelRequest_whenUserUnauthorizedForCanceling_thenReturnAccessDeniedException() throws Exception {
        // Given
        AssignmentCancelRequest mockCancelRequest = new AssignmentCancelRequestBuilder()
                .withValidFields()
                .build();

        // When
        Mockito.doNothing().when(assignmentConcludeService).cancel();

        // Then
        String endpoint = BASE_PATH.concat("/assignment/cancel");
        AysError mockAysResponse = AysErrorBuilder.FORBIDDEN;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, mockAdminUserToken.getAccessToken(), mockCancelRequest))
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

        Mockito.verify(assignmentConcludeService, Mockito.never())
                .cancel();
    }

    @Test
    void whenUserHasAssignmentWithValidStatus_thenReturnAssignmentSummaryResponse() throws Exception {

        // When
        Assignment mockAssignment = new AssignmentBuilder().build();
        Mockito.when(assignmentService.getAssignmentSummary())
                .thenReturn(mockAssignment);

        // Then
        String endpoint = BASE_PATH.concat("/assignment/summary");
        AssignmentSummaryResponse mockResponse = assignmentToAssignmentSummaryResponseMapper.map(mockAssignment);
        AysResponse<AssignmentSummaryResponse> mockAysResponse = AysResponse.successOf(mockResponse);
        mockMvc.perform(AysMockMvcRequestBuilders
                        .get(endpoint, mockUserToken.getAccessToken()))
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

        Mockito.verify(assignmentService, Mockito.times(1))
                .getAssignmentSummary();
    }

    @Test
    void whenUserUnauthorizedForGettingAssignmentSummary_thenReturnAccessDeniedException() throws Exception {

        // When
        Assignment mockAssignment = new AssignmentBuilder().build();
        Mockito.when(assignmentService.getAssignmentSummary())
                .thenReturn(mockAssignment);

        // Then
        String endpoint = BASE_PATH.concat("/assignment/summary");
        AysResponse<AysError> mockAysResponse = AysResponseBuilder.FORBIDDEN;

        mockMvc.perform(AysMockMvcRequestBuilders
                        .get(endpoint, mockAdminUserToken.getAccessToken()))
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

        Mockito.verifyNoInteractions(assignmentService);
    }
}
