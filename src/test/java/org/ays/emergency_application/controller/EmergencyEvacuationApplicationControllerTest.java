package org.ays.emergency_application.controller;

import org.ays.AysRestControllerTest;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageBuilder;
import org.ays.common.model.request.AysPhoneNumberRequest;
import org.ays.common.model.request.AysPhoneNumberRequestBuilder;
import org.ays.common.model.response.AysErrorResponse;
import org.ays.common.model.response.AysErrorResponseBuilder;
import org.ays.common.model.response.AysPageResponse;
import org.ays.common.model.response.AysResponse;
import org.ays.common.model.response.AysResponseBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.emergency_application.model.EmergencyEvacuationApplication;
import org.ays.emergency_application.model.EmergencyEvacuationApplicationBuilder;
import org.ays.emergency_application.model.enums.EmergencyEvacuationApplicationStatus;
import org.ays.emergency_application.model.mapper.EmergencyEvacuationApplicationToApplicationResponseMapper;
import org.ays.emergency_application.model.mapper.EmergencyEvacuationApplicationToApplicationsResponseMapper;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationListRequest;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationListRequestBuilder;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationRequest;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationUpdateRequest;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationUpdateRequestBuilder;
import org.ays.emergency_application.model.request.EmergencyEvacuationRequestBuilder;
import org.ays.emergency_application.model.response.EmergencyEvacuationApplicationResponse;
import org.ays.emergency_application.model.response.EmergencyEvacuationApplicationsResponse;
import org.ays.emergency_application.service.EmergencyEvacuationApplicationService;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

class EmergencyEvacuationApplicationControllerTest extends AysRestControllerTest {

    @MockBean
    private EmergencyEvacuationApplicationService emergencyEvacuationApplicationService;


    private final EmergencyEvacuationApplicationToApplicationsResponseMapper emergencyEvacuationApplicationToApplicationsResponseMapper = EmergencyEvacuationApplicationToApplicationsResponseMapper.initialize();
    private final EmergencyEvacuationApplicationToApplicationResponseMapper emergencyEvacuationApplicationToApplicationResponseMapper = EmergencyEvacuationApplicationToApplicationResponseMapper.initialize();

    private static final String BASE_PATH = "/api/v1";

    @Test
    void givenValidEmergencyEvacuationApplicationListRequest_whenEmergencyEvacuationApplicationsFound_thenReturnAysPageResponseOfEmergencyEvacuationApplicationsResponse() throws Exception {

        // Given
        EmergencyEvacuationApplicationListRequest mockListRequest = new EmergencyEvacuationApplicationListRequestBuilder()
                .withValidValues()
                .build();

        // When
        List<EmergencyEvacuationApplication> mockApplications = List.of(
                new EmergencyEvacuationApplicationBuilder()
                        .withValidValues()
                        .withoutApplicant()
                        .build()
        );
        AysPage<EmergencyEvacuationApplication> mockApplicationsPage = AysPageBuilder
                .from(mockApplications, mockListRequest.getPageable());

        Mockito.when(emergencyEvacuationApplicationService.findAll(Mockito.any(EmergencyEvacuationApplicationListRequest.class)))
                .thenReturn(mockApplicationsPage);

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminToken.getAccessToken(), mockListRequest);

        List<EmergencyEvacuationApplicationsResponse> mockApplicationsResponse = emergencyEvacuationApplicationToApplicationsResponseMapper.map(mockApplications);
        AysPageResponse<EmergencyEvacuationApplicationsResponse> pageOfResponse = AysPageResponse.<EmergencyEvacuationApplicationsResponse>builder()
                .of(mockApplicationsPage)
                .content(mockApplicationsResponse)
                .build();
        AysResponse<AysPageResponse<EmergencyEvacuationApplicationsResponse>> mockResponse = AysResponse
                .successOf(pageOfResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.times(1))
                .findAll(Mockito.any(EmergencyEvacuationApplicationListRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "151201485621548562154851458614125461254125412"
    })
    void givenInvalidEmergencyEvacuationApplicationListRequest_whenReferenceNumberNotValid_thenReturnValidationError(String referenceNumber) throws Exception {

        // Given
        EmergencyEvacuationApplicationListRequest mockListRequest = new EmergencyEvacuationApplicationListRequestBuilder()
                .withValidValues()
                .withReferenceNumber(referenceNumber)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminToken.getAccessToken(), mockListRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .findAll(Mockito.any(EmergencyEvacuationApplicationListRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "Invalid with special characters: #$%",
            "#$½#$£#$£#$$#½#£$£#$#£½#$½#$½$£#$#£$$#½#$$½",
            ".,..,.,.,.,.,,.,.,.,.,.,.,.,.,..,.,.,,.,.,.,",
            "t",
            "151201485621548562154851458614125461254125412",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices."
    })
    void givenInvalidEmergencyEvacuationApplicationListRequest_whenSourceCityNotValid_thenReturnValidationError(String sourceCity) throws Exception {

        // Given
        EmergencyEvacuationApplicationListRequest mockListRequest = new EmergencyEvacuationApplicationListRequestBuilder()
                .withValidValues()
                .withSourceCity(sourceCity)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminToken.getAccessToken(), mockListRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .findAll(Mockito.any(EmergencyEvacuationApplicationListRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "Invalid with special characters: #$%",
            "#$½#$£#$£#$$#½#£$£#$#£½#$½#$½$£#$#£$$#½#$$½",
            ".,..,.,.,.,.,,.,.,.,.,.,.,.,.,..,.,.,,.,.,.,",
            "t",
            "151201485621548562154851458614125461254125412",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices."
    })
    void givenInvalidEmergencyEvacuationApplicationListRequest_whenSourceDistrictNotValid_thenReturnValidationError(String sourceDistrict) throws Exception {

        // Given
        EmergencyEvacuationApplicationListRequest mockListRequest = new EmergencyEvacuationApplicationListRequestBuilder()
                .withValidValues()
                .withSourceDistrict(sourceDistrict)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminToken.getAccessToken(), mockListRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .findAll(Mockito.any(EmergencyEvacuationApplicationListRequest.class));
    }

    @ParameterizedTest
    @ValueSource(ints = {
            -1,
            0,
            1000
    })
    void givenInvalidEmergencyEvacuationApplicationListRequest_whenSeatingCountNotValid_thenReturnValidationError(Integer seatingCount) throws Exception {

        // Given
        EmergencyEvacuationApplicationListRequest mockListRequest = new EmergencyEvacuationApplicationListRequestBuilder()
                .withValidValues()
                .withSeatingCount(seatingCount)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminToken.getAccessToken(), mockListRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .findAll(Mockito.any(EmergencyEvacuationApplicationListRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "Invalid with special characters: #$%",
            "#$½#$£#$£#$$#½#£$£#$#£½#$½#$½$£#$#£$$#½#$$½",
            ".,..,.,.,.,.,,.,.,.,.,.,.,.,.,..,.,.,,.,.,.,",
            "t",
            "151201485621548562154851458614125461254125412",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices."
    })
    void givenInvalidEmergencyEvacuationApplicationListRequest_whenTargetCityNotValid_thenReturnValidationError(String targetCity) throws Exception {

        // Given
        EmergencyEvacuationApplicationListRequest mockListRequest = new EmergencyEvacuationApplicationListRequestBuilder()
                .withValidValues()
                .withTargetCity(targetCity)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminToken.getAccessToken(), mockListRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .findAll(Mockito.any(EmergencyEvacuationApplicationListRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "Invalid with special characters: #$%",
            "#$½#$£#$£#$$#½#£$£#$#£½#$½#$½$£#$#£$$#½#$$½",
            ".,..,.,.,.,.,,.,.,.,.,.,.,.,.,..,.,.,,.,.,.,",
            "t",
            "151201485621548562154851458614125461254125412",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices."
    })
    void givenInvalidEmergencyEvacuationApplicationListRequest_whenTargetDistrictNotValid_thenReturnValidationError(String targetDistrict) throws Exception {

        // Given
        EmergencyEvacuationApplicationListRequest mockListRequest = new EmergencyEvacuationApplicationListRequestBuilder()
                .withValidValues()
                .withTargetDistrict(targetDistrict)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminToken.getAccessToken(), mockListRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .findAll(Mockito.any(EmergencyEvacuationApplicationListRequest.class));
    }

    @Test
    void givenValidEmergencyEvacuationApplicationListRequest_whenUnauthorizedForListing_thenReturnAccessDeniedException() throws Exception {
        // Given
        EmergencyEvacuationApplicationListRequest mockListRequest = new EmergencyEvacuationApplicationListRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken(), mockListRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .findAll(Mockito.any(EmergencyEvacuationApplicationListRequest.class));
    }

    @Test
    void givenValidEmergencyEvacuationApplicationId_whenEmergencyEvacuationApplicationFound_thenReturnEmergencyEvacuationApplicationResponse() throws Exception {

        // Given
        String mockApplicationId = AysRandomUtil.generateUUID();

        // When
        EmergencyEvacuationApplication mockEmergencyEvacuationApplication = new EmergencyEvacuationApplicationBuilder()
                .withValidValues()
                .withId(mockApplicationId)
                .build();

        Mockito.when(emergencyEvacuationApplicationService.findById(mockApplicationId))
                .thenReturn(mockEmergencyEvacuationApplication);

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application/").concat(mockApplicationId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockAdminToken.getAccessToken());

        EmergencyEvacuationApplicationResponse mockEmergencyEvacuationApplicationResponse = emergencyEvacuationApplicationToApplicationResponseMapper
                .map(mockEmergencyEvacuationApplication);
        AysResponse<EmergencyEvacuationApplicationResponse> mockResponse = AysResponse
                .successOf(mockEmergencyEvacuationApplicationResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.times(1))
                .findById(mockApplicationId);
    }

    @Test
    void givenEmergencyEvacuationApplicationId_whenUnauthorizedForGettingEmergencyEvacuationApplicationById_thenReturnAccessDeniedException() throws Exception {

        // Given
        String mockApplicationId = AysRandomUtil.generateUUID();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application/".concat(mockApplicationId));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockUserToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .findById(mockApplicationId);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Invalid with special characters: #$%",
            ".,..,.,.,.,.,,.,.,.,.,.,.,.,.,..,.,.,,.,.,.,",
            "J----",
            "City--King",
            "John  Doe",
            "t",
            "                                      a",
            "151201485621548562154851458614125461254125412",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse.",
    })
    void givenId_whenIdDoesNotValid_thenReturnValidationError(String invalidId) throws Exception {

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application/".concat(invalidId));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockAdminToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .exists());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .findById(invalidId);
    }


    @Test
    void givenValidEmergencyEvacuationApplicationRequest_whenApplicationSaved_thenReturnSuccessResponse() throws Exception {
        // Given
        EmergencyEvacuationApplicationRequest mockApplicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.doNothing()
                .when(emergencyEvacuationApplicationService)
                .create(Mockito.any(EmergencyEvacuationApplicationRequest.class));

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.success();
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
                .withValidValues()
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

        AysResponse<Void> mockResponse = AysResponseBuilder.success();
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
                .withValidValues()
                .build();
        EmergencyEvacuationApplicationRequest mockApplicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidValues()
                .withPhoneNumber(mockPhoneNumberRequest)
                .withApplicantPhoneNumber(mockPhoneNumberRequest)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;
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
            "20",
            "80",
             ""
    })
    void givenInvalidEmergencyEvacuationApplicationRequest_whenPhoneNumberCountryCodeIsNotValid_thenReturnValidationError(String countryCode) throws Exception {
        // Given
        AysPhoneNumberRequest mockPhoneNumberRequest = new AysPhoneNumberRequestBuilder()
                .withValidValues()
                .withCountryCode(countryCode)
                .build();
        EmergencyEvacuationApplicationRequest mockApplicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidValues()
                .withPhoneNumber(mockPhoneNumberRequest)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;
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
            "J----",
            "City--King",
            "John  Doe",
            "t",
            "                                      a",
            "151201485621548562154851458614125461254125412",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices.",
    })
    void givenInvalidEmergencyEvacuationApplicationRequest_whenFirstNameIsNotValid_thenReturnValidationError(String firstName) throws Exception {
        // Given
        EmergencyEvacuationApplicationRequest mockApplicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidValues()
                .withFirstName(firstName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;
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
            "J----",
            "City--King",
            "John  Doe",
            "t",
            "                                      a",
            "151201485621548562154851458614125461254125412",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices.",
    })
    void givenInvalidEmergencyEvacuationApplicationRequest_whenLastNameIsNotValid_thenReturnValidationError(String lastName) throws Exception {
        // Given
        EmergencyEvacuationApplicationRequest mockApplicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidValues()
                .withLastName(lastName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;
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
                .withValidValues()
                .withPhoneNumber(mockPhoneNumberRequest)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;
        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.subErrors("[0].message")
                        .value("must be valid"))
                .andExpect(AysMockResultMatchersBuilders.subErrors("[0].field")
                        .value("phoneNumber"))
                .andExpect(AysMockResultMatchersBuilders.subErrors("[0].value")
                        .value(mockPhoneNumberRequest.getCountryCode() + mockPhoneNumberRequest.getLineNumber()));

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .create(Mockito.any(EmergencyEvacuationApplicationRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "Mary*land",
            "Mary$land",
            "CityName$",
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
                .withValidValues()
                .withSourceCity(sourceCity)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;
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
            "Mary*land",
            "Mary$land",
            "CityName$",
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
                .withValidValues()
                .withSourceDistrict(sourceDistrict)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;
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
            "Lorem ipsum",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices.",
    })
    void givenInvalidEmergencyEvacuationApplicationRequest_whenAddressIsNotValid_thenReturnValidationError(String address) throws Exception {
        // Given
        EmergencyEvacuationApplicationRequest mockApplicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidValues()
                .withAddress(address)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;
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
                .withValidValues()
                .withSeatingCount(seatingCount)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;
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
            "Mary*land",
            "Mary$land",
            "CityName$",
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
                .withValidValues()
                .withTargetCity(targetCity)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;
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
            "Mary*land",
            "Mary$land",
            "CityName$",
            "Invalid with special characters: #$%",
            "#$½#$£#$£#$$#½#£$£#$#£½#$½#$½$£#$#£$$#½#$$½",
            ".,..,.,.,.,.,,.,.,.,.,.,.,.,.,..,.,.,,.,.,.,",
            "t",
            "                                      a",
            "151201485621548562154851458614125461254125412",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices.",
    })
    void givenInvalidEmergencyEvacuationApplicationRequest_whenTargetDistrictIsNotValid_thenReturnValidationError(String targetDistrict) throws Exception {
        // Given
        EmergencyEvacuationApplicationRequest mockApplicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidValues()
                .withTargetDistrict(targetDistrict)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;
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
            "    ",
            "Invalid with special characters: #$%",
            "#$½#$£#$£#$$#½#£$£#$#£½#$½#$½$£#$#£$$#½#$$½",
            ".,..,.,.,.,.,,.,.,.,.,.,.,.,.,..,.,.,,.,.,.,",
            "t",
            "J----",
            "City--King",
            "John  Doe",
            "                                      a",
            "151201485621548562154851458614125461254125412",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices.",
    })
    void givenInvalidEmergencyEvacuationApplicationRequest_whenApplicantFirstNameIsNotValid_thenReturnValidationError(String applicantFirstName) throws Exception {
        // Given
        EmergencyEvacuationApplicationRequest mockApplicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidValues()
                .withApplicantFirstName(applicantFirstName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;
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
            "    ",
            "Invalid with special characters: #$%",
            "#$½#$£#$£#$$#½#£$£#$#£½#$½#$½$£#$#£$$#½#$$½",
            ".,..,.,.,.,.,,.,.,.,.,.,.,.,.,..,.,.,,.,.,.,",
            "t",
            "J----",
            "City--King",
            "John  Doe",
            "                                      a",
            "151201485621548562154851458614125461254125412",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices.",
    })
    void givenInvalidEmergencyEvacuationApplicationRequest_whenApplicantLastNameIsNotValid_thenReturnValidationError(String applicantLastName) throws Exception {
        // Given
        EmergencyEvacuationApplicationRequest mockApplicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidValues()
                .withApplicantLastName(applicantLastName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;
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
                .withValidValues()
                .withApplicantPhoneNumber(mockPhoneNumberRequest)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;
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
                .withValidValues()
                .withApplicantPhoneNumber(null)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;
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
            " ",
            "İstanbul",
    })
    void givenEmergencyEvacuationApplicationRequest_whenSourceAndTargetCityOrDistrictAreBlankOrEmptyOrTheSame_thenReturnValidationError(String name) throws Exception {
        // Given
        EmergencyEvacuationApplicationRequest mockApplicationRequest = new EmergencyEvacuationRequestBuilder()
                .withValidValues()
                .withSourceCity(name)
                .withSourceDistrict(name)
                .withTargetCity(name)
                .withTargetDistrict(name)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockApplicationRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;
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
    void givenValidIdAndValidUpdateRequest_whenApplicationUpdated_thenReturnSuccessResponse() throws Exception {
        // Given
        String mockId = "dbb3287a-563d-4d85-a978-bcd699294daa";
        EmergencyEvacuationApplicationUpdateRequest mockUpdateRequest = new EmergencyEvacuationApplicationUpdateRequestBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.doNothing()
                .when(emergencyEvacuationApplicationService)
                .update(Mockito.anyString(), Mockito.any(EmergencyEvacuationApplicationUpdateRequest.class));

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminToken.getAccessToken(), mockUpdateRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(MockMvcResultMatchers.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.times(1))
                .update(Mockito.anyString(), Mockito.any(EmergencyEvacuationApplicationUpdateRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Invalid with special characters: #$%",
            ".,..,.,.,.,.,,.,.,.,.,.,.,.,.,..,.,.,,.,.,.,",
            "t",
            "                                      a",
            "151201485621548562154851458614125461254125412",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse.",
    })
    void givenIdAndValidUpdateRequest_whenIdDoesNotValid_thenReturnValidationError(String mockId) throws Exception {

        // Given
        EmergencyEvacuationApplicationUpdateRequest mockUpdateRequest = new EmergencyEvacuationApplicationUpdateRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application/".concat(mockId));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockAdminToken.getAccessToken(), mockUpdateRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .exists());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .findById(Mockito.anyString());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(ints = {
            -1,
            0,
            1000
    })
    void givenValidIdAndUpdateRequest_whenSeatingCountDoesNotValid_thenReturnValidationError(Integer mockSeatingCount) throws Exception {
        // Given
        String mockId = "dbb3287a-563d-4d85-a978-bcd699294daa";
        EmergencyEvacuationApplicationUpdateRequest mockUpdateRequest = new EmergencyEvacuationApplicationUpdateRequestBuilder()
                .withValidValues()
                .withSeatingCount(mockSeatingCount)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminToken.getAccessToken(), mockUpdateRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .update(Mockito.anyString(), Mockito.any(EmergencyEvacuationApplicationUpdateRequest.class));
    }

    @ParameterizedTest
    @NullSource
    void givenValidIdAndUpdateRequest_whenHasObstaclePersonExistDoesNotValid_thenReturnValidationError(Boolean mockHasObstaclePersonExist) throws Exception {
        // Given
        String mockId = "dbb3287a-563d-4d85-a978-bcd699294daa";
        EmergencyEvacuationApplicationUpdateRequest mockUpdateRequest = new EmergencyEvacuationApplicationUpdateRequestBuilder()
                .withValidValues()
                .withHasObstaclePersonExist(mockHasObstaclePersonExist)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminToken.getAccessToken(), mockUpdateRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .update(Mockito.anyString(), Mockito.any(EmergencyEvacuationApplicationUpdateRequest.class));
    }

    @ParameterizedTest
    @NullSource
    void givenValidIdAndUpdateRequest_whenStatusDoesNotValid_thenReturnValidationError(EmergencyEvacuationApplicationStatus mockStatus) throws Exception {
        // Given
        String mockId = "dbb3287a-563d-4d85-a978-bcd699294daa";
        EmergencyEvacuationApplicationUpdateRequest mockUpdateRequest = new EmergencyEvacuationApplicationUpdateRequestBuilder()
                .withValidValues()
                .withStatus(mockStatus)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminToken.getAccessToken(), mockUpdateRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .update(Mockito.anyString(), Mockito.any(EmergencyEvacuationApplicationUpdateRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            """
                    Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of "de Finibus Bonorum et Malorum" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, "Lorem ipsum dolor sit amet..", comes from a line in section 1.10.32.
                    The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested. Sections 1.10.32 and 1.10.33 from "de Finibus Bonorum et Malorum" by Cicero are also reproduced in their exact original form, accompanied by English versions from the 1914 translation by H. Rackham.
                    """,
            " spaceAtTheBeginning",
            "spaceAtTheEnd ",
            " both ",
            "   justAString     "
    })
    void givenValidIdAndUpdateRequest_whenNotesDoesNotValid_thenReturnValidationError(String mockNotes) throws Exception {
        // Given
        String mockId = "dbb3287a-563d-4d85-a978-bcd699294daa";
        EmergencyEvacuationApplicationUpdateRequest mockUpdateRequest = new EmergencyEvacuationApplicationUpdateRequestBuilder()
                .withValidValues()
                .withNotes(mockNotes)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminToken.getAccessToken(), mockUpdateRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .update(Mockito.anyString(), Mockito.any(EmergencyEvacuationApplicationUpdateRequest.class));
    }

    @Test
    void givenValidIdAndUpdateRequest_whenUnauthorizedForUpdating_thenReturnAccessDeniedException() throws Exception {
        // Given
        String mockId = "dbb3287a-563d-4d85-a978-bcd699294daa";
        EmergencyEvacuationApplicationUpdateRequest mockUpdateRequest = new EmergencyEvacuationApplicationUpdateRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-application/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockUserToken.getAccessToken(), mockUpdateRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .update(Mockito.anyString(), Mockito.any(EmergencyEvacuationApplicationUpdateRequest.class));
    }

}
