package com.ays.assignment.controller;

import com.ays.AbstractRestControllerTest;
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
import com.ays.common.model.dto.request.AysPhoneNumberRequestBuilder;
import com.ays.common.model.dto.response.AysPageResponse;
import com.ays.common.model.dto.response.AysResponse;
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
                .withPhoneNumber(new AysPhoneNumberRequestBuilder().withValidFields().build())
                .build();

        // When
        Mockito.doNothing()
                .when(assignmentSaveService)
                .saveAssignment(Mockito.any(AssignmentSaveRequest.class));

        // Then
        String endpoint = BASE_PATH.concat("/assignment");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminUserToken.getAccessToken(), mockAssignmentSaveRequest);

        AysResponse<Void> mockResponse = AysResponse.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
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
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminUserToken.getAccessToken(), mockRequest);

        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
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
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminUserToken.getAccessToken(), mockRequest);

        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(assignmentSaveService, Mockito.never())
                .saveAssignment(Mockito.any(AssignmentSaveRequest.class));
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
                .post(endpoint, mockUserToken.getAccessToken(), mockAssignmentSaveRequest);

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(assignmentSaveService, Mockito.never())
                .saveAssignment(Mockito.any(AssignmentSaveRequest.class));
    }


    @Test
    void whenUserAssignmentFound_thenReturnAssignmentUserResponse() throws Exception {

        // When
        Assignment mockAssignment = new AssignmentBuilder().build();
        Mockito.when(assignmentService.getUserAssignment())
                .thenReturn(mockAssignment);

        // Then
        String endpoint = BASE_PATH.concat("/assignment");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockUserToken.getAccessToken());

        AssignmentUserResponse mockAssignmentUserResponse = assignmentToAssignmentUserResponseMapper.map(mockAssignment);
        AysResponse<AssignmentUserResponse> mockResponse = AysResponse.successOf(mockAssignmentUserResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

        // Verify
        Mockito.verify(assignmentService, Mockito.times(1))
                .getUserAssignment();

    }

    @Test
    void whenUnauthorizedForGettingUserAssignment_thenReturnAccessDeniedException() throws Exception {

        // Then
        String endpoint = BASE_PATH.concat("/assignment");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockAdminUserToken.getAccessToken());

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(assignmentService, Mockito.never())
                .getUserAssignment();
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
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockAdminUserToken.getAccessToken());

        AssignmentResponse mockAssignmentResponse = assignmentToAssignmentResponseMapper.map(mockAssignment);
        AysResponse<AssignmentResponse> mockResponse = AysResponse.successOf(mockAssignmentResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

        // Verify
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

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(assignmentService, Mockito.never())
                .getAssignmentById(mockAssignmentId);
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
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken(), mockSearchRequest);

        AssignmentSearchResponse mockSearchResponse = assignmentToAssignmentSearchResponseMapper.map(mockAssignment);
        AysResponse<AssignmentSearchResponse> mockAysResponse = AysResponse.successOf(mockSearchResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockAysResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

        // Verify
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
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminUserToken.getAccessToken(), mockAssignmentSearchRequest);

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(assignmentSearchService, Mockito.never())
                .searchAssignment(Mockito.any(AssignmentSearchRequest.class));
    }

    @Test
    void givenValidAssignmentListRequest_whenAssignmentsFound_thenReturnAysPageResponseOfAssignmentsResponse() throws Exception {

        // Given
        AssignmentListRequest mockListRequest = new AssignmentListRequestBuilder().withValidValues().build();

        // When
        List<AssignmentEntity> mockAssignmentEntities = List.of(new AssignmentEntityBuilder().withValidFields().build());
        Page<AssignmentEntity> mockPageAssignmentEntities = new PageImpl<>(mockAssignmentEntities);
        List<Assignment> mockAssignments = assignmentEntityToAssignmentMapper.map(mockAssignmentEntities);
        AysPage<Assignment> mockAysPageOfAssignments = AysPage
                .of(mockListRequest.getFilter(), mockPageAssignmentEntities, mockAssignments);

        Mockito.when(assignmentService.getAssignments(Mockito.any(AssignmentListRequest.class)))
                .thenReturn(mockAysPageOfAssignments);

        // Then
        String endpoint = BASE_PATH.concat("/assignments");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminUserToken.getAccessToken(), mockListRequest);

        List<AssignmentsResponse> mockAssignmentsResponse = assignmentToAssignmentsResponseMapper.map(mockAssignments);
        AysPageResponse<AssignmentsResponse> pageOfAssignmentsResponse = AysPageResponse.<AssignmentsResponse>builder()
                .of(mockAysPageOfAssignments)
                .content(mockAssignmentsResponse)
                .build();
        AysResponse<AysPageResponse<AssignmentsResponse>> mockResponse = AysResponse.successOf(pageOfAssignmentsResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

        // Verify
        Mockito.verify(assignmentService, Mockito.times(1))
                .getAssignments(Mockito.any(AssignmentListRequest.class));
    }

    @Test
    void givenValidAssignmentListRequest_whenUserUnauthorizedForListing_thenReturnAccessDeniedException() throws Exception {
        // Given
        AssignmentListRequest mockListRequest = new AssignmentListRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/assignments");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken(), mockListRequest);

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());
    }

    @Test
    void givenValidAssignmentIdAndAssignmentUpdateRequest_whenAssignmentUpdated_thenReturnAysResponseOfSuccess() throws Exception {
        // Given
        String mockAssignmentId = AysRandomUtil.generateUUID();
        AssignmentUpdateRequest mockUpdateRequest = new AssignmentUpdateRequestBuilder()
                .withValidFields()
                .build();

        // When
        Mockito.doNothing()
                .when(assignmentService)
                .updateAssignment(mockAssignmentId, mockUpdateRequest);

        // Then
        String endpoint = BASE_PATH.concat("/assignment/".concat(mockAssignmentId));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminUserToken.getAccessToken(), mockUpdateRequest);

        AysResponse<Void> mockResponse = AysResponse.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(assignmentService, Mockito.times(1))
                .updateAssignment(Mockito.anyString(), Mockito.any(AssignmentUpdateRequest.class));
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
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminUserToken.getAccessToken(), mockRequest);

        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
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
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminUserToken.getAccessToken(), mockRequest);

        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
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
        Mockito.doNothing()
                .when(assignmentService)
                .updateAssignment(mockAssignmentId, mockUpdateRequest);

        // Then
        String endpoint = BASE_PATH.concat("/assignment/".concat(mockAssignmentId));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockUserToken.getAccessToken(), mockUpdateRequest);

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(assignmentService, Mockito.never())
                .updateAssignment(Mockito.any(String.class), Mockito.any(AssignmentUpdateRequest.class));
    }

    @Test
    void givenValidAssignmentId_whenAssignmentDeleted_thenReturnAysResponseOfSuccess() throws Exception {

        // Given
        String mockAssignmentId = AysRandomUtil.generateUUID();

        // When
        Mockito.doNothing()
                .when(assignmentService)
                .deleteAssignment(mockAssignmentId);

        // Then
        String endpoint = BASE_PATH.concat("/assignment/".concat(mockAssignmentId));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .delete(endpoint, mockAdminUserToken.getAccessToken());

        AysResponse<Void> mockResponse = AysResponse.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(assignmentService, Mockito.times(1))
                .deleteAssignment(mockAssignmentId);
    }

    @Test
    void givenValidAssignmentId_whenUserUnauthorizedForDeleting_thenThrowAccessDeniedException() throws Exception {

        // Given
        String mockAssignmentId = AysRandomUtil.generateUUID();

        // When
        Mockito.doNothing().when(assignmentService).deleteAssignment(mockAssignmentId);

        // Then
        String endpoint = BASE_PATH.concat("/assignment/".concat(mockAssignmentId));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .delete(endpoint, mockUserToken.getAccessToken());

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(assignmentService, Mockito.never())
                .deleteAssignment(mockAssignmentId);
    }

    @Test
    void whenAssignmentApproved_thenReturnNothing() throws Exception {
        // When
        Mockito.doNothing()
                .when(assignmentConcludeService)
                .approve();

        // Then
        String endpoint = BASE_PATH.concat("/assignment/approve");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken());

        AysResponse<Void> mockResponse = AysResponse.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(assignmentConcludeService, Mockito.times(1))
                .approve();
    }

    @Test
    void givenVoid_whenUserUnauthorizedForApproving_thenReturnAccessDeniedException() throws Exception {

        // Then
        String endpoint = BASE_PATH.concat("/assignment/approve");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminUserToken.getAccessToken());

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(assignmentConcludeService, Mockito.times(0))
                .approve();
    }

    @Test
    void whenAssignmentStarted_thenReturnNothing() throws Exception {
        // When
        Mockito.doNothing()
                .when(assignmentConcludeService)
                .start();

        // Then
        String endpoint = BASE_PATH.concat("/assignment/start");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken());

        AysResponse<Void> mockResponse = AysResponse.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(assignmentConcludeService, Mockito.times(1))
                .start();
    }

    @Test
    void givenVoid_whenUserUnauthorizedForStarting_thenReturnAccessDeniedException() throws Exception {

        // Then
        String endpoint = BASE_PATH.concat("/assignment/start");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminUserToken.getAccessToken());

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(assignmentConcludeService, Mockito.never())
                .start();
    }

    @Test
    void whenAssignmentRejected_thenReturnNothing() throws Exception {
        // When
        Mockito.doNothing()
                .when(assignmentConcludeService)
                .reject();

        // Then
        String endpoint = BASE_PATH.concat("/assignment/reject");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken());

        AysResponse<Void> mockResponse = AysResponse.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(assignmentConcludeService, Mockito.times(1))
                .reject();
    }

    @Test
    void givenVoid_whenUserUnauthorizedForRejecting_thenReturnAccessDeniedException() throws Exception {

        // Then
        String endpoint = BASE_PATH.concat("/assignment/reject");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminUserToken.getAccessToken());

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(assignmentConcludeService, Mockito.never())
                .reject();
    }

    @Test
    void whenAssignmentCompleted_thenReturnNothing() throws Exception {
        // When
        Mockito.doNothing()
                .when(assignmentConcludeService)
                .complete();

        // Then
        String endpoint = BASE_PATH.concat("/assignment/complete");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken());

        AysResponse<Void> mockResponse = AysResponse.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(assignmentConcludeService, Mockito.times(1))
                .complete();
    }

    @Test
    void givenVoid_whenUserUnauthorizedForCompleting_thenReturnAccessDeniedException() throws Exception {

        // Then
        String endpoint = BASE_PATH.concat("/assignment/complete");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminUserToken.getAccessToken());

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(assignmentConcludeService, Mockito.never())
                .complete();
    }

    @Test
    void givenValidAssignmentCancelRequest_whenAssignmentCanceled_thenReturnNothing() throws Exception {
        // Given
        AssignmentCancelRequest mockCancelRequest = new AssignmentCancelRequestBuilder()
                .withValidFields()
                .build();

        // When
        Mockito.doNothing()
                .when(assignmentConcludeService)
                .cancel(mockCancelRequest);

        // Then
        String endpoint = BASE_PATH.concat("/assignment/cancel");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken(), mockCancelRequest);

        AysResponse<Void> mockResponse = AysResponse.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(assignmentConcludeService, Mockito.times(1))
                .cancel(Mockito.any(AssignmentCancelRequest.class));
    }

    @Test
    void givenInvalidAssignmentCancelRequest_whenReasonHasLessThan40Chars_thenReturnValidationError() throws Exception {
        // Given
        AssignmentCancelRequest mockCancelRequest = new AssignmentCancelRequestBuilder()
                .withReason("invalid-reason")
                .build();

        // When
        Mockito.doNothing()
                .when(assignmentConcludeService)
                .cancel(mockCancelRequest);

        // Then
        String endpoint = BASE_PATH.concat("/assignment/cancel");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken(), mockCancelRequest);

        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(assignmentConcludeService, Mockito.never())
                .cancel(Mockito.any(AssignmentCancelRequest.class));
    }

    @Test
    void givenValidAssignmentCancelRequest_whenUserUnauthorizedForCanceling_thenReturnAccessDeniedException() throws Exception {
        // Given
        AssignmentCancelRequest mockCancelRequest = new AssignmentCancelRequestBuilder()
                .withValidFields()
                .build();

        // When
        Mockito.doNothing()
                .when(assignmentConcludeService)
                .cancel(mockCancelRequest);

        // Then
        String endpoint = BASE_PATH.concat("/assignment/cancel");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminUserToken.getAccessToken(), mockCancelRequest);

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(assignmentConcludeService, Mockito.never())
                .cancel(Mockito.any(AssignmentCancelRequest.class));
    }

    @Test
    void whenUserHasAssignmentWithValidStatus_thenReturnAssignmentSummaryResponse() throws Exception {

        // When
        Assignment mockAssignment = new AssignmentBuilder().build();
        Mockito.when(assignmentService.getAssignmentSummary())
                .thenReturn(mockAssignment);

        // Then
        String endpoint = BASE_PATH.concat("/assignment/summary");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockUserToken.getAccessToken());

        AssignmentSummaryResponse mockSummaryResponse = assignmentToAssignmentSummaryResponseMapper
                .map(mockAssignment);
        AysResponse<AssignmentSummaryResponse> mockResponse = AysResponse.successOf(mockSummaryResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

        // Verify
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
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockAdminUserToken.getAccessToken());

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(assignmentService, Mockito.never())
                .getAssignmentSummary();
    }

}
