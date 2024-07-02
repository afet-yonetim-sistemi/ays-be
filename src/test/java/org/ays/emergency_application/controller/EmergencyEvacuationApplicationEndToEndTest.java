package org.ays.emergency_application.controller;

import org.ays.AysEndToEndTest;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageBuilder;
import org.ays.common.model.AysPageable;
import org.ays.common.model.response.AysErrorResponse;
import org.ays.common.model.response.AysPageResponse;
import org.ays.common.model.response.AysResponse;
import org.ays.common.model.response.AysResponseBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.common.util.exception.model.AysErrorBuilder;
import org.ays.emergency_application.model.EmergencyEvacuationApplication;
import org.ays.emergency_application.model.EmergencyEvacuationApplicationBuilder;
import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationStatus;
import org.ays.emergency_application.model.mapper.EmergencyEvacuationApplicationToApplicationResponseMapper;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationListRequest;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationListRequestBuilder;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationRequest;
import org.ays.emergency_application.model.request.EmergencyEvacuationRequestBuilder;
import org.ays.emergency_application.model.response.EmergencyEvacuationApplicationResponse;
import org.ays.emergency_application.port.EmergencyEvacuationApplicationReadPort;
import org.ays.emergency_application.port.EmergencyEvacuationApplicationSavePort;
import org.ays.emergency_application.repository.EmergencyEvacuationApplicationRepository;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

class EmergencyEvacuationApplicationEndToEndTest extends AysEndToEndTest {

    @Autowired
    private EmergencyEvacuationApplicationSavePort emergencyEvacuationApplicationSavePort;

    @Autowired
    private EmergencyEvacuationApplicationReadPort emergencyEvacuationApplicationReadPort;

    @Autowired
    private EmergencyEvacuationApplicationRepository emergencyEvacuationApplicationRepository;


    private final EmergencyEvacuationApplicationToApplicationResponseMapper emergencyEvacuationApplicationToApplicationResponseMapper = EmergencyEvacuationApplicationToApplicationResponseMapper.initialize();


    private final String BASE_PATH = "/api/v1";

    @Test
    void givenValidEmergencyEvacuationApplicationListRequest_whenEmergencyEvacuationApplicationsFound_thenReturnAysPageResponseOfEmergencyEvacuationApplicationsResponse() throws Exception {

        // Initialize
        emergencyEvacuationApplicationRepository.deleteAll();
        EmergencyEvacuationApplication application = emergencyEvacuationApplicationSavePort.save(
                new EmergencyEvacuationApplicationBuilder()
                        .withValidValues()
                        .withoutId()
                        .withoutInstitution()
                        .withStatus(EmergencyEvacuationApplicationStatus.PENDING)
                        .withoutApplicant()
                        .build()
        );

        // Given
        EmergencyEvacuationApplicationListRequest mockListRequest = new EmergencyEvacuationApplicationListRequestBuilder()
                .withValidValues()
                .withFilter(null)
                .withoutOrders()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, adminToken.getAccessToken(), mockListRequest);

        List<EmergencyEvacuationApplication> applications = List.of(
                new EmergencyEvacuationApplicationBuilder()
                        .withValidValues()
                        .withoutApplicant()
                        .build()
        );
        AysPage<EmergencyEvacuationApplication> applicationsPage = AysPageBuilder
                .from(applications, mockListRequest.getPageable());

        AysPageResponse<EmergencyEvacuationApplication> pageResponse = AysPageResponse.<EmergencyEvacuationApplication>builder()
                .of(applicationsPage)
                .build();

        AysResponse<AysPageResponse<EmergencyEvacuationApplication>> mockResponse = AysResponse.successOf(pageResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.contentSize()
                        .value(1))
                .andExpect(AysMockResultMatchersBuilders.firstContent("id")
                        .value(application.getId()))
                .andExpect(AysMockResultMatchersBuilders.firstContent("referenceNumber")
                        .value(application.getReferenceNumber()))
                .andExpect(AysMockResultMatchersBuilders.firstContent("firstName")
                        .value(application.getFirstName()))
                .andExpect(AysMockResultMatchersBuilders.firstContent("lastName")
                        .value(application.getLastName()))
                .andExpect(AysMockResultMatchersBuilders.firstContent("phoneNumber.countryCode")
                        .value(application.getPhoneNumber().getCountryCode()))
                .andExpect(AysMockResultMatchersBuilders.firstContent("phoneNumber.lineNumber")
                        .value(application.getPhoneNumber().getLineNumber()))
                .andExpect(AysMockResultMatchersBuilders.firstContent("seatingCount")
                        .value(application.getSeatingCount()))
                .andExpect(AysMockResultMatchersBuilders.firstContent("status")
                        .value(application.getStatus().toString()))
                .andExpect(AysMockResultMatchersBuilders.firstContent("isInPerson")
                        .value(application.getIsInPerson()))
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
                .andExpect(AysMockResultMatchersBuilders.response("orderedBy")
                        .isEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("filteredBy")
                        .isEmpty());
    }

    @Test
    void givenValidEmergencyEvacuationApplicationListRequest_whenEmergencyEvacuationApplicationsNotFound_thenReturnAysPageResponseOfEmergencyEvacuationApplicationsResponse() throws Exception {

        // Initialize
        emergencyEvacuationApplicationRepository.deleteAll();
        emergencyEvacuationApplicationSavePort.save(
                new EmergencyEvacuationApplicationBuilder()
                        .withValidValues()
                        .withoutId()
                        .withoutInstitution()
                        .withStatus(EmergencyEvacuationApplicationStatus.PENDING)
                        .withoutApplicant()
                        .build()
        );

        // Given
        EmergencyEvacuationApplicationListRequest mockListRequest = new EmergencyEvacuationApplicationListRequestBuilder()
                .withValidValues()
                .withStatuses(Set.of(EmergencyEvacuationApplicationStatus.COMPLETED))
                .withoutOrders()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, adminToken.getAccessToken(), mockListRequest);

        List<EmergencyEvacuationApplication> applications = List.of(
                new EmergencyEvacuationApplicationBuilder().withValidValues().build()
        );

        AysPage<EmergencyEvacuationApplication> applicationsPage = AysPageBuilder
                .from(applications, mockListRequest.getPageable());

        AysPageResponse<EmergencyEvacuationApplication> pageResponse = AysPageResponse.<EmergencyEvacuationApplication>builder()
                .of(applicationsPage).build();

        AysResponse<AysPageResponse<EmergencyEvacuationApplication>> mockResponse = AysResponse.successOf(pageResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.contentSize()
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
                .andExpect(AysMockResultMatchersBuilders.response("orderedBy")
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
        EmergencyEvacuationApplication application = emergencyEvacuationApplicationSavePort.save(
                new EmergencyEvacuationApplicationBuilder()
                        .withValidValues()
                        .withoutId()
                        .withoutInstitution()
                        .withoutApplicant()
                        .build()
        );

        // Given
        String applicationId = application.getId();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application/").concat(applicationId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, adminToken.getAccessToken());

        EmergencyEvacuationApplicationResponse mockApplicationResponse = emergencyEvacuationApplicationToApplicationResponseMapper
                .map(application);

        AysResponse<EmergencyEvacuationApplicationResponse> mockResponse = AysResponse
                .successOf(mockApplicationResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("id")
                        .value(application.getId()))
                .andExpect(AysMockResultMatchersBuilders.response("referenceNumber")
                        .value(application.getReferenceNumber()))
                .andExpect(AysMockResultMatchersBuilders.response("firstName")
                        .value(application.getFirstName()))
                .andExpect(AysMockResultMatchersBuilders.response("lastName")
                        .value(application.getLastName()))
                .andExpect(AysMockResultMatchersBuilders.response("phoneNumber.countryCode")
                        .value(application.getPhoneNumber().getCountryCode()))
                .andExpect(AysMockResultMatchersBuilders.response("phoneNumber.lineNumber")
                        .value(application.getPhoneNumber().getLineNumber()))
                .andExpect(AysMockResultMatchersBuilders.response("sourceCity")
                        .value(application.getSourceCity()))
                .andExpect(AysMockResultMatchersBuilders.response("sourceDistrict")
                        .value(application.getSourceDistrict()))
                .andExpect(AysMockResultMatchersBuilders.response("address")
                        .value(application.getAddress()))
                .andExpect(AysMockResultMatchersBuilders.response("seatingCount")
                        .value(application.getSeatingCount()))
                .andExpect(AysMockResultMatchersBuilders.response("targetCity")
                        .value(application.getTargetCity()))
                .andExpect(AysMockResultMatchersBuilders.response("targetDistrict")
                        .value(application.getTargetDistrict()))
                .andExpect(AysMockResultMatchersBuilders.response("status")
                        .value(application.getStatus().toString()))
                .andExpect(AysMockResultMatchersBuilders.response("isInPerson")
                        .value(application.getIsInPerson()))
                .andExpect(AysMockResultMatchersBuilders.response("hasObstaclePersonExist")
                        .value(application.getHasObstaclePersonExist()))
                .andExpect(AysMockResultMatchersBuilders.response("notes")
                        .value(application.getNotes()))
                .andExpect(AysMockResultMatchersBuilders.response("createdUser")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("createdAt")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("updatedUser")
                        .isEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("updatedAt")
                        .isEmpty());
    }

    @Test
    void givenValidApplicationId_whenUserUnauthorizedForEmergencyEvacuationApplication_thenReturnAccessDeniedException() throws Exception {

        // When
        String mockApplicationId = AysRandomUtil.generateUUID();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application/").concat(mockApplicationId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, superAdminToken.getAccessToken());

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
                .withValidValues()
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
        AysPageable pageable = AysPageable.builder()
                .page(1)
                .pageSize(1000)
                .build();
        Optional<EmergencyEvacuationApplication> application = emergencyEvacuationApplicationReadPort
                .findAll(pageable, null)
                .getContent().stream()
                .filter(evacuationApplication -> evacuationApplication.getFirstName().equals(applicationRequest.getFirstName()))
                .findFirst();

        Assertions.assertTrue(application.isPresent());
        Assertions.assertNull(application.get().getInstitution());
        Assertions.assertNotNull(application.get().getReferenceNumber());
        Assertions.assertEquals(10, application.get().getReferenceNumber().length());
        Assertions.assertEquals(application.get().getFirstName(), firstName);
        Assertions.assertEquals(application.get().getLastName(), applicationRequest.getLastName());
        Assertions.assertEquals(application.get().getPhoneNumber().getCountryCode(), applicationRequest.getPhoneNumber().getCountryCode());
        Assertions.assertEquals(application.get().getPhoneNumber().getLineNumber(), applicationRequest.getPhoneNumber().getLineNumber());
        Assertions.assertEquals(application.get().getSourceCity(), applicationRequest.getSourceCity());
        Assertions.assertEquals(application.get().getSourceDistrict(), applicationRequest.getSourceDistrict());
        Assertions.assertEquals(application.get().getAddress(), applicationRequest.getAddress());
        Assertions.assertEquals(application.get().getSeatingCount(), applicationRequest.getSeatingCount());
        Assertions.assertEquals(application.get().getTargetCity(), applicationRequest.getTargetCity());
        Assertions.assertEquals(application.get().getTargetDistrict(), applicationRequest.getTargetDistrict());
        Assertions.assertEquals(EmergencyEvacuationApplicationStatus.PENDING, application.get().getStatus());
        Assertions.assertEquals(application.get().getApplicantFirstName(), applicationRequest.getApplicantFirstName());
        Assertions.assertEquals(application.get().getApplicantLastName(), applicationRequest.getApplicantLastName());
        Assertions.assertEquals(application.get().getApplicantPhoneNumber().getCountryCode(), applicationRequest.getApplicantPhoneNumber().getCountryCode());
        Assertions.assertEquals(application.get().getApplicantPhoneNumber().getLineNumber(), applicationRequest.getApplicantPhoneNumber().getLineNumber());
        Assertions.assertFalse(application.get().getIsInPerson());
        Assertions.assertNull(application.get().getHasObstaclePersonExist());
        Assertions.assertNull(application.get().getNotes());
    }

    @Test
    void givenValidEmergencyEvacuationApplicationRequestWithoutApplicant_whenApplicationSaved_thenReturnSuccessResponse() throws Exception {
        // Given
        String firstName = "Test Application without Applicant";
        EmergencyEvacuationApplicationRequest applicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidValues()
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
        AysPageable pageable = AysPageable.builder()
                .page(1)
                .pageSize(1000)
                .build();
        Optional<EmergencyEvacuationApplication> application = emergencyEvacuationApplicationReadPort
                .findAll(pageable, null)
                .getContent().stream()
                .filter(evacuationApplication -> evacuationApplication.getFirstName().equals(applicationRequest.getFirstName()))
                .findFirst();

        Assertions.assertTrue(application.isPresent());
        Assertions.assertNull(application.get().getInstitution());
        Assertions.assertNotNull(application.get().getReferenceNumber());
        Assertions.assertEquals(10, application.get().getReferenceNumber().length());
        Assertions.assertEquals(application.get().getFirstName(), firstName);
        Assertions.assertEquals(application.get().getLastName(), applicationRequest.getLastName());
        Assertions.assertEquals(application.get().getPhoneNumber().getCountryCode(), applicationRequest.getPhoneNumber().getCountryCode());
        Assertions.assertEquals(application.get().getPhoneNumber().getLineNumber(), applicationRequest.getPhoneNumber().getLineNumber());
        Assertions.assertEquals(application.get().getSourceCity(), applicationRequest.getSourceCity());
        Assertions.assertEquals(application.get().getSourceDistrict(), applicationRequest.getSourceDistrict());
        Assertions.assertEquals(application.get().getAddress(), applicationRequest.getAddress());
        Assertions.assertEquals(application.get().getSeatingCount(), applicationRequest.getSeatingCount());
        Assertions.assertEquals(application.get().getTargetCity(), applicationRequest.getTargetCity());
        Assertions.assertEquals(application.get().getTargetDistrict(), applicationRequest.getTargetDistrict());
        Assertions.assertEquals(EmergencyEvacuationApplicationStatus.PENDING, application.get().getStatus());
        Assertions.assertEquals(application.get().getApplicantFirstName(), applicationRequest.getApplicantFirstName());
        Assertions.assertEquals(application.get().getApplicantLastName(), applicationRequest.getApplicantLastName());
        Assertions.assertNull(application.get().getApplicantPhoneNumber().getCountryCode());
        Assertions.assertNull(application.get().getApplicantPhoneNumber().getLineNumber());
        Assertions.assertTrue(application.get().getIsInPerson());
        Assertions.assertNull(application.get().getHasObstaclePersonExist());
        Assertions.assertNull(application.get().getNotes());
    }

}
