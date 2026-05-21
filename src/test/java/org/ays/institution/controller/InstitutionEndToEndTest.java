package org.ays.institution.controller;

import org.assertj.core.api.Assertions;
import org.ays.AysEndToEndTest;
import org.ays.common.model.AysPage;
import org.ays.common.model.response.AysPageResponse;
import org.ays.common.model.response.AysResponse;
import org.ays.common.model.response.AysResponseBuilder;
import org.ays.common.util.AysRandomUtil;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.Objects;
import java.util.Set;

class InstitutionEndToEndTest extends AysEndToEndTest {

    @Autowired
    private InstitutionSavePort institutionSavePort;

    @Autowired
    private CacheManager cacheManager;

    private final InstitutionToInstitutionsSummaryResponseMapper institutionToInstitutionsSummaryResponseMapper = InstitutionToInstitutionsSummaryResponseMapper.initialize();


    private static final String INSTITUTION_BASE_PATH = "/api/institution/v1";
    private static final String LANDING_BASE_PATH = "/api/landing/v1";

    @ParameterizedTest
    @MethodSource("mockStatuses")
    void givenValidInstitutionListRequest_whenInstitutionsFoundForSuperAdmin_thenReturnAysPageResponseOfInstitutionsResponse(InstitutionStatus mockStatus) throws Exception {

        // Initialize
        String institutionName = AysRandomUtil.generateText(10).concat(" Derneği");

        institutionSavePort.save(
                new InstitutionBuilder()
                        .withValidValues()
                        .withoutId()
                        .withName(institutionName)
                        .withStatus(mockStatus)
                        .build()
        );

        // Given
        InstitutionListRequest listRequest = new InstitutionListRequestBuilder()
                .withValidValues()
                .withName(institutionName)
                .withStatuses(Set.of(mockStatus))
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

    private static List<InstitutionStatus> mockStatuses() {
        return List.of(
                InstitutionStatus.ACTIVE,
                InstitutionStatus.PASSIVE,
                InstitutionStatus.DELETED
        );
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


    @Test
    void givenValidRequest_whenInstitutionCalledFirstTime_thenFetchFromDbAndCacheIt_whenCalledSecondTime_thenFetchFromCache() throws Exception {

        // Initialize
        String institutionName = AysRandomUtil.generateText(10).concat(" Derneği");

        institutionSavePort.save(
                new InstitutionBuilder()
                        .withValidValues()
                        .withoutId()
                        .withName(institutionName)
                        .withStatus(InstitutionStatus.ACTIVE)
                        .build()
        );

        // Given
        InstitutionListRequest listRequest = new InstitutionListRequestBuilder()
                .withValidValues()
                .build();

        String cacheKey = "findAll::1:10:null:null";

        Cache cache = Objects.requireNonNull(cacheManager.getCache("InstitutionAdapter"));
        cache.evict(cacheKey);

        // Verify
        Assertions.assertThat(cache.get(cacheKey))
                .as("Cache must be empty before the first request. Key: %s", cacheKey)
                .isNull();


        // When - First request execution
        String endpoint = INSTITUTION_BASE_PATH.concat("/institutions");
        MockHttpServletRequestBuilder firstRequest = AysMockMvcRequestBuilders
                .post(endpoint, superAdminToken.getAccessToken(), listRequest);

        AysResponse<AysPageResponse<InstitutionsResponse>> mockResponse = AysResponseBuilder.successPage();

        aysMockMvc.perform(firstRequest, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status().isOk());


        // Then - First request
        Assertions.assertThat(cache.get(cacheKey))
                .as("Cache must be populated after the first request. Key: %s", cacheKey)
                .isNotNull();


        // Then — Second request
        @SuppressWarnings("unchecked")
        AysPage<Institution> cachedPage = (AysPage<Institution>)
                Objects.requireNonNull(cache.get(cacheKey)).get();

        String manipulatedName = "Data From Cache";
        Assertions.assertThat(cachedPage).isNotNull();
        cachedPage.getContent().get(0).setName(manipulatedName);

        cache.put(cacheKey, cachedPage);

        MockHttpServletRequestBuilder secondRequest = AysMockMvcRequestBuilders
                .post(endpoint, superAdminToken.getAccessToken(), listRequest);

        aysMockMvc.perform(secondRequest, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status().isOk())
                .andExpect(AysMockResultMatchersBuilders.firstContent("name")
                        .value(manipulatedName));
    }
}
