package org.ays.institution.controller;

import org.ays.AysEndToEndTest;
import org.ays.common.model.response.AysPageResponse;
import org.ays.common.model.response.AysResponse;
import org.ays.common.model.response.AysResponseBuilder;
import org.ays.institution.model.Institution;
import org.ays.institution.model.InstitutionBuilder;
import org.ays.institution.model.enums.InstitutionStatus;
import org.ays.institution.model.mapper.InstitutionToInstitutionsSummaryResponseMapper;
import org.ays.institution.model.request.InstitutionListRequest;
import org.ays.institution.model.request.InstitutionListRequestBuilder;
import org.ays.institution.model.response.InstitutionsResponse;
import org.ays.institution.model.response.InstitutionsSummaryResponse;
import org.ays.institution.port.InstitutionSavePort;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.Set;

class InstitutionEndToEndTest extends AysEndToEndTest {

    @Autowired
    private InstitutionSavePort institutionSavePort;

    private final InstitutionToInstitutionsSummaryResponseMapper institutionToInstitutionsSummaryResponseMapper = InstitutionToInstitutionsSummaryResponseMapper.initialize();


    private static final String INSTITUTION_BASE_PATH = "/api/institution/v1";
    private static final String LANDING_BASE_PATH = "/api/landing/v1";

    @Test
    void givenValidInstitutionListRequest_whenInstitutionsFoundForSuperAdmin_thenReturnAysPageResponseOfInstitutionsResponse() throws Exception {

        // Initialize
        institutionSavePort.save(
                new InstitutionBuilder()
                        .withValidValues()
                        .withoutId()
                        .withName("Afet Derneği")
                        .withStatus(InstitutionStatus.ACTIVE)
                        .build()
        );

        // Given
        InstitutionListRequest listRequest = new InstitutionListRequestBuilder()
                .withValidValues()
                .withName("Afet Derneği")
                .withStatuses(Set.of(InstitutionStatus.ACTIVE))
                .build();

        // Then
        String endpoint = INSTITUTION_BASE_PATH.concat("/institutions");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminToken.getAccessToken(), listRequest);

        AysResponse<AysPageResponse<InstitutionsResponse>> mockResponse = AysResponseBuilder.successPage();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.content()
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.contentSize()
                        .value(1))
                .andExpect(AysMockResultMatchersBuilders.firstContent("id")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("name")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("status")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("createdAt")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("updatedAt")
                        .isEmpty());
    }

    @Test
    void whenActiveInstitutionsExist_thenReturnInstitutionSummaryResponses() throws Exception {

        // When
        List<Institution> mockActiveInstitutions = List.of(
                new InstitutionBuilder().withValidValues().withStatus(InstitutionStatus.ACTIVE).build(),
                new InstitutionBuilder().withValidValues().withStatus(InstitutionStatus.ACTIVE).build()
        );

        // Then
        String endpoint = INSTITUTION_BASE_PATH.concat("/institutions/summary");
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

    @Test
    void whenActiveInstitutionsExist_thenReturnInstitutionSummaryResponsesForLanding() throws Exception {

        // When
        List<Institution> mockActiveInstitutions = List.of(
                new InstitutionBuilder().withValidValues().withStatus(InstitutionStatus.ACTIVE).build(),
                new InstitutionBuilder().withValidValues().withStatus(InstitutionStatus.ACTIVE).build()
        );

        // Then
        String endpoint = LANDING_BASE_PATH.concat("/institutions/summary");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint);

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
