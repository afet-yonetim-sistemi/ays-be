package org.ays.institution.controller;

import org.ays.AbstractSystemTest;
import org.ays.common.model.dto.response.AysResponse;
import org.ays.common.util.exception.model.AysErrorBuilder;
import org.ays.common.util.exception.model.AysErrorResponse;
import org.ays.institution.model.Institution;
import org.ays.institution.model.dto.response.InstitutionsSummaryResponse;
import org.ays.institution.model.entity.InstitutionBuilder;
import org.ays.institution.model.enums.InstitutionStatus;
import org.ays.institution.model.mapper.InstitutionToInstitutionsSummaryResponseMapper;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

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
        String endpoint = BASE_PATH.concat("/summary");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, superAdminTokenV2.getAccessToken());

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

    @Test
    void givenNothing_whenUserUnauthorizedForSummary_thenReturnAccessDeniedException() throws Exception {

        // When
        String mockAccessToken = userToken.getAccessToken();

        // Then
        String endpoint = BASE_PATH.concat("/summary");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockAccessToken);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());
    }

}
