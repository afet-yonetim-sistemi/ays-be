package org.ays.landing.controller;

import org.ays.AbstractRestControllerTest;
import org.ays.common.model.dto.response.AysResponse;
import org.ays.common.model.dto.response.AysResponseBuilder;
import org.ays.common.util.exception.model.AysErrorBuilder;
import org.ays.common.util.exception.model.AysErrorResponse;
import org.ays.landing.controller.model.dto.request.EmergencyEvacuationRequestBuilder;
import org.ays.landing.model.dto.request.EmergencyEvacuationRequest;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

class EmergencyEvacuationControllerTest extends AbstractRestControllerTest {
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
}
