package com.ays.institution.controller;

import com.ays.AbstractSystemTest;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.model.dto.response.AysResponseBuilder;
import com.ays.common.util.exception.model.AysError;
import com.ays.institution.model.Institution;
import com.ays.institution.model.dto.response.InstitutionsSummaryResponse;
import com.ays.institution.model.entity.InstitutionBuilder;
import com.ays.institution.model.mapper.InstitutionToInstitutionSummaryResponseMapper;
import com.ays.util.AysMockMvcRequestBuilders;
import com.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;
import java.util.List;

class InstitutionSystemTest extends AbstractSystemTest {


    private final InstitutionToInstitutionSummaryResponseMapper institutionToInstitutionSummaryResponseMapper = InstitutionToInstitutionSummaryResponseMapper.initialize();


    private static final String BASE_PATH = "/api/v1/institutions";

    @Test
    void whenActiveInstitutionsExist_thenReturnInstitutionSummaryResponses() throws Exception {

        // When
        String mockAccessToken = superAdminToken.getAccessToken();

        Institution institution1 = new InstitutionBuilder().withValidFields().build();
        Institution institution2 = new InstitutionBuilder().withValidFields().build();


        List<Institution> mockInstitutions = Arrays.asList(
                institution1,
                institution2
        );


        // Then
        List<InstitutionsSummaryResponse> mockInstitutionResponses = institutionToInstitutionSummaryResponseMapper.map(mockInstitutions);
        AysResponse<List<InstitutionsSummaryResponse>> mockAysResponse = AysResponse.successOf(mockInstitutionResponses);

        mockMvc.perform(AysMockMvcRequestBuilders
                        .get(BASE_PATH.concat("/summary"), mockAccessToken))
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
        String mockAccessToken = userTokenOne.getAccessToken();

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
