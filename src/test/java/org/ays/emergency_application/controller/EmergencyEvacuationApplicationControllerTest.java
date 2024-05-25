package org.ays.emergency_application.controller;

import org.ays.AbstractRestControllerTest;
import org.ays.common.model.dto.response.AysResponse;
import org.ays.common.model.dto.response.AysResponseBuilder;
import org.ays.common.util.exception.model.AysErrorBuilder;
import org.ays.common.util.exception.model.AysErrorResponse;
import org.ays.emergency_application.model.dto.request.EmergencyEvacuationApplicationRequest;
import org.ays.emergency_application.model.dto.request.EmergencyEvacuationRequestBuilder;
import org.ays.emergency_application.model.dto.response.EmergencyEvacuationApplicationResponse;
import org.ays.emergency_application.service.EmergencyEvacuationApplicationService;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EmergencyEvacuationApplicationControllerTest extends AbstractRestControllerTest {
    @MockBean
    private EmergencyEvacuationApplicationService emergencyEvacuationApplicationService;
    private final String ENDPOINT = "/api/v1/emergency-evacuation-application";

    @Test
    void whenFirstNameEmptyShouldReturnInvalidEntityExceptionWith4XXError() throws Exception {
        // Given
        EmergencyEvacuationApplicationRequest emergencyEvacuationApplicationRequest = new EmergencyEvacuationApplicationRequest();

        // Then
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(ENDPOINT, emergencyEvacuationApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());
    }

    @Test
    void whenValidRequestShouldReturnSuccessResponse() throws Exception {
        // Given
        EmergencyEvacuationApplicationRequest emergencyEvacuationApplicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidFields()
                .build();

        // Then
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(ENDPOINT, emergencyEvacuationApplicationRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());
    }

    @Test
    void whenValidRequestShouldCreateObjectCorrectly() throws Exception {
        // Given
        EmergencyEvacuationApplicationRequest emergencyEvacuationApplicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidFields()
                .build();
        EmergencyEvacuationApplicationResponse createdResponse = EmergencyEvacuationApplicationResponse.builder().build();

        // When
        when(emergencyEvacuationApplicationService.create(emergencyEvacuationApplicationRequest)).thenReturn(createdResponse);

        // Then

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(ENDPOINT, emergencyEvacuationApplicationRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        verify(emergencyEvacuationApplicationService, times(1)).create(any(EmergencyEvacuationApplicationRequest.class));
    }
}
