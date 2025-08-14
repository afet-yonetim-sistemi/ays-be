package org.ays.institution.controller;

import org.ays.AysRestControllerTest;
import org.ays.common.model.response.AysErrorResponse;
import org.ays.common.model.response.AysErrorResponseBuilder;
import org.ays.common.model.response.AysResponse;
import org.ays.institution.model.Institution;
import org.ays.institution.model.InstitutionBuilder;
import org.ays.institution.model.mapper.InstitutionToInstitutionsSummaryResponseMapper;
import org.ays.institution.model.response.InstitutionsSummaryResponse;
import org.ays.institution.service.InstitutionService;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

@Deprecated(since = "1.2.0 - Use or Develop InstitutionControllerTest Class", forRemoval = true)
class InstitutionControllerOldTest extends AysRestControllerTest {

    @MockitoBean
    private InstitutionService institutionService;


    private final InstitutionToInstitutionsSummaryResponseMapper institutionToInstitutionsSummaryResponseMapper = InstitutionToInstitutionsSummaryResponseMapper.initialize();


    private static final String BASE_PATH = "/api/v1";


    @Test
    void whenInstitutionStatusActive_thenReturnListInstitutionResponse() throws Exception {

        // When
        List<Institution> mockActiveInstitutions = List.of(
                new InstitutionBuilder().withValidValues().build(),
                new InstitutionBuilder().withValidValues().build()
        );

        Mockito.when(institutionService.getSummaryOfActiveInstitutions())
                .thenReturn(mockActiveInstitutions);

        // Then
        String endpoint = BASE_PATH.concat("/institutions/summary");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockSuperAdminToken.getAccessToken());

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
                .get(BASE_PATH.concat("/institutions/summary"), mockUserToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.FORBIDDEN;

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
