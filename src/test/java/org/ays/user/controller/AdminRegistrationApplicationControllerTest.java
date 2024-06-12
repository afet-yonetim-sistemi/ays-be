package org.ays.user.controller;

import org.ays.AbstractRestControllerTest;
import org.ays.common.model.AysPage;
import org.ays.common.model.dto.request.AysPhoneNumberRequestBuilder;
import org.ays.common.model.request.AysPhoneNumberRequest;
import org.ays.common.model.response.AysErrorResponse;
import org.ays.common.model.response.AysPageResponse;
import org.ays.common.model.response.AysResponse;
import org.ays.common.model.response.AysResponseBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.common.util.exception.model.AysErrorBuilder;
import org.ays.institution.model.Institution;
import org.ays.institution.model.entity.InstitutionBuilder;
import org.ays.user.model.AdminRegistrationApplication;
import org.ays.user.model.AdminRegistrationApplicationBuilder;
import org.ays.user.model.dto.request.AdminRegistrationApplicationCompleteRequest;
import org.ays.user.model.dto.request.AdminRegistrationApplicationCompleteRequestBuilder;
import org.ays.user.model.dto.request.AdminRegistrationApplicationCreateRequest;
import org.ays.user.model.dto.request.AdminRegistrationApplicationCreateRequestBuilder;
import org.ays.user.model.dto.request.AdminRegistrationApplicationListRequest;
import org.ays.user.model.dto.request.AdminRegistrationApplicationListRequestBuilder;
import org.ays.user.model.dto.request.AdminRegistrationApplicationRejectRequest;
import org.ays.user.model.dto.request.AdminRegistrationApplicationRejectRequestBuilder;
import org.ays.user.model.dto.response.AdminRegistrationApplicationCreateResponse;
import org.ays.user.model.dto.response.AdminRegistrationApplicationResponse;
import org.ays.user.model.dto.response.AdminRegistrationApplicationSummaryResponse;
import org.ays.user.model.dto.response.AdminRegistrationApplicationsResponse;
import org.ays.user.model.entity.AdminRegistrationApplicationEntity;
import org.ays.user.model.entity.AdminRegistrationApplicationEntityBuilder;
import org.ays.user.model.enums.AdminRegistrationApplicationStatus;
import org.ays.user.model.mapper.AdminRegistrationApplicationEntityToAdminRegistrationApplicationMapper;
import org.ays.user.model.mapper.AdminRegistrationApplicationToAdminRegistrationApplicationCreateResponseMapper;
import org.ays.user.model.mapper.AdminRegistrationApplicationToAdminRegistrationApplicationResponseMapper;
import org.ays.user.model.mapper.AdminRegistrationApplicationToAdminRegistrationApplicationSummaryResponseMapper;
import org.ays.user.model.mapper.AdminRegistrationApplicationToAdminRegistrationApplicationsResponseMapper;
import org.ays.user.service.AdminRegistrationApplicationService;
import org.ays.user.service.AdminRegistrationCompleteService;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.ays.util.AysValidTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

class AdminRegistrationApplicationControllerTest extends AbstractRestControllerTest {

    @MockBean
    private AdminRegistrationApplicationService adminRegistrationApplicationService;

    @MockBean
    private AdminRegistrationCompleteService adminRegistrationCompleteService;

    private final AdminRegistrationApplicationEntityToAdminRegistrationApplicationMapper adminRegistrationApplicationEntityToAdminRegistrationApplicationMapper = AdminRegistrationApplicationEntityToAdminRegistrationApplicationMapper.initialize();
    private final AdminRegistrationApplicationToAdminRegistrationApplicationsResponseMapper adminRegistrationApplicationToAdminRegistrationApplicationsResponseMapper = AdminRegistrationApplicationToAdminRegistrationApplicationsResponseMapper.initialize();
    private final AdminRegistrationApplicationToAdminRegistrationApplicationResponseMapper adminRegistrationApplicationToAdminRegistrationApplicationResponseMapper = AdminRegistrationApplicationToAdminRegistrationApplicationResponseMapper.initialize();
    private final AdminRegistrationApplicationToAdminRegistrationApplicationSummaryResponseMapper adminRegistrationApplicationToAdminRegistrationApplicationSummaryResponseMapper = AdminRegistrationApplicationToAdminRegistrationApplicationSummaryResponseMapper.initialize();
    private final AdminRegistrationApplicationToAdminRegistrationApplicationCreateResponseMapper adminRegistrationApplicationToAdminRegistrationApplicationCreateResponseMapper = AdminRegistrationApplicationToAdminRegistrationApplicationCreateResponseMapper.initialize();


    private static final String BASE_PATH = "/api/v1";


    @Test
    void givenValidAdminRegisterApplicationListRequest_whenAdminRegisterApplicationsFound_thenReturnAysPageResponseOfAdminRegisterApplicationsResponse() throws Exception {

        // Given
        AdminRegistrationApplicationListRequest mockListRequest = new AdminRegistrationApplicationListRequestBuilder()
                .withValidValues().build();

        // When
        List<AdminRegistrationApplicationEntity> mockEntities = List.of(new AdminRegistrationApplicationEntityBuilder().withValidFields().build());
        Page<AdminRegistrationApplicationEntity> mockPageEntities = new PageImpl<>(mockEntities);
        List<AdminRegistrationApplication> mockList = adminRegistrationApplicationEntityToAdminRegistrationApplicationMapper.map(mockEntities);
        AysPage<AdminRegistrationApplication> mockAysPage = AysPage
                .of(mockListRequest.getFilter(), mockPageEntities, mockList);

        Mockito.when(adminRegistrationApplicationService.findAll(Mockito.any(AdminRegistrationApplicationListRequest.class)))
                .thenReturn(mockAysPage);

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminTokenV2.getAccessToken(), mockListRequest);

        List<AdminRegistrationApplicationsResponse> mockApplicationsResponse = adminRegistrationApplicationToAdminRegistrationApplicationsResponseMapper.map(mockList);
        AysPageResponse<AdminRegistrationApplicationsResponse> pageOfResponse = AysPageResponse.<AdminRegistrationApplicationsResponse>builder()
                .of(mockAysPage)
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

        AysErrorResponse mockErrorResponse = AysErrorBuilder.FORBIDDEN;

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
        String mockApplicationId = AysRandomUtil.generateUUID();

        // When
        AdminRegistrationApplication mockRegisterApplication = new AdminRegistrationApplicationBuilder()
                .withId(mockApplicationId)
                .build();
        Mockito.when(adminRegistrationApplicationService.findById(mockApplicationId))
                .thenReturn(mockRegisterApplication);

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(mockApplicationId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockSuperAdminTokenV2.getAccessToken());

        AdminRegistrationApplicationResponse mockApplicationResponse = adminRegistrationApplicationToAdminRegistrationApplicationResponseMapper
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
        String mockApplicationId = AysRandomUtil.generateUUID();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/".concat(mockApplicationId));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockUserToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorBuilder.FORBIDDEN;

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
            " This text has leading and trailing spaces which should be trimmed and accepted. ",
            "ÇalıŞkan ve dÜrüst İnsanlar her zaman başarıyı yakalar.(:;?/)"
    })
    void givenValidAdminRegisterApplicationCreateRequest_whenCreatingAdminRegisterApplication_thenReturnAdminRegisterApplicationCreateResponse(String reason) throws Exception {

        // Given
        AdminRegistrationApplicationCreateRequest mockRequest = new AdminRegistrationApplicationCreateRequestBuilder()
                .withValidFields()
                .withInstitutionId(AysValidTestData.Institution.ID)
                .withReason(reason)
                .build();

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withId(mockRequest.getInstitutionId())
                .build();
        AdminRegistrationApplication mockRegisterApplication = new AdminRegistrationApplicationBuilder()
                .withValidFields()
                .withInstitution(mockInstitution)
                .withReason(mockRequest.getReason())
                .withStatus(AdminRegistrationApplicationStatus.WAITING)
                .build();

        Mockito.when(adminRegistrationApplicationService.create(Mockito.any(AdminRegistrationApplicationCreateRequest.class)))
                .thenReturn(mockRegisterApplication);

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminTokenV2.getAccessToken(), mockRequest);

        AdminRegistrationApplicationCreateResponse mockApplicationCreateResponse = adminRegistrationApplicationToAdminRegistrationApplicationCreateResponseMapper
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
            "151201485621548562154851458614125461254125412",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean vestibulum commodo turpis, sed venenatis sapien suscipit sit amet. Etiam malesuada, ligula in semper varius, nisi mi pulvinar libero, ut commodo dolor orci quis urna. Vivamus ac euismod ex. Proin vel vulputate orci. Ut id nibh finibus, mattis sem id, maximus ante. Proin fringilla ipsum at arcu venenatis, non bibendum justo luctus. Phasellus vestibulum feugiat est sit amet bibendum. Donec nulla leo, ultricies sed pharetra sed, hendrerit vel nunc."
    })
    void givenInvalidAdminRegisterApplicationCreateRequest_whenCreatingAdminRegisterApplication_thenReturnValidationError(String invalidReason) throws Exception {

        // Given
        AdminRegistrationApplicationCreateRequest createRequest = new AdminRegistrationApplicationCreateRequestBuilder()
                .withValidFields()
                .withReason(invalidReason)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken(), createRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

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
                .withValidFields()
                .withInstitutionId(AysValidTestData.Institution.ID)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken(), mockRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.FORBIDDEN;

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
        String mockId = AysRandomUtil.generateUUID();
        AdminRegistrationApplication mockAdminRegistrationApplication = new AdminRegistrationApplicationBuilder()
                .withId(mockId)
                .build();

        // When
        Mockito.when(adminRegistrationApplicationService.findAllSummaryById(mockId))
                .thenReturn(mockAdminRegistrationApplication);

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/".concat(mockId).concat("/summary"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint);

        AdminRegistrationApplicationSummaryResponse mockSummaryResponse = adminRegistrationApplicationToAdminRegistrationApplicationSummaryResponseMapper
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
    void givenValidAdminRegisterRequest_whenAdminRegistered_thenReturnSuccessResponse() throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminRegistrationApplicationCompleteRequest mockRequest = new AdminRegistrationApplicationCompleteRequestBuilder()
                .withValidFields().build();

        // When
        Mockito.doNothing().when(adminRegistrationCompleteService).complete(Mockito.anyString(), Mockito.any());

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(mockId).concat("/complete");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

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
    void givenPhoneNumberWithAlphanumericCharacter_whenPhoneNumberIsNotValid_thenReturnValidationError() throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AysPhoneNumberRequest mockPhoneNumber = new AysPhoneNumberRequestBuilder()
                .withCountryCode("ABC")
                .withLineNumber("ABC").build();
        AdminRegistrationApplicationCompleteRequest mockRequest = new AdminRegistrationApplicationCompleteRequestBuilder()
                .withValidFields()
                .withPhoneNumber(mockPhoneNumber).build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(mockId).concat("/complete");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

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
    void givenPhoneNumberWithInvalidLength_whenPhoneNumberIsNotValid_thenReturnValidationError() throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AysPhoneNumberRequest mockPhoneNumber = new AysPhoneNumberRequestBuilder()
                .withCountryCode("456786745645")
                .withLineNumber("6546467456435548676845321346656654").build();
        AdminRegistrationApplicationCompleteRequest mockRequest = new AdminRegistrationApplicationCompleteRequestBuilder()
                .withValidFields()
                .withPhoneNumber(mockPhoneNumber).build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(mockId).concat("/complete");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

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
    void givenPhoneNumberWithInvalidOperator_whenPhoneNumberIsNotValid_thenReturnValidationError() throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        final String invalidOperator = "123";
        AysPhoneNumberRequest mockPhoneNumber = new AysPhoneNumberRequestBuilder()
                .withCountryCode("90")
                .withLineNumber(invalidOperator + "6327218").build();
        AdminRegistrationApplicationCompleteRequest mockRequest = new AdminRegistrationApplicationCompleteRequestBuilder()
                .withValidFields()
                .withPhoneNumber(mockPhoneNumber).build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(mockId).concat("/complete");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

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
            "John 1234",
            "John *^%$#",
            " John",
            "? John",
            "J"
    })
    void givenInvalidAdminRegisterApplicationCompleteRequestWithParametrizedInvalidNames_whenNamesAreNotValid_thenReturnValidationError(String invalidName) throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminRegistrationApplicationCompleteRequest mockRequest = new AdminRegistrationApplicationCompleteRequestBuilder()
                .withValidFields()
                .withFirstName(invalidName)
                .withLastName(invalidName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(mockId).concat("/complete");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

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
            "abc.def@mail.c",
            "abc.def@mail#archive.com",
            "abc.def@mail",
            "abcdef@mail..com",
            "abc-@mail.com"
    })
    void givenInvalidAdminRegisterApplicationCompleteRequestWithParametrizedInvalidEmails_whenEmailsAreNotValid_thenReturnValidationError(String invalidEmail) throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminRegistrationApplicationCompleteRequest mockRequest = new AdminRegistrationApplicationCompleteRequestBuilder()
                .withValidFields()
                .withEmail(invalidEmail)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(mockId).concat("/complete");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

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
            "abcdef@mail.com",
            "abc+def@archive.com",
            "john.doe123@example.co.uk",
            "admin_123@example.org",
            "admin-test@ays.com",
            "üşengeç-birkız@mail.com"
    })
    void givenValidAdminRegisterApplicationCompleteRequestWithParametrizedValidEmails_whenEmailsAreValid_thenReturnSuccessResponse(String validEmail) throws Exception {
        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminRegistrationApplicationCompleteRequest mockRequest = new AdminRegistrationApplicationCompleteRequestBuilder()
                .withValidFields()
                .withEmail(validEmail)
                .build();

        // When
        Mockito.doNothing().when(adminRegistrationCompleteService).complete(Mockito.anyString(), Mockito.any());

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(mockId).concat("/complete");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

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
    void givenValidAdminRegisterApplicationId_whenApproveAdminRegisterApplication_thenReturnNothing() throws Exception {
        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        Mockito.doNothing()
                .when(adminRegistrationApplicationService).approve(mockId);

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/".concat(mockId).concat("/approve"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminTokenV2.getAccessToken());

        AysResponse<Void> mockResponse = AysResponse.SUCCESS;

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
        String mockId = AysRandomUtil.generateUUID();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/".concat(mockId).concat("/approve"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorBuilder.FORBIDDEN;

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
        String mockId = AysRandomUtil.generateUUID();
        AdminRegistrationApplicationRejectRequest mockRequest = new AdminRegistrationApplicationRejectRequestBuilder()
                .withValidFields()
                .build();

        // When
        Mockito.doNothing().when(adminRegistrationApplicationService).reject(mockId, mockRequest);

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(mockId).concat("/reject");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminTokenV2.getAccessToken(), mockRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

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
        String mockId = AysRandomUtil.generateUUID();
        AdminRegistrationApplicationRejectRequest mockRequest = new AdminRegistrationApplicationRejectRequestBuilder()
                .withValidFields()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(mockId).concat("/reject");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken(), mockRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(adminRegistrationApplicationService, Mockito.never())
                .reject(Mockito.eq(mockId), Mockito.any(AdminRegistrationApplicationRejectRequest.class));
    }

}
