package org.ays.landing.controller;

import org.ays.AbstractRestControllerTest;
import org.ays.common.model.dto.response.AysResponse;
import org.ays.common.model.dto.response.AysResponseBuilder;
import org.ays.common.util.exception.model.AysErrorBuilder;
import org.ays.common.util.exception.model.AysErrorResponse;
import org.ays.landing.controller.model.dto.request.EmergencyEvacuationRequestBuilder;
import org.ays.landing.model.dto.request.EmergencyEvacuationRequest;
import org.ays.landing.model.dto.response.EmergencyEvacuationApplicationResponse;
import org.ays.landing.service.EmergencyEvacuationService;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EmergencyEvacuationControllerTest extends AbstractRestControllerTest {
    @MockBean
    private EmergencyEvacuationService emergencyEvacuationService;

    @Test
    void whenFirstNameEmptyShouldReturnInvalidEntityExceptionWith4XXError() throws Exception {
        // Given
        EmergencyEvacuationRequest emergencyEvacuationRequest = new EmergencyEvacuationRequest();

        // Then
        String endpoint = "/api/v1/emergency-evacuation";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, emergencyEvacuationRequest);

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
        EmergencyEvacuationRequest emergencyEvacuationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidFields()
                .build();

        // Then
        String endpoint = "/api/v1/emergency-evacuation";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, emergencyEvacuationRequest);

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
        EmergencyEvacuationRequest emergencyEvacuationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidFields()
                .build();
        EmergencyEvacuationApplicationResponse createdResponse = EmergencyEvacuationApplicationResponse.builder().build();

        // When
        when(emergencyEvacuationService.addEmergencyEvacuationRequest(emergencyEvacuationRequest)).thenReturn(createdResponse);

        // Then
        String endpoint = "/api/v1/emergency-evacuation";
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, emergencyEvacuationRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        verify(emergencyEvacuationService, times(1)).addEmergencyEvacuationRequest(any(EmergencyEvacuationRequest.class));
    }
}
