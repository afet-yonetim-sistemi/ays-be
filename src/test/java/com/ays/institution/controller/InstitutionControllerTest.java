package com.ays.institution.controller;

import com.ays.AbstractRestControllerTest;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.util.exception.model.AysError;
import com.ays.common.util.exception.model.AysErrorBuilder;
import com.ays.institution.model.Institution;
import com.ays.institution.model.dto.response.InstitutionsSummaryResponse;
import com.ays.institution.model.entity.InstitutionBuilder;
import com.ays.institution.model.mapper.InstitutionToInstitutionsSummaryResponseMapper;
import com.ays.institution.service.InstitutionService;
import com.ays.util.AysMockMvcRequestBuilders;
import com.ays.util.AysMockResultMatchersBuilders;
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
                .get(BASE_PATH.concat("/summary"), mockUserToken.getAccessToken());

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

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
