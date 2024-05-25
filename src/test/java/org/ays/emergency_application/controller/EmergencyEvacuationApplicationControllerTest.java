package org.ays.emergency_application.controller;

import org.ays.AbstractRestControllerTest;
import org.ays.common.model.dto.request.AysPhoneNumberRequest;
import org.ays.common.model.dto.request.AysPhoneNumberRequestBuilder;
import org.ays.common.model.dto.response.AysResponse;
import org.ays.common.model.dto.response.AysResponseBuilder;
import org.ays.common.util.exception.model.AysErrorBuilder;
import org.ays.common.util.exception.model.AysErrorResponse;
import org.ays.emergency_application.model.dto.request.EmergencyEvacuationApplicationRequest;
import org.ays.emergency_application.model.dto.request.EmergencyEvacuationRequestBuilder;
import org.ays.emergency_application.service.EmergencyEvacuationApplicationService;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

class EmergencyEvacuationApplicationControllerTest extends AbstractRestControllerTest {

    @MockBean
    private EmergencyEvacuationApplicationService emergencyEvacuationApplicationService;


    private final String BASE_PATH = "/api/v1";

    @Test
    void givenValidEmergencyEvacuationApplicationRequest_whenApplicationSaved_thenReturnSuccessResponse() throws Exception {
        // Given
        EmergencyEvacuationApplicationRequest mockApplicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidFields()
                .build();

        // When
        Mockito.doNothing()
                .when(emergencyEvacuationApplicationService)
                .create(Mockito.any(EmergencyEvacuationApplicationRequest.class));

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;
        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.times(1))
                .create(Mockito.any(EmergencyEvacuationApplicationRequest.class));
    }

    @Test
    void givenValidEmergencyEvacuationApplicationRequestWithoutApplicant_whenApplicationSaved_thenReturnSuccessResponse() throws Exception {
        // Given
        EmergencyEvacuationApplicationRequest mockApplicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidFields()
                .withoutApplicant()
                .build();

        // When
        Mockito.doNothing()
                .when(emergencyEvacuationApplicationService)
                .create(Mockito.any(EmergencyEvacuationApplicationRequest.class));

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;
        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.times(1))
                .create(Mockito.any(EmergencyEvacuationApplicationRequest.class));
    }

    @Test
    void givenInvalidEmergencyEvacuationApplicationRequest_whenPhoneNumbersAreSameOne_thenReturnValidationError() throws Exception {
        // Given
        AysPhoneNumberRequest mockPhoneNumberRequest = new AysPhoneNumberRequestBuilder()
                .withValidFields()
                .build();
        EmergencyEvacuationApplicationRequest mockApplicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidFields()
                .withPhoneNumber(mockPhoneNumberRequest)
                .withApplicantPhoneNumber(mockPhoneNumberRequest)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;
        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .create(Mockito.any(EmergencyEvacuationApplicationRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "Invalid with special characters: #$%",
            "#$½#$£#$£#$$#½#£$£#$#£½#$½#$½$£#$#£$$#½#$$½",
            ".,..,.,.,.,.,,.,.,.,.,.,.,.,.,..,.,.,,.,.,.,",
            "t",
            "                                      a",
            "151201485621548562154851458614125461254125412",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices.",
    })
    void givenInvalidEmergencyEvacuationApplicationRequest_whenFirstNameIsNotValid_thenReturnValidationError(String firstName) throws Exception {
        // Given
        EmergencyEvacuationApplicationRequest mockApplicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidFields()
                .withFirstName(firstName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;
        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .create(Mockito.any(EmergencyEvacuationApplicationRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "Invalid with special characters: #$%",
            "#$½#$£#$£#$$#½#£$£#$#£½#$½#$½$£#$#£$$#½#$$½",
            ".,..,.,.,.,.,,.,.,.,.,.,.,.,.,..,.,.,,.,.,.,",
            "t",
            "                                      a",
            "151201485621548562154851458614125461254125412",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices.",
    })
    void givenInvalidEmergencyEvacuationApplicationRequest_whenLastNameIsNotValid_thenReturnValidationError(String lastName) throws Exception {
        // Given
        EmergencyEvacuationApplicationRequest mockApplicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidFields()
                .withLastName(lastName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;
        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .create(Mockito.any(EmergencyEvacuationApplicationRequest.class));
    }

    @Test
    void givenInvalidEmergencyEvacuationApplicationRequest_whenPhoneNumberIsNotValid_thenReturnValidationError() throws Exception {
        // Given
        AysPhoneNumberRequest mockPhoneNumberRequest = new AysPhoneNumberRequestBuilder()
                .withCountryCode("456786745645")
                .withLineNumber("6546467456435548676845321346656654").build();
        EmergencyEvacuationApplicationRequest mockApplicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidFields()
                .withPhoneNumber(mockPhoneNumberRequest)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;
        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .create(Mockito.any(EmergencyEvacuationApplicationRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "Invalid with special characters: #$%",
            "#$½#$£#$£#$$#½#£$£#$#£½#$½#$½$£#$#£$$#½#$$½",
            ".,..,.,.,.,.,,.,.,.,.,.,.,.,.,..,.,.,,.,.,.,",
            "t",
            "                                      a",
            "151201485621548562154851458614125461254125412",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices.",
    })
    void givenInvalidEmergencyEvacuationApplicationRequest_whenSourceCityIsNotValid_thenReturnValidationError(String sourceCity) throws Exception {
        // Given
        EmergencyEvacuationApplicationRequest mockApplicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidFields()
                .withSourceCity(sourceCity)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;
        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .create(Mockito.any(EmergencyEvacuationApplicationRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "Invalid with special characters: #$%",
            "#$½#$£#$£#$$#½#£$£#$#£½#$½#$½$£#$#£$$#½#$$½",
            ".,..,.,.,.,.,,.,.,.,.,.,.,.,.,..,.,.,,.,.,.,",
            "t",
            "                                      a",
            "151201485621548562154851458614125461254125412",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices.",
    })
    void givenInvalidEmergencyEvacuationApplicationRequest_whenSourceDistrictIsNotValid_thenReturnValidationError(String sourceDistrict) throws Exception {
        // Given
        EmergencyEvacuationApplicationRequest mockApplicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidFields()
                .withSourceDistrict(sourceDistrict)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;
        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .create(Mockito.any(EmergencyEvacuationApplicationRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices.",
    })
    void givenInvalidEmergencyEvacuationApplicationRequest_whenAddressIsNotValid_thenReturnValidationError(String address) throws Exception {
        // Given
        EmergencyEvacuationApplicationRequest mockApplicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidFields()
                .withAddress(address)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;
        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .create(Mockito.any(EmergencyEvacuationApplicationRequest.class));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1000,})
    void givenInvalidEmergencyEvacuationApplicationRequest_whenAddressIsNotValid_thenReturnValidationError(Integer seatingCount) throws Exception {
        // Given
        EmergencyEvacuationApplicationRequest mockApplicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidFields()
                .withSeatingCount(seatingCount)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;
        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .create(Mockito.any(EmergencyEvacuationApplicationRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "Invalid with special characters: #$%",
            "#$½#$£#$£#$$#½#£$£#$#£½#$½#$½$£#$#£$$#½#$$½",
            ".,..,.,.,.,.,,.,.,.,.,.,.,.,.,..,.,.,,.,.,.,",
            "t",
            "                                      a",
            "151201485621548562154851458614125461254125412",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices.",
    })
    void givenInvalidEmergencyEvacuationApplicationRequest_whenTargetCityIsNotValid_thenReturnValidationError(String targetCity) throws Exception {
        // Given
        EmergencyEvacuationApplicationRequest mockApplicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidFields()
                .withTargetCity(targetCity)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;
        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .create(Mockito.any(EmergencyEvacuationApplicationRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "Invalid with special characters: #$%",
            "#$½#$£#$£#$$#½#£$£#$#£½#$½#$½$£#$#£$$#½#$$½",
            ".,..,.,.,.,.,,.,.,.,.,.,.,.,.,..,.,.,,.,.,.,",
            "t",
            "                                      a",
            "151201485621548562154851458614125461254125412",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices.",
    })
    void givenInvalidEmergencyEvacuationApplicationRequest_whenSourceTargetIsNotValid_thenReturnValidationError(String targetDistrict) throws Exception {
        // Given
        EmergencyEvacuationApplicationRequest mockApplicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidFields()
                .withTargetDistrict(targetDistrict)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;
        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .create(Mockito.any(EmergencyEvacuationApplicationRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Invalid with special characters: #$%",
            "#$½#$£#$£#$$#½#£$£#$#£½#$½#$½$£#$#£$$#½#$$½",
            ".,..,.,.,.,.,,.,.,.,.,.,.,.,.,..,.,.,,.,.,.,",
            "t",
            "                                      a",
            "151201485621548562154851458614125461254125412",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices.",
    })
    void givenInvalidEmergencyEvacuationApplicationRequest_whenApplicantFirstNameIsNotValid_thenReturnValidationError(String applicantFirstName) throws Exception {
        // Given
        EmergencyEvacuationApplicationRequest mockApplicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidFields()
                .withApplicantFirstName(applicantFirstName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;
        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .create(Mockito.any(EmergencyEvacuationApplicationRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Invalid with special characters: #$%",
            "#$½#$£#$£#$$#½#£$£#$#£½#$½#$½$£#$#£$$#½#$$½",
            ".,..,.,.,.,.,,.,.,.,.,.,.,.,.,..,.,.,,.,.,.,",
            "t",
            "                                      a",
            "151201485621548562154851458614125461254125412",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices.",
    })
    void givenInvalidEmergencyEvacuationApplicationRequest_whenApplicantLastNameIsNotValid_thenReturnValidationError(String applicantLastName) throws Exception {
        // Given
        EmergencyEvacuationApplicationRequest mockApplicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidFields()
                .withApplicantLastName(applicantLastName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;
        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .create(Mockito.any(EmergencyEvacuationApplicationRequest.class));
    }

    @Test
    void givenInvalidEmergencyEvacuationApplicationRequest_whenApplicantPhoneNumberIsNotValid_thenReturnValidationError() throws Exception {
        // Given
        AysPhoneNumberRequest mockPhoneNumberRequest = new AysPhoneNumberRequestBuilder()
                .withCountryCode("456786745645")
                .withLineNumber("6546467456435548676845321346656654").build();
        EmergencyEvacuationApplicationRequest mockApplicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidFields()
                .withApplicantPhoneNumber(mockPhoneNumberRequest)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;
        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .create(Mockito.any(EmergencyEvacuationApplicationRequest.class));
    }

    @Test
    void givenInvalidEmergencyEvacuationApplicationRequest_whenAllApplicantFieldsAreNotFilled_thenReturnValidationError() throws Exception {
        // Given
        EmergencyEvacuationApplicationRequest mockApplicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidFields()
                .withApplicantPhoneNumber(null)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;
        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .create(Mockito.any(EmergencyEvacuationApplicationRequest.class));
    }

}
