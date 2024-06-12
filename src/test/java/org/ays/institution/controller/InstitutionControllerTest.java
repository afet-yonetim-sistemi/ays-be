package org.ays.institution.controller;

import org.ays.AbstractRestControllerTest;
import org.ays.common.model.response.AysErrorResponse;
import org.ays.common.model.response.AysResponse;
import org.ays.common.util.exception.model.AysErrorBuilder;
import org.ays.institution.model.Institution;
import org.ays.institution.model.dto.response.InstitutionsSummaryResponse;
import org.ays.institution.model.entity.InstitutionBuilder;
import org.ays.institution.model.mapper.InstitutionToInstitutionsSummaryResponseMapper;
import org.ays.institution.service.InstitutionService;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

class InstitutionControllerTest extends AbstractRestControllerTest {

    @MockBean
    private InstitutionService institutionService;


    private final InstitutionToInstitutionsSummaryResponseMapper institutionToInstitutionsSummaryResponseMapper = InstitutionToInstitutionsSummaryResponseMapper.initialize();


    private static final String BASE_PATH = "/api/v1/institutions";

    @Test
    void whenInstitutionStatusActive_thenReturnListInstitutionResponse() throws Exception {

        // When
        List<Institution> mockActiveInstitutions = List.of(
                new InstitutionBuilder().withValidFields().build(),
                new InstitutionBuilder().withValidFields().build()
        );

        Mockito.when(institutionService.getSummaryOfActiveInstitutions())
                .thenReturn(mockActiveInstitutions);

        // Then
        String endpoint = BASE_PATH.concat("/summary");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockSuperAdminTokenV2.getAccessToken());

        List<InstitutionsSummaryResponse> mockActiveInstitutionsSummaryResponses = institutionToInstitutionsSummaryResponseMapper
                .map(mockActiveInstitutions);
        AysResponse<List<InstitutionsSummaryResponse>> mockResponse = AysResponse
                .successOf(mockActiveInstitutionsSummaryResponses);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

        // Verify
        Mockito.verify(institutionService, Mockito.times(1))
                .getSummaryOfActiveInstitutions();
    }

    @Test
    void whenUserUnauthorizedForSummary_thenReturnAccessDeniedException() throws Exception {

        // Then
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(BASE_PATH.concat("/summary"), mockUserToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(institutionService, Mockito.never())
                .getSummaryOfActiveInstitutions();
    }

}
