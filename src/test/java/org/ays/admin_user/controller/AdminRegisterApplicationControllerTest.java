package org.ays.admin_user.controller;

import org.ays.AbstractRestControllerTest;
import org.ays.admin_user.model.AdminRegisterApplicationBuilder;
import org.ays.admin_user.model.AdminRegistrationApplication;
import org.ays.admin_user.model.dto.request.AdminRegisterApplicationCompleteRequestBuilder;
import org.ays.admin_user.model.dto.request.AdminRegisterApplicationCreateRequest;
import org.ays.admin_user.model.dto.request.AdminRegisterApplicationCreateRequestBuilder;
import org.ays.admin_user.model.dto.request.AdminRegisterApplicationListRequest;
import org.ays.admin_user.model.dto.request.AdminRegisterApplicationListRequestBuilder;
import org.ays.admin_user.model.dto.request.AdminRegisterApplicationRejectRequest;
import org.ays.admin_user.model.dto.request.AdminRegisterApplicationRejectRequestBuilder;
import org.ays.admin_user.model.dto.request.AdminRegistrationApplicationCompleteRequest;
import org.ays.admin_user.model.dto.response.AdminRegisterApplicationCreateResponse;
import org.ays.admin_user.model.dto.response.AdminRegisterApplicationResponse;
import org.ays.admin_user.model.dto.response.AdminRegisterApplicationSummaryResponse;
import org.ays.admin_user.model.dto.response.AdminUserRegisterApplicationsResponse;
import org.ays.admin_user.model.entity.AdminRegisterApplicationEntity;
import org.ays.admin_user.model.entity.AdminRegisterApplicationEntityBuilder;
import org.ays.admin_user.model.enums.AdminRegisterApplicationStatus;
import org.ays.admin_user.model.mapper.AdminRegisterApplicationEntityToAdminRegisterApplicationMapper;
import org.ays.admin_user.model.mapper.AdminRegisterApplicationToAdminRegisterApplicationCreateResponseMapper;
import org.ays.admin_user.model.mapper.AdminRegisterApplicationToAdminRegisterApplicationResponseMapper;
import org.ays.admin_user.model.mapper.AdminRegisterApplicationToAdminRegisterApplicationSummaryResponseMapper;
import org.ays.admin_user.model.mapper.AdminRegisterApplicationToAdminRegisterApplicationsResponseMapper;
import org.ays.admin_user.service.AdminRegisterApplicationService;
import org.ays.admin_user.service.AdminRegistrationCompleteService;
import org.ays.common.model.AysPage;
import org.ays.common.model.dto.request.AysPhoneNumberRequest;
import org.ays.common.model.dto.request.AysPhoneNumberRequestBuilder;
import org.ays.common.model.dto.response.AysPageResponse;
import org.ays.common.model.dto.response.AysResponse;
import org.ays.common.model.dto.response.AysResponseBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.common.util.exception.model.AysErrorBuilder;
import org.ays.common.util.exception.model.AysErrorResponse;
import org.ays.institution.model.Institution;
import org.ays.institution.model.entity.InstitutionBuilder;
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

class AdminRegisterApplicationControllerTest extends AbstractRestControllerTest {

    @MockBean
    private AdminRegisterApplicationService adminRegisterApplicationService;

    @MockBean
    private AdminRegistrationCompleteService adminRegistrationCompleteService;

    private final AdminRegisterApplicationEntityToAdminRegisterApplicationMapper adminRegisterApplicationEntityToAdminRegisterApplicationMapper = AdminRegisterApplicationEntityToAdminRegisterApplicationMapper.initialize();
    private final AdminRegisterApplicationToAdminRegisterApplicationsResponseMapper adminRegisterApplicationToAdminRegisterApplicationsResponseMapper = AdminRegisterApplicationToAdminRegisterApplicationsResponseMapper.initialize();
    private final AdminRegisterApplicationToAdminRegisterApplicationResponseMapper adminRegisterApplicationToAdminRegisterApplicationResponseMapper = AdminRegisterApplicationToAdminRegisterApplicationResponseMapper.initialize();
    private final AdminRegisterApplicationToAdminRegisterApplicationSummaryResponseMapper adminRegisterApplicationToAdminRegisterApplicationSummaryResponseMapper = AdminRegisterApplicationToAdminRegisterApplicationSummaryResponseMapper.initialize();
    private final AdminRegisterApplicationToAdminRegisterApplicationCreateResponseMapper adminRegisterApplicationToAdminRegisterApplicationCreateResponseMapper = AdminRegisterApplicationToAdminRegisterApplicationCreateResponseMapper.initialize();


    private static final String BASE_PATH = "/api/v1";


    @Test
    void givenValidAdminUserRegisterApplicationListRequest_whenAdminUserRegisterApplicationsFound_thenReturnAysPageResponseOfAdminUserRegisterApplicationsResponse() throws Exception {

        // Given
        AdminRegisterApplicationListRequest mockListRequest = new AdminRegisterApplicationListRequestBuilder()
                .withValidValues().build();

        // When
        List<AdminRegisterApplicationEntity> mockEntities = List.of(new AdminRegisterApplicationEntityBuilder().withValidFields().build());
        Page<AdminRegisterApplicationEntity> mockPageEntities = new PageImpl<>(mockEntities);
        List<AdminRegistrationApplication> mockList = adminRegisterApplicationEntityToAdminRegisterApplicationMapper.map(mockEntities);
        AysPage<AdminRegistrationApplication> mockAysPage = AysPage
                .of(mockListRequest.getFilter(), mockPageEntities, mockList);

        Mockito.when(adminRegisterApplicationService.findAll(Mockito.any(AdminRegisterApplicationListRequest.class)))
                .thenReturn(mockAysPage);

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken(), mockListRequest);

        List<AdminUserRegisterApplicationsResponse> mockApplicationsResponse = adminRegisterApplicationToAdminRegisterApplicationsResponseMapper.map(mockList);
        AysPageResponse<AdminUserRegisterApplicationsResponse> pageOfResponse = AysPageResponse.<AdminUserRegisterApplicationsResponse>builder()
                .of(mockAysPage)
                .content(mockApplicationsResponse)
                .build();
        AysResponse<AysPageResponse<AdminUserRegisterApplicationsResponse>> mockResponse = AysResponse
                .successOf(pageOfResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

        // Verify
        Mockito.verify(adminRegisterApplicationService, Mockito.times(1))
                .findAll(Mockito.any(AdminRegisterApplicationListRequest.class));
    }

    @Test
    void givenValidAdminUserRegisterApplicationListRequest_whenUserUnauthorizedForListing_thenReturnAccessDeniedException() throws Exception {
        // Given
        AdminRegisterApplicationListRequest mockListRequest = new AdminRegisterApplicationListRequestBuilder()
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
        Mockito.verify(adminRegisterApplicationService, Mockito.never())
                .findAll(Mockito.any(AdminRegisterApplicationListRequest.class));
    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenAdminUserRegisterApplicationFound_thenReturnAdminUserRegisterApplicationResponse() throws Exception {

        // Given
        String mockApplicationId = AysRandomUtil.generateUUID();

        // When
        AdminRegistrationApplication mockRegisterApplication = new AdminRegisterApplicationBuilder()
                .withId(mockApplicationId)
                .build();
        Mockito.when(adminRegisterApplicationService.findById(mockApplicationId))
                .thenReturn(mockRegisterApplication);

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(mockApplicationId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockSuperAdminToken.getAccessToken());

        AdminRegisterApplicationResponse mockApplicationResponse = adminRegisterApplicationToAdminRegisterApplicationResponseMapper
                .map(mockRegisterApplication);
        AysResponse<AdminRegisterApplicationResponse> mockResponse = AysResponse
                .successOf(mockApplicationResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

        // Verify
        Mockito.verify(adminRegisterApplicationService, Mockito.times(1))
                .findById(mockApplicationId);

    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenUnauthorizedForGettingAdminUserRegisterApplicationById_thenReturnAccessDeniedException() throws Exception {

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
        Mockito.verify(adminRegisterApplicationService, Mockito.never())
                .findById(mockApplicationId);
    }

    @Test
    void givenValidAdminUserRegisterApplicationCreateRequest_whenCreatingAdminUserRegisterApplication_thenReturnAdminUserRegisterApplicationCreateResponse() throws Exception {

        // Given
        AdminRegisterApplicationCreateRequest mockRequest = new AdminRegisterApplicationCreateRequestBuilder()
                .withValidFields()
                .withInstitutionId(AysValidTestData.Institution.ID)
                .build();

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withId(mockRequest.getInstitutionId())
                .build();
        AdminRegistrationApplication mockRegisterApplication = new AdminRegisterApplicationBuilder()
                .withValidFields()
                .withInstitution(mockInstitution)
                .withReason(mockRequest.getReason())
                .withStatus(AdminRegisterApplicationStatus.WAITING)
                .build();

        Mockito.when(adminRegisterApplicationService.create(Mockito.any(AdminRegisterApplicationCreateRequest.class)))
                .thenReturn(mockRegisterApplication);

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken(), mockRequest);

        AdminRegisterApplicationCreateResponse mockApplicationCreateResponse = adminRegisterApplicationToAdminRegisterApplicationCreateResponseMapper
                .map(mockRegisterApplication);
        AysResponse<AdminRegisterApplicationCreateResponse> mockResponse = AysResponse.successOf(mockApplicationCreateResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

        // Verify
        Mockito.verify(adminRegisterApplicationService, Mockito.times(1))
                .create(Mockito.any(AdminRegisterApplicationCreateRequest.class));

    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Invalid reason with special characters: #$%",
            "Too short",
            "                                      a",
            "151201485621548562154851458614125461254125412"
    })
    void givenInvalidAdminUserRegisterApplicationCreateRequest_whenCreatingAdminUserRegisterApplication_thenReturnValidationError(String invalidReason) throws Exception {

        // Given
        AdminRegisterApplicationCreateRequest createRequest = new AdminRegisterApplicationCreateRequestBuilder()
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
        Mockito.verify(adminRegisterApplicationService, Mockito.never())
                .create(Mockito.any(AdminRegisterApplicationCreateRequest.class));
    }

    @Test
    void givenValidAdminUserRegisterApplicationCreateRequest_whenUnauthorizedForCreatingAdminUserRegisterApplication_thenReturnAccessDeniedException() throws Exception {

        // Given
        AdminRegisterApplicationCreateRequest mockRequest = new AdminRegisterApplicationCreateRequestBuilder()
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
        Mockito.verify(adminRegisterApplicationService, Mockito.never())
                .create(Mockito.any(AdminRegisterApplicationCreateRequest.class));
    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenAdminUserApplicationFound_thenReturnAdminUserApplicationSummaryResponse() throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminRegistrationApplication mockAdminRegistrationApplication = new AdminRegisterApplicationBuilder()
                .withId(mockId)
                .build();

        // When
        Mockito.when(adminRegisterApplicationService.findAllSummaryById(mockId))
                .thenReturn(mockAdminRegistrationApplication);

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/".concat(mockId).concat("/summary"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint);

        AdminRegisterApplicationSummaryResponse mockSummaryResponse = adminRegisterApplicationToAdminRegisterApplicationSummaryResponseMapper
                .map(mockAdminRegistrationApplication);
        AysResponse<AdminRegisterApplicationSummaryResponse> mockResponse = AysResponse
                .successOf(mockSummaryResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());
    }

    @Test
    void givenValidAdminUserRegisterRequest_whenAdminUserRegistered_thenReturnSuccessResponse() throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminRegistrationApplicationCompleteRequest mockRequest = new AdminRegisterApplicationCompleteRequestBuilder()
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
        AdminRegistrationApplicationCompleteRequest mockRequest = new AdminRegisterApplicationCompleteRequestBuilder()
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
        AdminRegistrationApplicationCompleteRequest mockRequest = new AdminRegisterApplicationCompleteRequestBuilder()
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
        AdminRegistrationApplicationCompleteRequest mockRequest = new AdminRegisterApplicationCompleteRequestBuilder()
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
    void givenInvalidAdminUserRegisterApplicationCompleteRequestWithParametrizedInvalidNames_whenNamesAreNotValid_thenReturnValidationError(String invalidName) throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminRegistrationApplicationCompleteRequest mockRequest = new AdminRegisterApplicationCompleteRequestBuilder()
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
    void givenInvalidAdminUserRegisterApplicationCompleteRequestWithParametrizedInvalidEmails_whenEmailsAreNotValid_thenReturnValidationError(String invalidEmail) throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminRegistrationApplicationCompleteRequest mockRequest = new AdminRegisterApplicationCompleteRequestBuilder()
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
    void givenValidAdminUserRegisterApplicationCompleteRequestWithParametrizedValidEmails_whenEmailsAreValid_thenReturnSuccessResponse(String validEmail) throws Exception {
        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminRegistrationApplicationCompleteRequest mockRequest = new AdminRegisterApplicationCompleteRequestBuilder()
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
    void givenValidAdminUserRegisterApplicationId_whenApproveAdminUserRegisterApplication_thenReturnNothing() throws Exception {
        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        Mockito.doNothing()
                .when(adminRegisterApplicationService).approve(mockId);

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/".concat(mockId).concat("/approve"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken());

        AysResponse<Void> mockResponse = AysResponse.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(adminRegisterApplicationService, Mockito.times(1))
                .approve(mockId);
    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenUnauthorizedForApprovingAdminUserRegisterApplication_thenReturnAccessDeniedException() throws Exception {

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
        Mockito.verify(adminRegisterApplicationService, Mockito.never())
                .approve(mockId);
    }

    @Test
    void givenValidAdminUserRegisterApplicationRejectRequest_whenRejectingAdminUserRegisterApplication_thenReturnSuccess() throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminRegisterApplicationRejectRequest mockRequest = new AdminRegisterApplicationRejectRequestBuilder()
                .withValidFields()
                .build();

        // When
        Mockito.doNothing().when(adminRegisterApplicationService).reject(mockId, mockRequest);

        // Then
        String endpoint = BASE_PATH.concat("/admin-registration-application/").concat(mockId).concat("/reject");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken(), mockRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(adminRegisterApplicationService, Mockito.times(1))
                .reject(Mockito.eq(mockId), Mockito.any(AdminRegisterApplicationRejectRequest.class));

    }

    @Test
    void givenValidAdminUserRegisterApplicationRejectRequest_whenUnauthorizedForRejectingAdminUserRegisterApplication_thenReturnAccessDeniedException() throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminRegisterApplicationRejectRequest mockRequest = new AdminRegisterApplicationRejectRequestBuilder()
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
        Mockito.verify(adminRegisterApplicationService, Mockito.never())
                .reject(Mockito.eq(mockId), Mockito.any(AdminRegisterApplicationRejectRequest.class));
    }

}
