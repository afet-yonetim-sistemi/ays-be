package com.ays.institution.controller;

import com.ays.AbstractRestControllerTest;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.model.dto.response.AysResponseBuilder;
import com.ays.common.util.exception.model.AysError;
import com.ays.institution.model.Institution;
import com.ays.institution.model.dto.response.InstitutionsSummaryResponse;
import com.ays.institution.model.entity.InstitutionBuilder;
import com.ays.institution.model.enums.InstitutionStatus;
import com.ays.institution.model.mapper.InstitutionToInstitutionSummaryResponseMapper;
import com.ays.institution.service.InstitutionService;
import com.ays.util.AysMockMvcRequestBuilders;
import com.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;
import java.util.List;


class InstitutionControllerTest extends AbstractRestControllerTest {

    @MockBean
    private InstitutionService institutionService;


    private final InstitutionToInstitutionSummaryResponseMapper institutionToInstitutionSummaryResponseMapper = InstitutionToInstitutionSummaryResponseMapper.initialize();


    private static final String BASE_PATH = "/api/v1/institutions";

    @Test
    void givenNothing_whenInstitutionStatusActive_thenReturnListInstitutionResponse() throws Exception {

        String mockAccessToken = mockSuperAdminToken.getAccessToken();

        Institution institution1 = new InstitutionBuilder()
                .withValidFields()
                .withStatus(InstitutionStatus.ACTIVE)
                .build();

        Institution institution2 = new InstitutionBuilder()
                .withValidFields()
                .withStatus(InstitutionStatus.ACTIVE)
                .build();


        List<Institution> mockInstitutions = Arrays.asList(
                institution1,
                institution2
        );

        // when
        Mockito.when(institutionService.getSummaryOfActiveInstitutions()).thenReturn(mockInstitutions);

        // then
        List<InstitutionsSummaryResponse> mockInstitutionResponseList = institutionToInstitutionSummaryResponseMapper.map(mockInstitutions);
        AysResponse<List<InstitutionsSummaryResponse>> mockAysResponse = AysResponse.successOf(mockInstitutionResponseList);

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


        String mockAccessToken = mockUserToken.getAccessToken();


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
