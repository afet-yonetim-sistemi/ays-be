package com.ays.assignment.controller;

import com.ays.AbstractRestControllerTest;
import com.ays.assignment.model.Assignment;
import com.ays.assignment.model.AssignmentBuilder;
import com.ays.assignment.model.dto.request.AssignmentSaveRequest;
import com.ays.assignment.model.dto.request.AssignmentSaveRequestBuilder;
import com.ays.assignment.model.dto.response.AssignmentSavedResponse;
import com.ays.assignment.model.dto.response.AssignmentSavedResponseBuilder;
import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.assignment.model.mapper.AssignmentToAssignmentSavedResponseMapper;
import com.ays.assignment.service.AssignmentSaveService;
import com.ays.common.model.AysPhoneNumberBuilder;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.model.dto.response.AysResponseBuilder;
import com.ays.common.util.exception.model.AysError;
import com.ays.util.AysMockMvcRequestBuilders;
import com.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


class AssignmentControllerTest extends AbstractRestControllerTest {

    @MockBean
    private AssignmentSaveService assignmentSaveService;

    private final AssignmentToAssignmentSavedResponseMapper assignmentToAssignmentSavedResponseMapper = AssignmentToAssignmentSavedResponseMapper.initialize();

    private static final String BASE_PATH = "/api/v1";

    @Test
    void givenValidAssignmentSaveRequest_whenAssignmentSaved_thenReturnAssignmentSavedResponse() throws Exception {
        // Given
        AssignmentSaveRequest mockAssignmentSaveRequest = new AssignmentSaveRequestBuilder()
                .withPhoneNumber(new AysPhoneNumberBuilder().withValidFields().build())
                .build();

        // When
        Assignment mockAssignment = new AssignmentBuilder()
                .withDescription(mockAssignmentSaveRequest.getDescription())
                .withFirstName(mockAssignmentSaveRequest.getFirstName())
                .withLastName(mockAssignmentSaveRequest.getLastName())
                .withPhoneNumber(mockAssignmentSaveRequest.getPhoneNumber())
                .withStatus(AssignmentStatus.AVAILABLE)
                .build();
        Mockito.when(assignmentSaveService.saveAssignment(Mockito.any(AssignmentSaveRequest.class)))
                .thenReturn(mockAssignment);

        // Then
        String endpoint = BASE_PATH.concat("/assignment");
        AssignmentSavedResponse mockUserSavedResponse = new AssignmentSavedResponseBuilder()
                .withDescription(mockAssignment.getDescription())
                .withFirstName(mockAssignment.getFirstName())
                .withLastName(mockAssignment.getLastName())
                .withPhoneNumber(mockAssignment.getPhoneNumber())
                .withLongitude(mockAssignment.getLongitude())
                .withLatitude(mockAssignment.getLatitude())
                .build();
        AysResponse<AssignmentSavedResponse> mockResponse = AysResponseBuilder.successOf(mockUserSavedResponse);
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(endpoint, mockAdminUserToken.getAccessToken(), mockAssignmentSaveRequest))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.time()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus()
                        .value(mockResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess()
                        .value(mockResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.description")
                        .value(mockResponse.getResponse().getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.firstName")
                        .value(mockResponse.getResponse().getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.lastName")
                        .value(mockResponse.getResponse().getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.phoneNumber.countryCode")
                        .value(mockResponse.getResponse().getPhoneNumber().getCountryCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.phoneNumber.lineNumber")
                        .value(mockResponse.getResponse().getPhoneNumber().getLineNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.latitude")
                        .value(mockResponse.getResponse().getLatitude()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.longitude")
                        .value(mockResponse.getResponse().getLongitude()))
        ;

        Mockito.verify(assignmentSaveService, Mockito.times(1))
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
}