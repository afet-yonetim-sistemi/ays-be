package org.ays.emergency_application.controller;

import org.ays.AysEndToEndTest;
import org.ays.common.model.AysPageable;
import org.ays.common.model.AysPageableBuilder;
import org.ays.common.model.response.AysPageResponse;
import org.ays.common.model.response.AysResponse;
import org.ays.common.model.response.AysResponseBuilder;
import org.ays.emergency_application.model.EmergencyEvacuationApplication;
import org.ays.emergency_application.model.EmergencyEvacuationApplicationBuilder;
import org.ays.emergency_application.model.enums.EmergencyEvacuationApplicationStatus;
import org.ays.emergency_application.model.mapper.EmergencyEvacuationApplicationToApplicationResponseMapper;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationListRequest;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationListRequestBuilder;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationRequest;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationUpdateRequest;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationUpdateRequestBuilder;
import org.ays.emergency_application.model.request.EmergencyEvacuationRequestBuilder;
import org.ays.emergency_application.model.response.EmergencyEvacuationApplicationResponse;
import org.ays.emergency_application.port.EmergencyEvacuationApplicationReadPort;
import org.ays.emergency_application.port.EmergencyEvacuationApplicationSavePort;
import org.ays.emergency_application.repository.EmergencyEvacuationApplicationRepository;
import org.ays.institution.model.InstitutionBuilder;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.ays.util.AysValidTestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

class EmergencyEvacuationApplicationEndToEndTest extends AysEndToEndTest {

    @Autowired
    private EmergencyEvacuationApplicationSavePort emergencyEvacuationApplicationSavePort;

    @Autowired
    private EmergencyEvacuationApplicationReadPort emergencyEvacuationApplicationReadPort;

    @Autowired
    private EmergencyEvacuationApplicationRepository emergencyEvacuationApplicationRepository;


    private final EmergencyEvacuationApplicationToApplicationResponseMapper emergencyEvacuationApplicationToApplicationResponseMapper = EmergencyEvacuationApplicationToApplicationResponseMapper.initialize();


    private static final Pattern UUID_REGEX = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    private static final String BASE_PATH = "/api/v1";


    @Test
    void givenValidEmergencyEvacuationApplicationListRequest_whenApplicationsFound_thenReturnAysPageResponseOfEmergencyEvacuationApplicationsResponse() throws Exception {

        // Initialize
        emergencyEvacuationApplicationRepository.deleteAll();

        List<EmergencyEvacuationApplication> applications = List.of(
                emergencyEvacuationApplicationSavePort.save(
                        new EmergencyEvacuationApplicationBuilder()
                                .withValidValues()
                                .withoutId()
                                .withoutInstitution()
                                .withStatus(EmergencyEvacuationApplicationStatus.PENDING)
                                .withoutApplicant()
                                .build()
                ),
                emergencyEvacuationApplicationSavePort.save(
                        new EmergencyEvacuationApplicationBuilder()
                                .withValidValues()
                                .withoutId()
                                .withInstitution(
                                        new InstitutionBuilder()
                                                .withValidValues()
                                                .withId(AysValidTestData.Admin.INSTITUTION_ID)
                                                .build()
                                )
                                .withStatus(EmergencyEvacuationApplicationStatus.PENDING)
                                .withoutApplicant()
                                .build()
                )
        );
        applications.forEach(application -> emergencyEvacuationApplicationSavePort.save(application));

        // Given
        AysPageable mockAysPageable = new AysPageableBuilder()
                .withPage(1)
                .withPageSize(10)
                .build();
        EmergencyEvacuationApplicationListRequest mockListRequest = new EmergencyEvacuationApplicationListRequestBuilder()
                .withValidValues()
                .withPageable(mockAysPageable)
                .withFilter(null)
                .withoutOrders()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, adminToken.getAccessToken(), mockListRequest);

        AysResponse<AysPageResponse<EmergencyEvacuationApplication>> mockResponse = AysResponseBuilder.successPage();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.contentSize()
                        .value(applications.size()))
                .andExpect(AysMockResultMatchersBuilders.contents("id")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.contents("referenceNumber")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.contents("firstName")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.contents("lastName")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.contents("phoneNumber.countryCode")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.contents("phoneNumber.lineNumber")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.contents("applicantFirstName")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.contents("applicantLastName")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.contents("applicantPhoneNumber.countryCode")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.contents("applicantPhoneNumber.lineNumber")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.contents("seatingCount")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.contents("status")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.contents("isInPerson")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.contents("createdAt")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("pageNumber")
                        .value(1))
                .andExpect(AysMockResultMatchersBuilders.response("pageSize")
                        .value(applications.size()))
                .andExpect(AysMockResultMatchersBuilders.response("totalPageCount")
                        .value(1))
                .andExpect(AysMockResultMatchersBuilders.response("totalElementCount")
                        .value(applications.size()))
                .andExpect(AysMockResultMatchersBuilders.response("orderedBy")
                        .isEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("filteredBy")
                        .isNotEmpty());
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

        AysResponse<AysPageResponse<EmergencyEvacuationApplication>> mockResponse = AysResponseBuilder.successPage();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.contentSize()
                        .value(0))
                .andExpect(AysMockResultMatchersBuilders.contents("id")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.contents("referenceNumber")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.contents("firstName")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.contents("lastName")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.contents("phoneNumber.countryCode")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.contents("phoneNumber.lineNumber")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.contents("applicantFirstName")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.contents("applicantLastName")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.contents("applicantPhoneNumber.countryCode")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.contents("applicantPhoneNumber.lineNumber")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.contents("seatingCount")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.contents("status")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.contents("isInPerson")
                        .doesNotExist())
                .andExpect(AysMockResultMatchersBuilders.contents("createdAt")
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
                .post(endpoint, adminToken.getAccessToken(), applicationRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.success();
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
        Assertions.assertTrue(UUID_REGEX.matcher(application.get().getCreatedUser()).matches());
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
                .post(endpoint, adminToken.getAccessToken(), applicationRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.success();
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
        Assertions.assertTrue(UUID_REGEX.matcher(application.get().getCreatedUser()).matches());
    }


    @Test
    void givenValidIdAndValidUpdateRequest_whenApplicationUpdated_thenReturnSuccessResponse() throws Exception {

        // Initialize
        emergencyEvacuationApplicationRepository.deleteAll();
        EmergencyEvacuationApplication application = emergencyEvacuationApplicationSavePort.save(
                new EmergencyEvacuationApplicationBuilder()
                        .withValidValues()
                        .withoutId()
                        .withoutInstitution()
                        .withoutApplicant()
                        .withSeatingCount(5)
                        .withStatus(EmergencyEvacuationApplicationStatus.PENDING)
                        .withoutHasObstaclePersonExist()
                        .withoutNotes()
                        .build()
        );

        // Given
        String id = application.getId();
        EmergencyEvacuationApplicationUpdateRequest updateRequest = new EmergencyEvacuationApplicationUpdateRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application/").concat(id);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, adminToken.getAccessToken(), updateRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(MockMvcResultMatchers.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Optional<EmergencyEvacuationApplication> applicationFromDatabase = emergencyEvacuationApplicationReadPort
                .findById(id);

        Assertions.assertTrue(applicationFromDatabase.isPresent());
        Assertions.assertNotNull(applicationFromDatabase.get().getInstitution());
        Assertions.assertEquals(applicationFromDatabase.get().getSeatingCount(), updateRequest.getSeatingCount());
        Assertions.assertEquals(applicationFromDatabase.get().getHasObstaclePersonExist(), updateRequest.getHasObstaclePersonExist());
        Assertions.assertEquals(applicationFromDatabase.get().getStatus(), updateRequest.getStatus());
        Assertions.assertEquals(applicationFromDatabase.get().getNotes(), updateRequest.getNotes());
        Assertions.assertNotNull(applicationFromDatabase.get().getUpdatedUser());
        Assertions.assertNotNull(applicationFromDatabase.get().getUpdatedAt());
        Assertions.assertTrue(UUID_REGEX.matcher(applicationFromDatabase.get().getUpdatedUser()).matches());
    }

}
