package org.ays.admin_user.controller;

import org.ays.AbstractRestControllerTest;
import org.ays.admin_user.model.AdminUserRegisterApplication;
import org.ays.admin_user.model.AdminUserRegisterApplicationBuilder;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationCompleteRequest;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationCompleteRequestBuilder;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationCreateRequest;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationCreateRequestBuilder;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationListRequest;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationListRequestBuilder;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationRejectRequest;
import org.ays.admin_user.model.dto.request.AdminUserRegisterApplicationRejectRequestBuilder;
import org.ays.admin_user.model.dto.response.AdminUserRegisterApplicationCreateResponse;
import org.ays.admin_user.model.dto.response.AdminUserRegisterApplicationResponse;
import org.ays.admin_user.model.dto.response.AdminUserRegisterApplicationSummaryResponse;
import org.ays.admin_user.model.dto.response.AdminUserRegisterApplicationsResponse;
import org.ays.admin_user.model.entity.AdminUserRegisterApplicationEntity;
import org.ays.admin_user.model.entity.AdminUserRegisterApplicationEntityBuilder;
import org.ays.admin_user.model.enums.AdminUserRegisterApplicationStatus;
import org.ays.admin_user.model.mapper.AdminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper;
import org.ays.admin_user.model.mapper.AdminUserRegisterApplicationToAdminUserRegisterApplicationCreateResponseMapper;
import org.ays.admin_user.model.mapper.AdminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper;
import org.ays.admin_user.model.mapper.AdminUserRegisterApplicationToAdminUserRegisterApplicationSummaryResponseMapper;
import org.ays.admin_user.model.mapper.AdminUserRegisterApplicationToAdminUserRegisterApplicationsResponseMapper;
import org.ays.admin_user.service.AdminUserRegisterApplicationService;
import org.ays.admin_user.service.AdminUserRegisterService;
import org.ays.common.model.AysPage;
import org.ays.common.model.dto.request.AysPhoneNumberRequest;
import org.ays.common.model.dto.request.AysPhoneNumberRequestBuilder;
import org.ays.common.model.dto.response.AysPageResponse;
import org.ays.common.model.dto.response.AysResponse;
import org.ays.common.model.dto.response.AysResponseBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.common.util.exception.model.AysError;
import org.ays.common.util.exception.model.AysErrorBuilder;
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

class AdminUserRegisterApplicationControllerTest extends AbstractRestControllerTest {

    @MockBean
    private AdminUserRegisterApplicationService adminUserRegisterApplicationService;

    @MockBean
    private AdminUserRegisterService adminUserRegisterService;

    private final AdminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper adminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper = AdminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper.initialize();
    private final AdminUserRegisterApplicationToAdminUserRegisterApplicationsResponseMapper adminUserRegisterApplicationToAdminUserRegisterApplicationsResponseMapper = AdminUserRegisterApplicationToAdminUserRegisterApplicationsResponseMapper.initialize();
    private final AdminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper adminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper = AdminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper.initialize();
    private final AdminUserRegisterApplicationToAdminUserRegisterApplicationSummaryResponseMapper adminUserRegisterApplicationToAdminUserRegisterApplicationSummaryResponseMapper = AdminUserRegisterApplicationToAdminUserRegisterApplicationSummaryResponseMapper.initialize();
    private final AdminUserRegisterApplicationToAdminUserRegisterApplicationCreateResponseMapper adminUserRegisterApplicationToAdminUserRegisterApplicationCreateResponseMapper = AdminUserRegisterApplicationToAdminUserRegisterApplicationCreateResponseMapper.initialize();


    private static final String BASE_PATH = "/api/v1/admin";


    @Test
    void givenValidAdminUserRegisterApplicationListRequest_whenAdminUserRegisterApplicationsFound_thenReturnAysPageResponseOfAdminUserRegisterApplicationsResponse() throws Exception {

        // Given
        AdminUserRegisterApplicationListRequest mockListRequest = new AdminUserRegisterApplicationListRequestBuilder()
                .withValidValues().build();

        // When
        List<AdminUserRegisterApplicationEntity> mockEntities = List.of(new AdminUserRegisterApplicationEntityBuilder().withValidFields().build());
        Page<AdminUserRegisterApplicationEntity> mockPageEntities = new PageImpl<>(mockEntities);
        List<AdminUserRegisterApplication> mockList = adminUserRegisterApplicationEntityToAdminUserRegisterApplicationMapper.map(mockEntities);
        AysPage<AdminUserRegisterApplication> mockAysPage = AysPage
                .of(mockListRequest.getFilter(), mockPageEntities, mockList);

        Mockito.when(adminUserRegisterApplicationService.getRegistrationApplications(Mockito.any(AdminUserRegisterApplicationListRequest.class)))
                .thenReturn(mockAysPage);

        // Then
        String endpoint = BASE_PATH.concat("/registration-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken(), mockListRequest);

        List<AdminUserRegisterApplicationsResponse> mockApplicationsResponse = adminUserRegisterApplicationToAdminUserRegisterApplicationsResponseMapper.map(mockList);
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
        Mockito.verify(adminUserRegisterApplicationService, Mockito.times(1))
                .getRegistrationApplications(Mockito.any(AdminUserRegisterApplicationListRequest.class));
    }

    @Test
    void givenValidAdminUserRegisterApplicationListRequest_whenUserUnauthorizedForListing_thenReturnAccessDeniedException() throws Exception {
        // Given
        AdminUserRegisterApplicationListRequest mockListRequest = new AdminUserRegisterApplicationListRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/registration-applications");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken(), mockListRequest);

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(adminUserRegisterApplicationService, Mockito.never())
                .getRegistrationApplications(Mockito.any(AdminUserRegisterApplicationListRequest.class));
    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenAdminUserRegisterApplicationFound_thenReturnAdminUserRegisterApplicationResponse() throws Exception {

        // Given
        String mockApplicationId = AysRandomUtil.generateUUID();

        // When
        AdminUserRegisterApplication mockRegisterApplication = new AdminUserRegisterApplicationBuilder()
                .withId(mockApplicationId)
                .build();
        Mockito.when(adminUserRegisterApplicationService.getRegistrationApplicationById(mockApplicationId))
                .thenReturn(mockRegisterApplication);

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/").concat(mockApplicationId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockSuperAdminToken.getAccessToken());

        AdminUserRegisterApplicationResponse mockApplicationResponse = adminUserRegisterApplicationToAdminUserRegisterApplicationResponseMapper
                .map(mockRegisterApplication);
        AysResponse<AdminUserRegisterApplicationResponse> mockResponse = AysResponse
                .successOf(mockApplicationResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

        // Verify
        Mockito.verify(adminUserRegisterApplicationService, Mockito.times(1))
                .getRegistrationApplicationById(mockApplicationId);

    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenUnauthorizedForGettingAdminUserRegisterApplicationById_thenReturnAccessDeniedException() throws Exception {

        // Given
        String mockApplicationId = AysRandomUtil.generateUUID();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/".concat(mockApplicationId));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockUserToken.getAccessToken());

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(adminUserRegisterApplicationService, Mockito.never())
                .getRegistrationApplicationById(mockApplicationId);
    }

    @Test
    void givenValidAdminUserRegisterApplicationCreateRequest_whenCreatingAdminUserRegisterApplication_thenReturnAdminUserRegisterApplicationCreateResponse() throws Exception {

        // Given
        AdminUserRegisterApplicationCreateRequest mockRequest = new AdminUserRegisterApplicationCreateRequestBuilder()
                .withValidFields()
                .withInstitutionId(AysValidTestData.Institution.ID)
                .build();

        // When
        Institution mockInstitution = new InstitutionBuilder()
                .withId(mockRequest.getInstitutionId())
                .build();
        AdminUserRegisterApplication mockRegisterApplication = new AdminUserRegisterApplicationBuilder()
                .withValidFields()
                .withInstitution(mockInstitution)
                .withReason(mockRequest.getReason())
                .withStatus(AdminUserRegisterApplicationStatus.WAITING)
                .build();

        Mockito.when(adminUserRegisterApplicationService.createRegistrationApplication(Mockito.any(AdminUserRegisterApplicationCreateRequest.class)))
                .thenReturn(mockRegisterApplication);

        // Then
        String endpoint = BASE_PATH.concat("/registration-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken(), mockRequest);

        AdminUserRegisterApplicationCreateResponse mockApplicationCreateResponse = adminUserRegisterApplicationToAdminUserRegisterApplicationCreateResponseMapper
                .map(mockRegisterApplication);
        AysResponse<AdminUserRegisterApplicationCreateResponse> mockResponse = AysResponse.successOf(mockApplicationCreateResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

        // Verify
        Mockito.verify(adminUserRegisterApplicationService, Mockito.times(1))
                .createRegistrationApplication(Mockito.any(AdminUserRegisterApplicationCreateRequest.class));

    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Invalid reason with special characters: #$%",
            "Too short",
            "                                 a"
    })
    void givenInvalidAdminUserRegisterApplicationCreateRequest_whenCreatingAdminUserRegisterApplication_thenReturnValidationError(String invalidReason) throws Exception {

        // Given
        AdminUserRegisterApplicationCreateRequest createRequest = new AdminUserRegisterApplicationCreateRequestBuilder()
                .withValidFields()
                .withReason(invalidReason)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken(), createRequest);

        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(adminUserRegisterApplicationService, Mockito.never())
                .createRegistrationApplication(Mockito.any(AdminUserRegisterApplicationCreateRequest.class));
    }

    @Test
    void givenValidAdminUserRegisterApplicationCreateRequest_whenUnauthorizedForCreatingAdminUserRegisterApplication_thenReturnAccessDeniedException() throws Exception {

        // Given
        AdminUserRegisterApplicationCreateRequest mockRequest = new AdminUserRegisterApplicationCreateRequestBuilder()
                .withValidFields()
                .withInstitutionId(AysValidTestData.Institution.ID)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken(), mockRequest);

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(adminUserRegisterApplicationService, Mockito.never())
                .createRegistrationApplication(Mockito.any(AdminUserRegisterApplicationCreateRequest.class));
    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenAdminUserApplicationFound_thenReturnAdminUserApplicationSummaryResponse() throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminUserRegisterApplication mockAdminUserRegisterApplication = new AdminUserRegisterApplicationBuilder()
                .withId(mockId)
                .build();

        // When
        Mockito.when(adminUserRegisterApplicationService.getRegistrationApplicationSummaryById(mockId))
                .thenReturn(mockAdminUserRegisterApplication);

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/".concat(mockId).concat("/summary"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint);

        AdminUserRegisterApplicationSummaryResponse mockSummaryResponse = adminUserRegisterApplicationToAdminUserRegisterApplicationSummaryResponseMapper
                .map(mockAdminUserRegisterApplication);
        AysResponse<AdminUserRegisterApplicationSummaryResponse> mockResponse = AysResponse
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
        AdminUserRegisterApplicationCompleteRequest mockRequest = new AdminUserRegisterApplicationCompleteRequestBuilder()
                .withValidFields().build();

        // When
        Mockito.doNothing().when(adminUserRegisterService).completeRegistration(Mockito.anyString(), Mockito.any());

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/").concat(mockId).concat("/complete");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(adminUserRegisterService, Mockito.times(1))
                .completeRegistration(Mockito.anyString(), Mockito.any());
    }

    @Test
    void givenPhoneNumberWithAlphanumericCharacter_whenPhoneNumberIsNotValid_thenReturnValidationError() throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AysPhoneNumberRequest mockPhoneNumber = new AysPhoneNumberRequestBuilder()
                .withCountryCode("ABC")
                .withLineNumber("ABC").build();
        AdminUserRegisterApplicationCompleteRequest mockRequest = new AdminUserRegisterApplicationCompleteRequestBuilder()
                .withValidFields()
                .withPhoneNumber(mockPhoneNumber).build();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/").concat(mockId).concat("/complete");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockRequest);

        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(adminUserRegisterService, Mockito.never())
                .completeRegistration(Mockito.anyString(), Mockito.any());
    }

    @Test
    void givenPhoneNumberWithInvalidLength_whenPhoneNumberIsNotValid_thenReturnValidationError() throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AysPhoneNumberRequest mockPhoneNumber = new AysPhoneNumberRequestBuilder()
                .withCountryCode("456786745645")
                .withLineNumber("6546467456435548676845321346656654").build();
        AdminUserRegisterApplicationCompleteRequest mockRequest = new AdminUserRegisterApplicationCompleteRequestBuilder()
                .withValidFields()
                .withPhoneNumber(mockPhoneNumber).build();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/").concat(mockId).concat("/complete");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockRequest);

        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(adminUserRegisterService, Mockito.never())
                .completeRegistration(Mockito.anyString(), Mockito.any());
    }

    @Test
    void givenPhoneNumberWithInvalidOperator_whenPhoneNumberIsNotValid_thenReturnValidationError() throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        final String invalidOperator = "123";
        AysPhoneNumberRequest mockPhoneNumber = new AysPhoneNumberRequestBuilder()
                .withCountryCode("90")
                .withLineNumber(invalidOperator + "6327218").build();
        AdminUserRegisterApplicationCompleteRequest mockRequest = new AdminUserRegisterApplicationCompleteRequestBuilder()
                .withValidFields()
                .withPhoneNumber(mockPhoneNumber).build();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/").concat(mockId).concat("/complete");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockRequest);

        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(adminUserRegisterService, Mockito.never())
                .completeRegistration(Mockito.anyString(), Mockito.any());
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
        AdminUserRegisterApplicationCompleteRequest mockRequest = new AdminUserRegisterApplicationCompleteRequestBuilder()
                .withValidFields()
                .withFirstName(invalidName)
                .withLastName(invalidName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/").concat(mockId).concat("/complete");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockRequest);

        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(adminUserRegisterService, Mockito.never())
                .completeRegistration(Mockito.anyString(), Mockito.any());
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
        AdminUserRegisterApplicationCompleteRequest mockRequest = new AdminUserRegisterApplicationCompleteRequestBuilder()
                .withValidFields()
                .withEmail(invalidEmail)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/").concat(mockId).concat("/complete");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockRequest);

        AysError mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(adminUserRegisterService, Mockito.never())
                .completeRegistration(Mockito.anyString(), Mockito.any());
    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenApproveAdminUserRegisterApplication_thenReturnNothing() throws Exception {
        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        Mockito.doNothing()
                .when(adminUserRegisterApplicationService).approveRegistrationApplication(mockId);

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/".concat(mockId).concat("/approve"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken());

        AysResponse<Void> mockResponse = AysResponse.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(adminUserRegisterApplicationService, Mockito.times(1))
                .approveRegistrationApplication(mockId);
    }

    @Test
    void givenValidAdminUserRegisterApplicationId_whenUnauthorizedForApprovingAdminUserRegisterApplication_thenReturnAccessDeniedException() throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/".concat(mockId).concat("/approve"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken());

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(adminUserRegisterApplicationService, Mockito.never())
                .approveRegistrationApplication(mockId);
    }

    @Test
    void givenValidAdminUserRegisterApplicationRejectRequest_whenRejectingAdminUserRegisterApplication_thenReturnSuccess() throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminUserRegisterApplicationRejectRequest mockRequest = new AdminUserRegisterApplicationRejectRequestBuilder()
                .withValidFields()
                .build();

        // When
        Mockito.doNothing().when(adminUserRegisterApplicationService).rejectRegistrationApplication(mockId, mockRequest);

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/").concat(mockId).concat("/reject");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken(), mockRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(adminUserRegisterApplicationService, Mockito.times(1))
                .rejectRegistrationApplication(Mockito.eq(mockId), Mockito.any(AdminUserRegisterApplicationRejectRequest.class));

    }

    @Test
    void givenValidAdminUserRegisterApplicationRejectRequest_whenUnauthorizedForRejectingAdminUserRegisterApplication_thenReturnAccessDeniedException() throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AdminUserRegisterApplicationRejectRequest mockRequest = new AdminUserRegisterApplicationRejectRequestBuilder()
                .withValidFields()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/registration-application/").concat(mockId).concat("/reject");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken(), mockRequest);

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(adminUserRegisterApplicationService, Mockito.never())
                .rejectRegistrationApplication(Mockito.eq(mockId), Mockito.any(AdminUserRegisterApplicationRejectRequest.class));
    }

}
