package com.ays.institution.controller;

import com.ays.AbstractSystemTest;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.model.dto.response.AysResponseBuilder;
import com.ays.common.util.exception.model.AysError;
import com.ays.institution.model.Institution;
import com.ays.institution.model.dto.response.InstitutionsSummaryResponse;
import com.ays.institution.model.entity.InstitutionBuilder;
import com.ays.institution.model.enums.InstitutionStatus;
import com.ays.institution.model.mapper.InstitutionToInstitutionsSummaryResponseMapper;
import com.ays.util.AysMockMvcRequestBuilders;
import com.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

class InstitutionSystemTest extends AbstractSystemTest {


    private final InstitutionToInstitutionsSummaryResponseMapper institutionToInstitutionsSummaryResponseMapper = InstitutionToInstitutionsSummaryResponseMapper.initialize();


    private static final String BASE_PATH = "/api/v1/institutions";

    @Test
    void whenActiveInstitutionsExist_thenReturnInstitutionSummaryResponses() throws Exception {

        // When
        List<Institution> mockActiveInstitutions = List.of(
                new InstitutionBuilder().withValidFields().withStatus(InstitutionStatus.ACTIVE).build(),
                new InstitutionBuilder().withValidFields().withStatus(InstitutionStatus.ACTIVE).build()
        );

        // Then
        List<InstitutionsSummaryResponse> mockInstitutionResponses = institutionToInstitutionsSummaryResponseMapper
                .map(mockActiveInstitutions);
        AysResponse<List<InstitutionsSummaryResponse>> mockAysResponse = AysResponse.successOf(mockInstitutionResponses);

        mockMvc.perform(AysMockMvcRequestBuilders
                        .get(BASE_PATH.concat("/summary"), superAdminToken.getAccessToken()))
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
    void givenNothing_whenUserUnauthorizedForSummary_thenReturnAccessDeniedException() throws Exception {

        // When
        String mockAccessToken = userToken.getAccessToken();

        // Then
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(BASE_PATH.concat("/summary"), mockAccessToken);

        AysResponse<AysError> mockResponse = AysResponseBuilder.FORBIDDEN;
        mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(AysMockResultMatchersBuilders.status().isForbidden())
                .andExpect(AysMockResultMatchersBuilders.time().isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.httpStatus().value(mockResponse.getHttpStatus().name()))
                .andExpect(AysMockResultMatchersBuilders.isSuccess().value(mockResponse.getIsSuccess()))
                .andExpect(AysMockResultMatchersBuilders.response().doesNotExist());

    }

}
