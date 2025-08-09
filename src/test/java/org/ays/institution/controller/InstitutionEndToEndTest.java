package org.ays.institution.controller;

import org.ays.AysEndToEndTest;
import org.ays.common.model.response.AysResponse;
import org.ays.institution.model.Institution;
import org.ays.institution.model.InstitutionBuilder;
import org.ays.institution.model.enums.InstitutionStatus;
import org.ays.institution.model.mapper.InstitutionToInstitutionsSummaryResponseMapper;
import org.ays.institution.model.response.InstitutionsSummaryResponse;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

class InstitutionEndToEndTest extends AysEndToEndTest {


    private final InstitutionToInstitutionsSummaryResponseMapper institutionToInstitutionsSummaryResponseMapper = InstitutionToInstitutionsSummaryResponseMapper.initialize();


    private static final String BASE_PATH = "/api/institution/v1";


    @Test
    void whenActiveInstitutionsExist_thenReturnInstitutionSummaryResponses() throws Exception {

        // When
        List<Institution> mockActiveInstitutions = List.of(
                new InstitutionBuilder().withValidValues().withStatus(InstitutionStatus.ACTIVE).build(),
                new InstitutionBuilder().withValidValues().withStatus(InstitutionStatus.ACTIVE).build()
        );

        // Then
        String endpoint = BASE_PATH.concat("/institutions/summary");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, superAdminToken.getAccessToken());

        List<InstitutionsSummaryResponse> mockInstitutionResponses = institutionToInstitutionsSummaryResponseMapper
                .map(mockActiveInstitutions);
        AysResponse<List<InstitutionsSummaryResponse>> mockResponse = AysResponse
                .successOf(mockInstitutionResponses);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());
    }

}
