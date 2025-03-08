package org.ays.auth.controller;

import org.ays.AysRestControllerTest;
import org.ays.auth.exception.AysAdminRegistrationApplicationNotExistByIdAuthException;
import org.ays.auth.model.AdminRegistrationApplication;
import org.ays.auth.model.AdminRegistrationApplicationBuilder;
import org.ays.auth.model.enums.AdminRegistrationApplicationStatus;
import org.ays.auth.model.mapper.AdminRegistrationApplicationToApplicationResponseMapper;
import org.ays.auth.model.mapper.AdminRegistrationApplicationToApplicationsResponseMapper;
import org.ays.auth.model.mapper.AdminRegistrationApplicationToCreateResponseMapper;
import org.ays.auth.model.mapper.AdminRegistrationApplicationToSummaryResponseMapper;
import org.ays.auth.model.request.AdminRegistrationApplicationCompleteRequest;
import org.ays.auth.model.request.AdminRegistrationApplicationCompleteRequestBuilder;
import org.ays.auth.model.request.AdminRegistrationApplicationCreateRequest;
import org.ays.auth.model.request.AdminRegistrationApplicationCreateRequestBuilder;
import org.ays.auth.model.request.AdminRegistrationApplicationListRequest;
import org.ays.auth.model.request.AdminRegistrationApplicationListRequestBuilder;
import org.ays.auth.model.request.AdminRegistrationApplicationRejectRequest;
import org.ays.auth.model.request.AdminRegistrationApplicationRejectRequestBuilder;
import org.ays.auth.model.response.AdminRegistrationApplicationCreateResponse;
import org.ays.auth.model.response.AdminRegistrationApplicationResponse;
import org.ays.auth.model.response.AdminRegistrationApplicationSummaryResponse;
import org.ays.auth.model.response.AdminRegistrationApplicationsResponse;
import org.ays.auth.service.AdminRegistrationApplicationService;
import org.ays.auth.service.AdminRegistrationCompleteService;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageBuilder;
import org.ays.common.model.request.AysPhoneNumberRequest;
import org.ays.common.model.request.AysPhoneNumberRequestBuilder;
import org.ays.common.model.response.AysErrorResponse;
import org.ays.common.model.response.AysErrorResponseBuilder;
import org.ays.common.model.response.AysPageResponse;
import org.ays.common.model.response.AysResponse;
import org.ays.common.model.response.AysResponseBuilder;
import org.ays.institution.model.Institution;
import org.ays.institution.model.InstitutionBuilder;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.ays.util.AysValidTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

class AdminRegistrationApplicationControllerTest extends AysRestControllerTest {

    @MockBean
    private AdminRegistrationApplicationService adminRegistrationApplicationService;

    @MockBean
    private AdminRegistrationCompleteService adminRegistrationCompleteService;

    private final AdminRegistrationApplicationToApplicationsResponseMapper adminRegistrationApplicationToApplicationsResponseMapper = AdminRegistrationApplicationToApplicationsResponseMapper.initialize();
    private final AdminRegistrationApplicationToApplicationResponseMapper adminRegistrationApplicationToApplicationResponseMapper = AdminRegistrationApplicationToApplicationResponseMapper.initialize();
    private final AdminRegistrationApplicationToSummaryResponseMapper adminRegistrationApplicationToSummaryResponseMapper = AdminRegistrationApplicationToSummaryResponseMapper.initialize();
    private final AdminRegistrationApplicationToCreateResponseMapper adminRegistrationApplicationToCreateResponseMapper = AdminRegistrationApplicationToCreateResponseMapper.initialize();


    private static final String BASE_PATH = "/api/v1";


    @Test
    void givenValidAdminRegisterApplicationListRequest_whenAdminRegisterApplicationsFound_thenReturnAysPageResponseOfAdminRegisterApplicationsResponse() throws Exception {

        // Given
        AdminRegistrationApplicationListRequest mockListRequest = new AdminRegistrationApplicationListRequestBuilder()
                .withValidValues()
                .build();

        // When
        List<AdminRegistrationApplication> mockApplications = List.of(
                new AdminRegistrationApplicationBuilder().withValidValues().build()
        );

        AysPage<AdminRegistrationApplication> mockApplicationPage = AysPageBuilder
                .from(mockApplications, mockListRequest.getPageable());

        Mockito.when(adminRegistrationApplicationService.findAll(Mockito.any(AdminRegistrationApplicationListRequest.class)))
                .thenReturn(mockApplicationPage);

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken(), mockListRequest);

        List<AdminRegistrationApplicationsResponse> mockApplicationsResponse = adminRegistrationApplicationToApplicationsResponseMapper.map(mockApplications);
        AysPageResponse<AdminRegistrationApplicationsResponse> pageOfResponse = AysPageResponse.<AdminRegistrationApplicationsResponse>builder()
                .of(mockApplicationPage)
                .content(mockApplicationsResponse)
                .build();
        AysResponse<AysPageResponse<AdminRegistrationApplicationsResponse>> mockResponse = AysResponse
                .successOf(pageOfResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

        // Verify
        Mockito.verify(adminRegistrationApplicationService, Mockito.times(1))
                .findAll(Mockito.any(AdminRegistrationApplicationListRequest.class));
    }

    @Test
    void givenValidAdminRegisterApplicationListRequest_whenUnauthorizedForListing_thenReturnAccessDeniedException() throws Exception {
        // Given
        AdminRegistrationApplicationListRequest mockListRequest = new AdminRegistrationApplicationListRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken(), mockListRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(adminRegistrationApplicationService, Mockito.never())
                .findAll(Mockito.any(AdminRegistrationApplicationListRequest.class));
    }

    @Test
    void givenValidAdminRegisterApplicationId_whenAdminRegisterApplicationFound_thenReturnAdminRegisterApplicationResponse() throws Exception {

        // Given
        String mockApplicationId = "a0c2351d-54ce-4019-8ffe-a2f8a2700824";

        // When
        AdminRegistrationApplication mockRegisterApplication = new AdminRegistrationApplicationBuilder()
                .withId(mockApplicationId)
                .build();
        Mockito.when(adminRegistrationApplicationService.findById(mockApplicationId))
                .thenReturn(mockRegisterApplication);

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(mockApplicationId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockSuperAdminToken.getAccessToken());

        AdminRegistrationApplicationResponse mockApplicationResponse = adminRegistrationApplicationToApplicationResponseMapper
                .map(mockRegisterApplication);
        AysResponse<AdminRegistrationApplicationResponse> mockResponse = AysResponse
                .successOf(mockApplicationResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

        // Verify
        Mockito.verify(adminRegistrationApplicationService, Mockito.times(1))
                .findById(mockApplicationId);

    }

    @Test
    void givenValidAdminRegisterApplicationId_whenUnauthorizedForGettingAdminRegisterApplicationById_thenReturnAccessDeniedException() throws Exception {

        // Given
        String mockApplicationId = "68c867b4-e84a-405c-b1ab-f8dcaa9c41f2";

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/".concat(mockApplicationId));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockUserToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(adminRegistrationApplicationService, Mockito.never())
                .findById(mockApplicationId);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "This is a valid text with sufficient length and contains alphabetic characters.",
            "This text includes numbers 12345 and still should be accepted because it's within limits.",
            "This text, which includes punctuation marks, should be accepted.",
            "ÇalıŞkan ve dÜrüst İnsanlar her zaman başarıyı yakalar.(:;?/)"
    })
    void givenValidAdminRegisterApplicationCreateRequest_whenCreatingAdminRegisterApplication_thenReturnAdminRegisterApplicationCreateResponse(String reason) throws Exception {

        // Given
        AdminRegistrationApplicationCreateRequest mockRequest = new AdminRegistrationApplicationCreateRequestBuilder()
                .withValidValues()
                .withInstitutionId(AysValidTestData.SuperAdmin.INSTITUTION_ID)
                .withReason(reason)
                .build();

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withId(mockRequest.getInstitutionId())
                .build();
        AdminRegistrationApplication mockRegisterApplication = new AdminRegistrationApplicationBuilder()
                .withValidValues()
                .withInstitution(mockInstitution)
                .withReason(mockRequest.getReason())
                .withStatus(AdminRegistrationApplicationStatus.WAITING)
                .build();

        Mockito.when(adminRegistrationApplicationService.create(Mockito.any(AdminRegistrationApplicationCreateRequest.class)))
                .thenReturn(mockRegisterApplication);

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken(), mockRequest);

        AdminRegistrationApplicationCreateResponse mockApplicationCreateResponse = adminRegistrationApplicationToCreateResponseMapper
                .map(mockRegisterApplication);
        AysResponse<AdminRegistrationApplicationCreateResponse> mockResponse = AysResponse.successOf(mockApplicationCreateResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

        // Verify
        Mockito.verify(adminRegistrationApplicationService, Mockito.times(1))
                .create(Mockito.any(AdminRegistrationApplicationCreateRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Invalid reason with special characters: #$%",
            "#$½#$£#$£#$$#½#£$£#$#£½#$½#$½$£#$#£$$#½#$$½",
            ".,..,.,.,.,.,,.,.,.,.,.,.,.,.,..,.,.,,.,.,.,",
            "Too short",
            "                                      a",
            " spaceAtTheBeginning",
            "spaceAtTheEnd ",
            " both ",
            "   justAString     ",
            "151201485621548562154851458614125461254125412",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean vestibulum commodo turpis, sed venenatis sapien suscipit sit amet. Etiam malesuada, ligula in semper varius, nisi mi pulvinar libero, ut commodo dolor orci quis urna. Vivamus ac euismod ex. Proin vel vulputate orci. Ut id nibh finibus, mattis sem id, maximus ante. Proin fringilla ipsum at arcu venenatis, non bibendum justo luctus. Phasellus vestibulum feugiat est sit amet bibendum. Donec nulla leo, ultricies sed pharetra sed, hendrerit vel nunc."
    })
    void givenInvalidAdminRegisterApplicationCreateRequest_whenCreatingAdminRegisterApplication_thenReturnValidationError(String invalidReason) throws Exception {

        // Given
        AdminRegistrationApplicationCreateRequest createRequest = new AdminRegistrationApplicationCreateRequestBuilder()
                .withValidValues()
                .withReason(invalidReason)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken(), createRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(adminRegistrationApplicationService, Mockito.never())
                .create(Mockito.any(AdminRegistrationApplicationCreateRequest.class));
    }

    @Test
    void givenValidAdminRegisterApplicationCreateRequest_whenUnauthorizedForCreatingAdminRegisterApplication_thenReturnAccessDeniedException() throws Exception {

        // Given
        AdminRegistrationApplicationCreateRequest mockRequest = new AdminRegistrationApplicationCreateRequestBuilder()
                .withValidValues()
                .withInstitutionId(AysValidTestData.SuperAdmin.INSTITUTION_ID)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken(), mockRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(adminRegistrationApplicationService, Mockito.never())
                .create(Mockito.any(AdminRegistrationApplicationCreateRequest.class));
    }


    @Test
    void givenValidAdminRegisterApplicationId_whenAdminApplicationFound_thenReturnAdminApplicationSummaryResponse() throws Exception {

        // Given
        String mockId = "085fbe72-caa7-439d-8db1-166ed005e120";
        AdminRegistrationApplication mockAdminRegistrationApplication = new AdminRegistrationApplicationBuilder()
                .withId(mockId)
                .build();

        // When
        Mockito.when(adminRegistrationApplicationService.findSummaryById(mockId))
                .thenReturn(mockAdminRegistrationApplication);

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/".concat(mockId).concat("/summary"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint);

        AdminRegistrationApplicationSummaryResponse mockSummaryResponse = adminRegistrationApplicationToSummaryResponseMapper
                .map(mockAdminRegistrationApplication);
        AysResponse<AdminRegistrationApplicationSummaryResponse> mockResponse = AysResponse
                .successOf(mockSummaryResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());
    }

    @Test
    void givenIdAndAdminRegisterApplication_whenAdminApplicationNotFound_thenReturnUnauthorizedError() throws Exception {

        // Given
        String mockId = "181e8310-6dfd-444c-aa38-056ce8401345";

        // When
        Mockito.when(adminRegistrationApplicationService.findSummaryById(mockId))
                .thenThrow(new AysAdminRegistrationApplicationNotExistByIdAuthException(mockId));

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/".concat(mockId).concat("/summary"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.UNAUTHORIZED;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isUnauthorized())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotHaveJsonPath());
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "a@b.co",
            "abcdef@mail.com",
            "abc+def@archive.com",
            "john.doe123@example.co.uk",
            "admin_123@example.org",
            "admin-test@ays.com",
            "johndoe@gmail.com",
            "janedoe123@yahoo.com",
            "michael.jordan@nba.com",
            "alice.smith@company.co.uk",
            "info@mywebsite.org",
            "support@helpdesk.net",
            "rajeshmehmetjosephanastasiyahamidjianguonalalitachunoscarmanojfelixmichaelhugoaslambeatrizsergeyemmaricardohenrymunnigaryrobertorosehungabdullahramaisaaclijunxinchonadiaqiangyuliyabrendapauljeanlyubovpablogiuseppexuanchaosimakevinminlongperez@aystest.org"
    })
    void givenValidAdminRegisterRequest_whenAdminRegistered_thenReturnSuccessResponse(String mockEmailAddress) throws Exception {

        // Given
        String mockId = "e8de09dc-a44e-40eb-bcc7-cf0141f8733c";
        AdminRegistrationApplicationCompleteRequest mockRequest = new AdminRegistrationApplicationCompleteRequestBuilder()
                .withValidValues()
                .withEmailAddress(mockEmailAddress)
                .build();

        // When
        Mockito.doNothing().when(adminRegistrationCompleteService).complete(Mockito.anyString(), Mockito.any());

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(mockId).concat("/complete");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(adminRegistrationCompleteService, Mockito.times(1))
                .complete(Mockito.anyString(), Mockito.any());
    }


    @Test
    void givenIdAndAdminRegisterRequest_whenAdminApplicationNotFound_thenReturnUnauthorizedError() throws Exception {

        // Given
        String mockId = "181e8310-6dfd-444c-aa38-056ce8401345";
        AdminRegistrationApplicationCompleteRequest mockRequest = new AdminRegistrationApplicationCompleteRequestBuilder()
                .withValidValues().build();

        // When
        Mockito.doThrow(new AysAdminRegistrationApplicationNotExistByIdAuthException(mockId))
                .when(adminRegistrationCompleteService)
                .complete(Mockito.anyString(), Mockito.any());

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(mockId).concat("/complete");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.UNAUTHORIZED;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isUnauthorized())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotHaveJsonPath());

        // Verify
        Mockito.verify(adminRegistrationCompleteService, Mockito.times(1))
                .complete(Mockito.anyString(), Mockito.any());
    }

    @Test
    void givenPhoneNumberWithAlphanumericCharacter_whenPhoneNumberIsNotValid_thenReturnValidationError() throws Exception {

        // Given
        String mockId = "f1b789d0-6095-4860-85bb-e1a0b20f1d13";
        AysPhoneNumberRequest mockPhoneNumberRequest = new AysPhoneNumberRequestBuilder()
                .withCountryCode("ABC")
                .withLineNumber("ABC").build();
        AdminRegistrationApplicationCompleteRequest mockRequest = new AdminRegistrationApplicationCompleteRequestBuilder()
                .withValidValues()
                .withPhoneNumber(mockPhoneNumberRequest).build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(mockId).concat("/complete");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockRequest);

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
        Mockito.verify(adminRegistrationCompleteService, Mockito.never())
                .complete(Mockito.anyString(), Mockito.any());
    }

    @Test
    void givenPhoneNumberWithInvalidLength_whenPhoneNumberIsNotValid_thenReturnValidationError() throws Exception {

        // Given
        String mockId = "25930d3f-4cea-4147-a21a-0f22c9bf72de";
        AysPhoneNumberRequest mockPhoneNumberRequest = new AysPhoneNumberRequestBuilder()
                .withCountryCode("456786745645")
                .withLineNumber("6546467456435548676845321346656654").build();
        AdminRegistrationApplicationCompleteRequest mockRequest = new AdminRegistrationApplicationCompleteRequestBuilder()
                .withValidValues()
                .withPhoneNumber(mockPhoneNumberRequest).build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(mockId).concat("/complete");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockRequest);

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
        Mockito.verify(adminRegistrationCompleteService, Mockito.never())
                .complete(Mockito.anyString(), Mockito.any());
    }

    @Test
    void givenPhoneNumberWithInvalidOperator_whenPhoneNumberIsNotValid_thenReturnValidationError() throws Exception {

        // Given
        String mockId = "2028b456-e06c-4ea1-9017-b45523529576";
        final String invalidOperator = "123";
        AysPhoneNumberRequest mockPhoneNumberRequest = new AysPhoneNumberRequestBuilder()
                .withCountryCode("90")
                .withLineNumber(invalidOperator + "6327218").build();
        AdminRegistrationApplicationCompleteRequest mockRequest = new AdminRegistrationApplicationCompleteRequestBuilder()
                .withValidValues()
                .withPhoneNumber(mockPhoneNumberRequest).build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(mockId).concat("/complete");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockRequest);

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
        Mockito.verify(adminRegistrationCompleteService, Mockito.never())
                .complete(Mockito.anyString(), Mockito.any());
    }

    @Test
    void givenPhoneNumberWithInvalidCountryCode_whenPhoneNumberIsNotValid_thenReturnValidationError() throws Exception {

        // Given
        String mockId = "25930d3f-4cea-4147-a21a-0f22c9bf72de";
        AysPhoneNumberRequest mockPhoneNumberRequest = new AysPhoneNumberRequestBuilder()
                .withCountryCode("80")
                .withLineNumber("5468524152").build();
        AdminRegistrationApplicationCompleteRequest mockRequest = new AdminRegistrationApplicationCompleteRequestBuilder()
                .withValidValues()
                .withPhoneNumber(mockPhoneNumberRequest).build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(mockId).concat("/complete");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockRequest);

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
        Mockito.verify(adminRegistrationCompleteService, Mockito.never())
                .complete(Mockito.anyString(), Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "John 1234",
            "John *^%$#",
            " John",
            "? John",
            "J",
            "J----",
            "City--King",
            "John  Doe"
    })
    void givenInvalidAdminRegisterApplicationCompleteRequestWithParametrizedInvalidNames_whenNamesAreNotValid_thenReturnValidationError(String invalidName) throws Exception {

        // Given
        String mockId = "f423facc-36fe-4615-a68d-f7f1fe5cd860";
        AdminRegistrationApplicationCompleteRequest mockRequest = new AdminRegistrationApplicationCompleteRequestBuilder()
                .withValidValues()
                .withFirstName(invalidName)
                .withLastName(invalidName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(mockId).concat("/complete");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(adminRegistrationCompleteService, Mockito.never())
                .complete(Mockito.anyString(), Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "a@b.c",
            "plainaddress",
            "@missingusername.com",
            "username@.com",
            "username@gmail",
            "username@gmail..com",
            "username@gmail.c",
            "username@-gmail.com",
            "username@gmail-.com",
            "username@gmail.com.",
            "username@.gmail.com",
            "username@gmail@gmail.com",
            "username(john.doe)@gmail.com",
            "user@domain(comment).com",
            "usernamegmail.com",
            "username@gmail,com",
            "username@gmail space.co",
            "username@gmail..co.uk",
            "user#gmail.com",
            "bekeleandreaevelynirenealexandrascottmirasoniamustafahuivladimirmarcoyolandaraymondakhtermichaeldennistatianayuliyagangmargaretthomassumanjeanamymostafasaidrubenchenedithjumasitimeilucasgaryghulamminaxiaohongmarcosrafaelamyantoniamohamadfatmaahmed@aystest.org"
    })
    void givenInvalidAdminRegisterApplicationCompleteRequestWithParametrizedInvalidEmails_whenEmailsAreNotValid_thenReturnValidationError(String mockEmailAddress) throws Exception {

        // Given
        String mockId = "53617d24-e32c-4249-b9e6-b10e63a439bd";
        AdminRegistrationApplicationCompleteRequest mockRequest = new AdminRegistrationApplicationCompleteRequestBuilder()
                .withValidValues()
                .withEmailAddress(mockEmailAddress)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(mockId).concat("/complete");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(adminRegistrationCompleteService, Mockito.never())
                .complete(Mockito.anyString(), Mockito.any());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "g",
            "g.h2j5L",
            "uqTY6zlNlPgQZtyaRIVNPEAaLAPdOxEhmLWcoCcR4TbkLWedAcaHa96ZYXdrvor7qhN8B5ccms06NfbzVG2gzq0DWkHLxQupzmmbH3W9UhrnLS0LjuBVgOjQEoyaXesrJ"
    })
    void givenInvalidAdminRegisterApplicationCompleteRequestWithInvalidPassword_whenPasswordDoesNotValid_thenReturnValidationError(String mockPassword) throws Exception {

        // Given
        String mockId = "de2b9621-0bf6-45df-a173-4697797446b7";
        AdminRegistrationApplicationCompleteRequest mockRequest = new AdminRegistrationApplicationCompleteRequestBuilder()
                .withValidValues()
                .withPassword(mockPassword)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(mockId).concat("/complete");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(adminRegistrationCompleteService, Mockito.never())
                .complete(Mockito.anyString(), Mockito.any());
    }

    @Test
    void givenValidAdminRegisterApplicationId_whenApproveAdminRegisterApplication_thenReturnNothing() throws Exception {
        // Given
        String mockId = "47d91587-612f-462b-975f-ed227e2ee3a7";

        // When
        Mockito.doNothing()
                .when(adminRegistrationApplicationService).approve(mockId);

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/".concat(mockId).concat("/approve"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken());

        AysResponse<Void> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(adminRegistrationApplicationService, Mockito.times(1))
                .approve(mockId);
    }

    @Test
    void givenValidAdminRegisterApplicationId_whenUnauthorizedForApprovingAdminRegisterApplication_thenReturnAccessDeniedException() throws Exception {

        // Given
        String mockId = "5cbc8774-e2c3-414b-a1d8-26abcf3c9d17";

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/".concat(mockId).concat("/approve"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(adminRegistrationApplicationService, Mockito.never())
                .approve(mockId);
    }

    @Test
    void givenValidAdminRegisterApplicationRejectRequest_whenRejectingAdminRegisterApplication_thenReturnSuccess() throws Exception {

        // Given
        String mockId = "c5c504a6-1223-483b-a8ba-6fb4ea309e00";
        AdminRegistrationApplicationRejectRequest mockRequest = new AdminRegistrationApplicationRejectRequestBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.doNothing().when(adminRegistrationApplicationService).reject(mockId, mockRequest);

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(mockId).concat("/reject");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken(), mockRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(adminRegistrationApplicationService, Mockito.times(1))
                .reject(Mockito.eq(mockId), Mockito.any(AdminRegistrationApplicationRejectRequest.class));

    }

    @Test
    void givenValidAdminRegisterApplicationRejectRequest_whenUnauthorizedForRejectingAdminRegisterApplication_thenReturnAccessDeniedException() throws Exception {

        // Given
        String mockId = "c27aa25e-7e2f-4bc1-8056-eb9c2948f507";
        AdminRegistrationApplicationRejectRequest mockRequest = new AdminRegistrationApplicationRejectRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(mockId).concat("/reject");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken(), mockRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(adminRegistrationApplicationService, Mockito.never())
                .reject(Mockito.eq(mockId), Mockito.any(AdminRegistrationApplicationRejectRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "   ",
            "less than 40",
            """
                    Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor.
                    Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.
                    Donec qudam felis, ultricies nec, pellentesque eu, pretscsxwium quis, sem. Nulla consequat massa quis
                    enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut,
                    imperdiet a, venenatdskjvndshjcndsis vitae, justo. Nullam dictum felis eu pedde mollis pretium. Integer tincidunt.
                    Cras dapibus. Vivdamus ewl
                    """,
            " spaceAtTheBeginning",
            "spaceAtTheEnd ",
            " both ",
            "   justAString     "
    })
    void givenInvalidAdminRegisterApplicationRejectRequest_whenRejectingAdminRegisterApplication_thenReturnValidationError(String rejectReason) throws Exception {

        // Given
        String mockId = "4d04bd1e-6318-43ba-ab40-57efb8afc918";
        AdminRegistrationApplicationRejectRequest mockRequest = new AdminRegistrationApplicationRejectRequestBuilder()
                .withValidValues()
                .withRejectReason(rejectReason)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(mockId).concat("/reject");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken(), mockRequest);

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(adminRegistrationApplicationService, Mockito.never())
                .reject(Mockito.anyString(),
                        Mockito.any(AdminRegistrationApplicationRejectRequest.class));
    }

}
