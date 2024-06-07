package org.ays.emergency_application.controller;

import org.ays.AbstractRestControllerTest;
import org.ays.common.model.AysPage;
import org.ays.common.model.dto.request.AysPhoneNumberRequest;
import org.ays.common.model.dto.request.AysPhoneNumberRequestBuilder;
import org.ays.common.model.dto.response.AysPageResponse;
import org.ays.common.model.dto.response.AysResponse;
import org.ays.common.model.dto.response.AysResponseBuilder;
import org.ays.common.util.exception.model.AysErrorBuilder;
import org.ays.common.util.exception.model.AysErrorResponse;
import org.ays.emergency_application.model.EmergencyEvacuationApplication;
import org.ays.emergency_application.model.dto.request.EmergencyEvacuationApplicationListRequest;
import org.ays.emergency_application.model.dto.request.EmergencyEvacuationApplicationRequest;
import org.ays.emergency_application.model.dto.request.EmergencyEvacuationRequestBuilder;
import org.ays.emergency_application.model.dto.response.EmergencyEvacuationApplicationsResponse;
import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationEntity;
import org.ays.emergency_application.model.entity.EmergencyEvacuationApplicationEntityBuilder;
import org.ays.emergency_application.model.mapper.EmergencyEvacuationApplicationEntityToEmergencyEvacuationApplicationMapper;
import org.ays.emergency_application.model.mapper.EmergencyEvacuationApplicationToApplicationsResponseMapper;
import org.ays.emergency_application.service.EmergencyEvacuationApplicationService;
import org.ays.user.model.dto.request.EmergencyEvacuationApplicationListRequestBuilder;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

class EmergencyEvacuationApplicationControllerTest extends AbstractRestControllerTest {

    @MockBean
    private EmergencyEvacuationApplicationService emergencyEvacuationApplicationService;


    private final EmergencyEvacuationApplicationToApplicationsResponseMapper emergencyEvacuationApplicationToApplicationsResponseMapper = EmergencyEvacuationApplicationToApplicationsResponseMapper.initialize();
    private final EmergencyEvacuationApplicationEntityToEmergencyEvacuationApplicationMapper emergencyEvacuationApplicationEntityToEmergencyEvacuationApplicationMapper = EmergencyEvacuationApplicationEntityToEmergencyEvacuationApplicationMapper.initialize();


    private final String BASE_PATH = "/api/v1";

    @Test
    void givenValidAdminRegisterApplicationListRequest_whenAdminRegisterApplicationsFound_thenReturnAysPageResponseOfAdminRegisterApplicationsResponse() throws Exception {

        // Given
        EmergencyEvacuationApplicationListRequest mockListRequest = new EmergencyEvacuationApplicationListRequestBuilder()
                .withValidValues()
                .build();

        // When
        List<EmergencyEvacuationApplicationEntity> mockEntities = List.of(new EmergencyEvacuationApplicationEntityBuilder().build());
        Page<EmergencyEvacuationApplicationEntity> mockPageEntities = new PageImpl<>(mockEntities);
        List<EmergencyEvacuationApplication> mockList = emergencyEvacuationApplicationEntityToEmergencyEvacuationApplicationMapper.map(mockEntities);
        AysPage<EmergencyEvacuationApplication> mockAysPage = AysPage
                .of(mockListRequest.getFilter(), mockPageEntities, mockList);

        Mockito.when(emergencyEvacuationApplicationService.findAll(Mockito.any(EmergencyEvacuationApplicationListRequest.class)))
                .thenReturn(mockAysPage);

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminTokenV2.getAccessToken(), mockListRequest);

        List<EmergencyEvacuationApplicationsResponse> mockApplicationsResponse = emergencyEvacuationApplicationToApplicationsResponseMapper.map(mockList);
        AysPageResponse<EmergencyEvacuationApplicationsResponse> pageOfResponse = AysPageResponse.<EmergencyEvacuationApplicationsResponse>builder()
                .of(mockAysPage)
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
    void givenInvalidAdminRegisterApplicationListRequest_whenReferenceNumberNotValid_thenReturnValidationError(String referenceNumber) throws Exception {

        // Given
        EmergencyEvacuationApplicationListRequest mockListRequest = new EmergencyEvacuationApplicationListRequestBuilder()
                .withValidValues()
                .withReferenceNumber(referenceNumber)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminTokenV2.getAccessToken(), mockListRequest);

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
    void givenInvalidAdminRegisterApplicationListRequest_whenSourceCityNotValid_thenReturnValidationError(String sourceCity) throws Exception {

        // Given
        EmergencyEvacuationApplicationListRequest mockListRequest = new EmergencyEvacuationApplicationListRequestBuilder()
                .withValidValues()
                .withSourceCity(sourceCity)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminTokenV2.getAccessToken(), mockListRequest);

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
    void givenInvalidAdminRegisterApplicationListRequest_whenSourceDistrictNotValid_thenReturnValidationError(String sourceDistrict) throws Exception {

        // Given
        EmergencyEvacuationApplicationListRequest mockListRequest = new EmergencyEvacuationApplicationListRequestBuilder()
                .withValidValues()
                .withSourceDistrict(sourceDistrict)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminTokenV2.getAccessToken(), mockListRequest);

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
    void givenInvalidAdminRegisterApplicationListRequest_whenSeatingCountNotValid_thenReturnValidationError(Integer seatingCount) throws Exception {

        // Given
        EmergencyEvacuationApplicationListRequest mockListRequest = new EmergencyEvacuationApplicationListRequestBuilder()
                .withValidValues()
                .withSeatingCount(seatingCount)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminTokenV2.getAccessToken(), mockListRequest);

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
    void givenInvalidAdminRegisterApplicationListRequest_whenTargetCityNotValid_thenReturnValidationError(String targetCity) throws Exception {

        // Given
        EmergencyEvacuationApplicationListRequest mockListRequest = new EmergencyEvacuationApplicationListRequestBuilder()
                .withValidValues()
                .withTargetCity(targetCity)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminTokenV2.getAccessToken(), mockListRequest);

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
    void givenInvalidAdminRegisterApplicationListRequest_whenTargetDistrictNotValid_thenReturnValidationError(String targetDistrict) throws Exception {

        // Given
        EmergencyEvacuationApplicationListRequest mockListRequest = new EmergencyEvacuationApplicationListRequestBuilder()
                .withValidValues()
                .withTargetDistrict(targetDistrict)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/emergency-evacuation-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminTokenV2.getAccessToken(), mockListRequest);

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
    void givenValidAdminRegisterApplicationListRequest_whenUnauthorizedForListing_thenReturnAccessDeniedException() throws Exception {
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
