package org.ays.emergency_application.controller;

import org.ays.AbstractRestControllerTest;
import org.ays.common.model.dto.response.AysResponse;
import org.ays.common.model.dto.response.AysResponseBuilder;
import org.ays.emergency_application.model.dto.request.EmergencyEvacuationApplicationRequest;
import org.ays.emergency_application.model.dto.request.EmergencyEvacuationRequestBuilder;
import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationEntity;
import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationStatus;
import org.ays.emergency_application.repository.EmergencyEvacuationApplicationRepositoryTest;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Optional;

class EmergencyEvacuationApplicationSystemTest extends AbstractRestControllerTest {

    @Autowired
    private EmergencyEvacuationApplicationRepositoryTest emergencyEvacuationApplicationRepository;


    private final String BASE_PATH = "/api/v1";

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
