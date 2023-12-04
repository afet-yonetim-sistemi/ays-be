package com.ays.institution.controller;

import com.ays.AbstractRestControllerTest;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.model.dto.response.AysResponseBuilder;
import com.ays.common.util.exception.model.AysError;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

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

        Mockito.when(institutionService.getSummaryOfActiveInstitutions()).thenReturn(mockActiveInstitutions);

        // Then
        List<InstitutionsSummaryResponse> mockActiveInstitutionsSummaryResponses = institutionToInstitutionsSummaryResponseMapper.map(mockActiveInstitutions);
        AysResponse<List<InstitutionsSummaryResponse>> mockAysResponse = AysResponse.successOf(mockActiveInstitutionsSummaryResponses);

        mockMvc.perform(AysMockMvcRequestBuilders
                        .get(BASE_PATH.concat("/summary"), mockSuperAdminToken.getAccessToken()))
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
    void whenUserUnauthorizedForSummary_thenReturnAccessDeniedException() throws Exception {

        // When
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(BASE_PATH.concat("/summary"), mockUserToken.getAccessToken());

        // Then
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
