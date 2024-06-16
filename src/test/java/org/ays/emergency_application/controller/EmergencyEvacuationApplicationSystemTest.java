package org.ays.emergency_application.controller;

import org.ays.AbstractSystemTest;
import org.ays.auth.model.dto.request.EmergencyEvacuationApplicationListRequestBuilder;
import org.ays.common.model.AysPage;
import org.ays.common.model.response.AysPageResponse;
import org.ays.common.model.response.AysResponse;
import org.ays.common.model.response.AysResponseBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.common.util.exception.model.AysErrorBuilder;
import org.ays.common.util.exception.model.AysErrorResponse;
import org.ays.emergency_application.model.EmergencyEvacuationApplication;
import org.ays.emergency_application.model.dto.request.EmergencyEvacuationApplicationListRequest;
import org.ays.emergency_application.model.dto.request.EmergencyEvacuationApplicationRequest;
import org.ays.emergency_application.model.dto.request.EmergencyEvacuationRequestBuilder;
import org.ays.emergency_application.model.dto.response.EmergencyEvacuationApplicationResponse;
import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationEntity;
import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationEntityBuilder;
import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationStatus;
import org.ays.emergency_application.model.mapper.EmergencyEvacuationApplicationEntityToEmergencyEvacuationApplicationMapper;
import org.ays.emergency_application.model.mapper.EmergencyEvacuationApplicationToApplicationResponseMapper;
import org.ays.emergency_application.repository.EmergencyEvacuationApplicationRepositoryTest;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

class EmergencyEvacuationApplicationSystemTest extends AbstractSystemTest {

    @Autowired
    private EmergencyEvacuationApplicationRepositoryTest emergencyEvacuationApplicationRepository;


    private final EmergencyEvacuationApplicationEntityToEmergencyEvacuationApplicationMapper entityToEmergencyEvacuationApplicationMapper = EmergencyEvacuationApplicationEntityToEmergencyEvacuationApplicationMapper.initialize();
    private final EmergencyEvacuationApplicationToApplicationResponseMapper emergencyEvacuationApplicationToApplicationResponseMapper = EmergencyEvacuationApplicationToApplicationResponseMapper.initialize();

    private final String BASE_PATH = "/api/v1";

    @Test
    void givenValidEmergencyEvacuationApplicationListRequest_whenEmergencyEvacuationApplicationsFound_thenReturnAysPageResponseOfEmergencyEvacuationApplicationsResponse() throws Exception {

        // Initialize
        emergencyEvacuationApplicationRepository.deleteAll();
        EmergencyEvacuationApplicationEntity applicationEntity = emergencyEvacuationApplicationRepository.save(
                new EmergencyEvacuationApplicationEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .withInstitutionId(null)
                        .withStatus(EmergencyEvacuationApplicationStatus.PENDING)
                        .withoutApplicant()
                        .build()
        );

        // Given
        EmergencyEvacuationApplicationListRequest mockListRequest = new EmergencyEvacuationApplicationListRequestBuilder()
                .withValidValues()
                .withFilter(null)
                .withSort(null)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, adminTokenV2.getAccessToken(), mockListRequest);

        List<EmergencyEvacuationApplicationEntity> emergencyEvacuationApplicationEntities = List.of(
                new EmergencyEvacuationApplicationEntityBuilder().withValidFields().build()
        );
        Page<EmergencyEvacuationApplicationEntity> pageOfEntities = new PageImpl<>(
                emergencyEvacuationApplicationEntities
        );
        List<EmergencyEvacuationApplication> adminRegistrationApplications = entityToEmergencyEvacuationApplicationMapper
                .map(emergencyEvacuationApplicationEntities);
        AysPage<EmergencyEvacuationApplication> page = AysPage.of(
                pageOfEntities,
                adminRegistrationApplications
        );
        AysPageResponse<EmergencyEvacuationApplication> pageResponse = AysPageResponse.<EmergencyEvacuationApplication>builder()
                .of(page).build();
        AysResponse<AysPageResponse<EmergencyEvacuationApplication>> mockResponse = AysResponse.successOf(pageResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("content.size()")
                        .value(1))
                .andExpect(AysMockResultMatchersBuilders.firstContent("id")
                        .value(applicationEntity.getId()))
                .andExpect(AysMockResultMatchersBuilders.firstContent("referenceNumber")
                        .value(applicationEntity.getReferenceNumber()))
                .andExpect(AysMockResultMatchersBuilders.firstContent("firstName")
                        .value(applicationEntity.getFirstName()))
                .andExpect(AysMockResultMatchersBuilders.firstContent("lastName")
                        .value(applicationEntity.getLastName()))
                .andExpect(AysMockResultMatchersBuilders.firstContent("phoneNumber.countryCode")
                        .value(applicationEntity.getCountryCode()))
                .andExpect(AysMockResultMatchersBuilders.firstContent("phoneNumber.lineNumber")
                        .value(applicationEntity.getLineNumber()))
                .andExpect(AysMockResultMatchersBuilders.firstContent("seatingCount")
                        .value(applicationEntity.getSeatingCount()))
                .andExpect(AysMockResultMatchersBuilders.firstContent("status")
                        .value(applicationEntity.getStatus().toString()))
                .andExpect(AysMockResultMatchersBuilders.firstContent("isInPerson")
                        .value(applicationEntity.getIsInPerson()))
                .andExpect(AysMockResultMatchersBuilders.firstContent("createdAt")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("pageNumber")
                        .value(1))
                .andExpect(AysMockResultMatchersBuilders.response("pageSize")
                        .value(1))
                .andExpect(AysMockResultMatchersBuilders.response("totalPageCount")
                        .value(1))
                .andExpect(AysMockResultMatchersBuilders.response("totalElementCount")
                        .value(1))
                .andExpect(AysMockResultMatchersBuilders.response("sortedBy")
                        .isEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("filteredBy")
                        .isEmpty());
    }

    @Test
    void givenValidEmergencyEvacuationApplicationListRequest_whenEmergencyEvacuationApplicationsNotFound_thenReturnAysPageResponseOfEmergencyEvacuationApplicationsResponse() throws Exception {

        // Initialize
        emergencyEvacuationApplicationRepository.deleteAll();
        emergencyEvacuationApplicationRepository.save(
                new EmergencyEvacuationApplicationEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .withInstitutionId(null)
                        .withStatus(EmergencyEvacuationApplicationStatus.PENDING)
                        .withoutApplicant()
                        .build()
        );

        // Given
        EmergencyEvacuationApplicationListRequest mockListRequest = new EmergencyEvacuationApplicationListRequestBuilder()
                .withValidValues()
                .withStatuses(Set.of(EmergencyEvacuationApplicationStatus.COMPLETED))
                .withSort(null)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, adminTokenV2.getAccessToken(), mockListRequest);

        List<EmergencyEvacuationApplicationEntity> emergencyEvacuationApplicationEntities = List.of(
                new EmergencyEvacuationApplicationEntityBuilder().withValidFields().build()
        );
        Page<EmergencyEvacuationApplicationEntity> pageOfEntities = new PageImpl<>(
                emergencyEvacuationApplicationEntities
        );
        List<EmergencyEvacuationApplication> adminRegistrationApplications = entityToEmergencyEvacuationApplicationMapper
                .map(emergencyEvacuationApplicationEntities);
        AysPage<EmergencyEvacuationApplication> page = AysPage.of(
                pageOfEntities,
                adminRegistrationApplications
        );
        AysPageResponse<EmergencyEvacuationApplication> pageResponse = AysPageResponse.<EmergencyEvacuationApplication>builder()
                .of(page).build();
        AysResponse<AysPageResponse<EmergencyEvacuationApplication>> mockResponse = AysResponse.successOf(pageResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("content.size()")
                        .value(0))
                .andExpect(AysMockResultMatchersBuilders.firstContent("id")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.firstContent("referenceNumber")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.firstContent("firstName")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.firstContent("lastName")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.firstContent("phoneNumber.countryCode")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.firstContent("phoneNumber.lineNumber")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.firstContent("seatingCount")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.firstContent("status")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.firstContent("isInPerson")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.firstContent("createdAt")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.response("pageNumber")
                        .value(1))
                .andExpect(AysMockResultMatchersBuilders.response("pageSize")
                        .value(0))
                .andExpect(AysMockResultMatchersBuilders.response("totalPageCount")
                        .value(0))
                .andExpect(AysMockResultMatchersBuilders.response("totalElementCount")
                        .value(0))
                .andExpect(AysMockResultMatchersBuilders.response("sortedBy")
                        .isEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("filteredBy")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("filteredBy.statuses")
                        .isNotEmpty());
    }

    @Test
    void givenValidEmergencyEvacuationApplicationId_whenEmergencyEvacuationApplicationExists_thenReturnEmergencyEvacuationApplicationResponse() throws Exception {

        // Initialize
        emergencyEvacuationApplicationRepository.deleteAll();
        EmergencyEvacuationApplicationEntity applicationEntity = emergencyEvacuationApplicationRepository.save(
                new EmergencyEvacuationApplicationEntityBuilder()
                        .withValidFields()
                        .withId(null)
                        .withInstitutionId(null)
                        .withoutApplicant()
                        .build()
        );

        // Given
        String applicationId = applicationEntity.getId();

        // When
        EmergencyEvacuationApplication emergencyEvacuationApplication = entityToEmergencyEvacuationApplicationMapper
                .map(applicationEntity);

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application/").concat(applicationId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, adminTokenV2.getAccessToken());

        EmergencyEvacuationApplicationResponse mockEmergencyEvacuationApplicationResponse = emergencyEvacuationApplicationToApplicationResponseMapper
                .map(emergencyEvacuationApplication);
        AysResponse<EmergencyEvacuationApplicationResponse> mockResponse = AysResponse
                .successOf(mockEmergencyEvacuationApplicationResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("id")
                        .value(applicationEntity.getId()))
                .andExpect(AysMockResultMatchersBuilders.response("referenceNumber")
                        .value(applicationEntity.getReferenceNumber()))
                .andExpect(AysMockResultMatchersBuilders.response("firstName")
                        .value(applicationEntity.getFirstName()))
                .andExpect(AysMockResultMatchersBuilders.response("lastName")
                        .value(applicationEntity.getLastName()))
                .andExpect(AysMockResultMatchersBuilders.response("phoneNumber.countryCode")
                        .value(applicationEntity.getCountryCode()))
                .andExpect(AysMockResultMatchersBuilders.response("phoneNumber.lineNumber")
                        .value(applicationEntity.getLineNumber()))
                .andExpect(AysMockResultMatchersBuilders.response("sourceCity")
                        .value(applicationEntity.getSourceCity()))
                .andExpect(AysMockResultMatchersBuilders.response("sourceDistrict")
                        .value(applicationEntity.getSourceDistrict()))
                .andExpect(AysMockResultMatchersBuilders.response("address")
                        .value(applicationEntity.getAddress()))
                .andExpect(AysMockResultMatchersBuilders.response("seatingCount")
                        .value(applicationEntity.getSeatingCount()))
                .andExpect(AysMockResultMatchersBuilders.response("targetCity")
                        .value(applicationEntity.getTargetCity()))
                .andExpect(AysMockResultMatchersBuilders.response("targetDistrict")
                        .value(applicationEntity.getTargetDistrict()))
                .andExpect(AysMockResultMatchersBuilders.response("status")
                        .value(applicationEntity.getStatus().toString()))
                .andExpect(AysMockResultMatchersBuilders.response("isInPerson")
                        .value(applicationEntity.getIsInPerson()))
                .andExpect(AysMockResultMatchersBuilders.response("hasObstaclePersonExist")
                        .value(applicationEntity.getHasObstaclePersonExist()))
                .andExpect(AysMockResultMatchersBuilders.response("notes")
                        .value(applicationEntity.getNotes()))
                .andExpect(AysMockResultMatchersBuilders.response("createdUser")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("createdAt")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("updatedUser")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("updatedAt")
                        .isNotEmpty());
    }

    @Test
    void givenValidApplicationId_whenUserUnauthorizedForEmergencyEvacuationApplication_thenReturnAccessDeniedException() throws Exception {

        // When
        String mockApplicationId = AysRandomUtil.generateUUID();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application/").concat(mockApplicationId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, superAdminTokenV2.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());
    }


    @Test
    void givenValidEmergencyEvacuationApplicationRequest_whenApplicationSaved_thenReturnSuccessResponse() throws Exception {
        // Given
        String firstName = "Test Application with Applicant";
        EmergencyEvacuationApplicationRequest applicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidFields()
                .withFirstName(firstName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, applicationRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;
        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Optional<EmergencyEvacuationApplicationEntity> applicationEntity = emergencyEvacuationApplicationRepository
                .findByFirstName(firstName);

        Assertions.assertTrue(applicationEntity.isPresent());
        Assertions.assertNull(applicationEntity.get().getInstitutionId());
        Assertions.assertNull(applicationEntity.get().getInstitutionEntity());
        Assertions.assertNotNull(applicationEntity.get().getReferenceNumber());
        Assertions.assertEquals(10, applicationEntity.get().getReferenceNumber().length());
        Assertions.assertEquals(applicationEntity.get().getFirstName(), firstName);
        Assertions.assertEquals(applicationEntity.get().getLastName(), applicationRequest.getLastName());
        Assertions.assertEquals(applicationEntity.get().getCountryCode(), applicationRequest.getPhoneNumber().getCountryCode());
        Assertions.assertEquals(applicationEntity.get().getLineNumber(), applicationRequest.getPhoneNumber().getLineNumber());
        Assertions.assertEquals(applicationEntity.get().getSourceCity(), applicationRequest.getSourceCity());
        Assertions.assertEquals(applicationEntity.get().getSourceDistrict(), applicationRequest.getSourceDistrict());
        Assertions.assertEquals(applicationEntity.get().getAddress(), applicationRequest.getAddress());
        Assertions.assertEquals(applicationEntity.get().getSeatingCount(), applicationRequest.getSeatingCount());
        Assertions.assertEquals(applicationEntity.get().getTargetCity(), applicationRequest.getTargetCity());
        Assertions.assertEquals(applicationEntity.get().getTargetDistrict(), applicationRequest.getTargetDistrict());
        Assertions.assertEquals(EmergencyEvacuationApplicationStatus.PENDING, applicationEntity.get().getStatus());
        Assertions.assertEquals(applicationEntity.get().getApplicantFirstName(), applicationRequest.getApplicantFirstName());
        Assertions.assertEquals(applicationEntity.get().getApplicantLastName(), applicationRequest.getApplicantLastName());
        Assertions.assertEquals(applicationEntity.get().getApplicantCountryCode(), applicationRequest.getApplicantPhoneNumber().getCountryCode());
        Assertions.assertEquals(applicationEntity.get().getApplicantLineNumber(), applicationRequest.getApplicantPhoneNumber().getLineNumber());
        Assertions.assertFalse(applicationEntity.get().getIsInPerson());
        Assertions.assertNull(applicationEntity.get().getHasObstaclePersonExist());
        Assertions.assertNull(applicationEntity.get().getNotes());
    }

    @Test
    void givenValidEmergencyEvacuationApplicationRequestWithoutApplicant_whenApplicationSaved_thenReturnSuccessResponse() throws Exception {
        // Given
        String firstName = "Test Application without Applicant";
        EmergencyEvacuationApplicationRequest applicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidFields()
                .withFirstName(firstName)
                .withoutApplicant()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, applicationRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;
        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Optional<EmergencyEvacuationApplicationEntity> applicationEntity = emergencyEvacuationApplicationRepository
                .findByFirstName(firstName);

        Assertions.assertTrue(applicationEntity.isPresent());
        Assertions.assertNull(applicationEntity.get().getInstitutionId());
        Assertions.assertNull(applicationEntity.get().getInstitutionEntity());
        Assertions.assertNotNull(applicationEntity.get().getReferenceNumber());
        Assertions.assertEquals(10, applicationEntity.get().getReferenceNumber().length());
        Assertions.assertEquals(applicationEntity.get().getFirstName(), firstName);
        Assertions.assertEquals(applicationEntity.get().getLastName(), applicationRequest.getLastName());
        Assertions.assertEquals(applicationEntity.get().getCountryCode(), applicationRequest.getPhoneNumber().getCountryCode());
        Assertions.assertEquals(applicationEntity.get().getLineNumber(), applicationRequest.getPhoneNumber().getLineNumber());
        Assertions.assertEquals(applicationEntity.get().getSourceCity(), applicationRequest.getSourceCity());
        Assertions.assertEquals(applicationEntity.get().getSourceDistrict(), applicationRequest.getSourceDistrict());
        Assertions.assertEquals(applicationEntity.get().getAddress(), applicationRequest.getAddress());
        Assertions.assertEquals(applicationEntity.get().getSeatingCount(), applicationRequest.getSeatingCount());
        Assertions.assertEquals(applicationEntity.get().getTargetCity(), applicationRequest.getTargetCity());
        Assertions.assertEquals(applicationEntity.get().getTargetDistrict(), applicationRequest.getTargetDistrict());
        Assertions.assertEquals(EmergencyEvacuationApplicationStatus.PENDING, applicationEntity.get().getStatus());
        Assertions.assertEquals(applicationEntity.get().getApplicantFirstName(), applicationRequest.getApplicantFirstName());
        Assertions.assertEquals(applicationEntity.get().getApplicantLastName(), applicationRequest.getApplicantLastName());
        Assertions.assertNull(applicationEntity.get().getApplicantCountryCode());
        Assertions.assertNull(applicationEntity.get().getApplicantLineNumber());
        Assertions.assertTrue(applicationEntity.get().getIsInPerson());
        Assertions.assertNull(applicationEntity.get().getHasObstaclePersonExist());
        Assertions.assertNull(applicationEntity.get().getNotes());
    }

}
