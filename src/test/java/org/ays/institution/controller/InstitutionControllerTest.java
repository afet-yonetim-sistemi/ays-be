package org.ays.institution.controller;

import org.ays.AysRestControllerTest;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageBuilder;
import org.ays.common.model.response.AysErrorResponse;
import org.ays.common.model.response.AysErrorResponseBuilder;
import org.ays.common.model.response.AysPageResponse;
import org.ays.common.model.response.AysResponse;
import org.ays.institution.model.Institution;
import org.ays.institution.model.InstitutionBuilder;
import org.ays.institution.model.mapper.InstitutionToInstitutionsResponseMapper;
import org.ays.institution.model.mapper.InstitutionToInstitutionsSummaryResponseMapper;
import org.ays.institution.model.request.InstitutionListRequest;
import org.ays.institution.model.request.InstitutionListRequestBuilder;
import org.ays.institution.model.response.InstitutionsResponse;
import org.ays.institution.model.response.InstitutionsSummaryResponse;
import org.ays.institution.service.InstitutionService;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

class InstitutionControllerTest extends AysRestControllerTest {

    @MockitoBean
    private InstitutionService institutionService;


    private final InstitutionToInstitutionsResponseMapper institutionToInstitutionsResponseMapper = InstitutionToInstitutionsResponseMapper.initialize();
    private final InstitutionToInstitutionsSummaryResponseMapper institutionToInstitutionsSummaryResponseMapper = InstitutionToInstitutionsSummaryResponseMapper.initialize();


    private static final String INSTITUTION_BASE_PATH = "/api/institution/v1";
    private static final String LANDING_BASE_PATH = "/api/landing/v1";

    @Test
    void givenValidInstitutionListRequest_whenInstitutionsFound_thenReturnAysPageResponseOfInstitutionsResponse() throws Exception {
        // Given
        InstitutionListRequest mockListRequest = new InstitutionListRequestBuilder()
                .withValidValues()
                .build();

        //When
        List<Institution> mockInstitutions = List.of(
                new InstitutionBuilder().withValidValues().build()
        );

        AysPage<Institution> mockInstitutionPage = AysPageBuilder
                .from(mockInstitutions, mockListRequest.getPageable());

        Mockito.when(institutionService.findAll(Mockito.any(InstitutionListRequest.class)))
                .thenReturn(mockInstitutionPage);

        // Then
        String endpoint = INSTITUTION_BASE_PATH.concat("/institutions");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken(), mockListRequest);

        List<InstitutionsResponse> mockInstitutionsResponse = institutionToInstitutionsResponseMapper.map(mockInstitutions);
        AysPageResponse<InstitutionsResponse> pageOfResponse = AysPageResponse.<InstitutionsResponse>builder()
                .of(mockInstitutionPage)
                .content(mockInstitutionsResponse)
                .build();
        AysResponse<AysPageResponse<InstitutionsResponse>> mockResponse = AysResponse
                .successOf(pageOfResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

        // Verify
        Mockito.verify(institutionService, Mockito.times(1))
                .findAll(Mockito.any(InstitutionListRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Test Foundation 1234",
            "Foundation *^%$#",
            " Test",
            "? Foundation",
            "J----",
            "Volunteer-Disaster--Foundation",
            "Disaster  Foundation"
    })
    void givenInstitutionListRequest_whenNameDoesNotValid_thenReturnValidationError(String invalidName) throws Exception {

        // Given
        InstitutionListRequest mockListRequest = new InstitutionListRequestBuilder()
                .withValidValues()
                .withName(invalidName)
                .build();

        // Then
        String endpoint = INSTITUTION_BASE_PATH.concat("/institutions");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken(), mockListRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(institutionService, Mockito.never())
                .findAll(Mockito.any(InstitutionListRequest.class));
    }

    @Test
    void givenValidInstitutionListRequest_whenInstitutionUnauthorized_thenReturnAccessDeniedException() throws Exception {
        // Given
        InstitutionListRequest mockListRequest = new InstitutionListRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = INSTITUTION_BASE_PATH.concat("/institutions");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken(), mockListRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(institutionService, Mockito.never())
                .findAll(Mockito.any(InstitutionListRequest.class));
    }

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
        String endpoint = INSTITUTION_BASE_PATH.concat("/institutions/summary");
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
                .get(INSTITUTION_BASE_PATH.concat("/institutions/summary"), mockUserToken.getAccessToken());

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

    @Test
    void whenInstitutionStatusActive_thenReturnListInstitutionResponseForLanding() throws Exception {

        // When
        List<Institution> mockActiveInstitutions = List.of(
                new InstitutionBuilder().withValidValues().build(),
                new InstitutionBuilder().withValidValues().build()
        );

        Mockito.when(institutionService.getSummaryOfActiveInstitutions())
                .thenReturn(mockActiveInstitutions);

        // Then
        String endpoint = LANDING_BASE_PATH.concat("/institutions/summary");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint);

        List<InstitutionsSummaryResponse> mockActiveInstitutionsSummaryResponses = institutionToInstitutionsSummaryResponseMapper
                .map(mockActiveInstitutions);
        AysResponse<List<InstitutionsSummaryResponse>> mockResponse = AysResponse
                .successOf(mockActiveInstitutionsSummaryResponses);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response(1, "id")
                        .value(mockActiveInstitutions.get(1).getId()))
                .andExpect(AysMockResultMatchersBuilders.response(1, "name")
                        .value(mockActiveInstitutions.get(1).getName()));

        // Verify
        Mockito.verify(institutionService, Mockito.times(1))
                .getSummaryOfActiveInstitutions();
    }

}
