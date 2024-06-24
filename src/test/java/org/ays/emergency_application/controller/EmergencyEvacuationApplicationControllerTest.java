package org.ays.emergency_application.controller;

import org.ays.AysRestControllerTest;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageBuilder;
import org.ays.common.model.request.AysPhoneNumberRequest;
import org.ays.common.model.request.AysPhoneNumberRequestBuilder;
import org.ays.common.model.response.AysErrorResponse;
import org.ays.common.model.response.AysPageResponse;
import org.ays.common.model.response.AysResponse;
import org.ays.common.model.response.AysResponseBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.common.util.exception.model.AysErrorBuilder;
import org.ays.emergency_application.model.EmergencyEvacuationApplication;
import org.ays.emergency_application.model.EmergencyEvacuationApplicationBuilder;
import org.ays.emergency_application.model.mapper.EmergencyEvacuationApplicationToApplicationResponseMapper;
import org.ays.emergency_application.model.mapper.EmergencyEvacuationApplicationToApplicationsResponseMapper;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationListRequest;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationListRequestBuilder;
import org.ays.emergency_application.model.request.EmergencyEvacuationApplicationRequest;
import org.ays.emergency_application.model.request.EmergencyEvacuationRequestBuilder;
import org.ays.emergency_application.model.response.EmergencyEvacuationApplicationResponse;
import org.ays.emergency_application.model.response.EmergencyEvacuationApplicationsResponse;
import org.ays.emergency_application.service.EmergencyEvacuationApplicationService;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

class EmergencyEvacuationApplicationControllerTest extends AysRestControllerTest {

    @MockBean
    private EmergencyEvacuationApplicationService emergencyEvacuationApplicationService;


    private final EmergencyEvacuationApplicationToApplicationsResponseMapper emergencyEvacuationApplicationToApplicationsResponseMapper = EmergencyEvacuationApplicationToApplicationsResponseMapper.initialize();
    private final EmergencyEvacuationApplicationToApplicationResponseMapper emergencyEvacuationApplicationToApplicationResponseMapper = EmergencyEvacuationApplicationToApplicationResponseMapper.initialize();

    private final String BASE_PATH = "/api/v1";

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

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

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
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices.",
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

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

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
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices.",
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

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

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

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

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
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices.",
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

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

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
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam In hac habitasse platea dictumst. Nullam in turpis at nunc ultrices.",
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

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

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

        AysErrorResponse mockErrorResponse = AysErrorBuilder.FORBIDDEN;

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

        AysErrorResponse mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(emergencyEvacuationApplicationService, Mockito.never())
                .findById(mockApplicationId);
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
                .withValidValues()
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
                .withValidValues()
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
                .withValidValues()
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
                .withValidValues()
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
                .withValidValues()
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
                .withValidValues()
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
                .withValidValues()
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
                .withValidValues()
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
                .withValidValues()
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
                .withValidValues()
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
                .withValidValues()
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
                .withValidValues()
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
