package org.ays.auth.controller;

import org.ays.AysRestControllerTest;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.AysRoleBuilder;
import org.ays.auth.model.enums.AysRoleStatus;
import org.ays.auth.model.mapper.AysRoleToResponseMapper;
import org.ays.auth.model.mapper.AysRoleToRolesResponseMapper;
import org.ays.auth.model.mapper.AysRoleToRolesSummaryResponseMapper;
import org.ays.auth.model.request.AysRoleCreateRequest;
import org.ays.auth.model.request.AysRoleCreateRequestBuilder;
import org.ays.auth.model.request.AysRoleListRequest;
import org.ays.auth.model.request.AysRoleListRequestBuilder;
import org.ays.auth.model.request.AysRoleUpdateRequest;
import org.ays.auth.model.request.AysRoleUpdateRequestBuilder;
import org.ays.auth.model.response.AysRoleResponse;
import org.ays.auth.model.response.AysRolesResponse;
import org.ays.auth.model.response.AysRolesSummaryResponse;
import org.ays.auth.service.AysRoleCreateService;
import org.ays.auth.service.AysRoleReadService;
import org.ays.auth.service.AysRoleUpdateService;
import org.ays.auth.util.exception.AysRoleAlreadyDeletedException;
import org.ays.auth.util.exception.AysRoleAssignedToUserException;
import org.ays.common.model.AysPage;
import org.ays.common.model.AysPageBuilder;
import org.ays.common.model.response.AysErrorResponse;
import org.ays.common.model.response.AysPageResponse;
import org.ays.common.model.response.AysResponse;
import org.ays.common.model.response.AysResponseBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.common.util.exception.model.AysErrorBuilder;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

class AysRoleControllerTest extends AysRestControllerTest {

    @MockBean
    private AysRoleReadService roleReadService;

    @MockBean
    private AysRoleCreateService roleCreateService;

    @MockBean
    private AysRoleUpdateService roleUpdateService;


    private final AysRoleToRolesResponseMapper roleToRolesResponseMapper = AysRoleToRolesResponseMapper.initialize();
    private final AysRoleToRolesSummaryResponseMapper roleToRolesSummaryResponseMapper = AysRoleToRolesSummaryResponseMapper.initialize();
    private final AysRoleToResponseMapper roleToRoleResponseMapper = AysRoleToResponseMapper.initialize();


    private static final String BASE_PATH = "/api/v1";


    @Test
    void whenRolesFound_thenReturnSummaryOfRoles() throws Exception {

        // When
        List<AysRole> mockRoles = List.of(
                new AysRoleBuilder().withValidValues().build(),
                new AysRoleBuilder().withValidValues().build()
        );

        Mockito.when(roleReadService.findAll())
                .thenReturn(mockRoles);

        // Then
        String endpoint = BASE_PATH.concat("/roles/summary");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockAdminToken.getAccessToken());

        List<AysRolesSummaryResponse> mockRolesSummaryResponses = roleToRolesSummaryResponseMapper.map(mockRoles);
        AysResponse<List<AysRolesSummaryResponse>> mockResponse = AysResponse
                .successOf(mockRolesSummaryResponses);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

        // Verify
        Mockito.verify(roleReadService, Mockito.times(1))
                .findAll();
    }


    @Test
    void givenValidRoleListRequest_whenRolesFound_thenReturnAysPageResponseOfRolesResponse() throws Exception {

        // Given
        AysRoleListRequest mockListRequest = new AysRoleListRequestBuilder()
                .withValidValues()
                .build();

        // When
        List<AysRole> mockRoles = List.of(
                new AysRoleBuilder().withValidValues().build()
        );

        AysPage<AysRole> mockRolePage = AysPageBuilder
                .from(mockRoles, mockListRequest.getPageable());

        Mockito.when(roleReadService.findAll(Mockito.any(AysRoleListRequest.class)))
                .thenReturn(mockRolePage);

        // Then
        String endpoint = BASE_PATH.concat("/roles");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken(), mockListRequest);

        List<AysRolesResponse> mockRolesResponse = roleToRolesResponseMapper.map(mockRoles);
        AysPageResponse<AysRolesResponse> pageOfResponse = AysPageResponse.<AysRolesResponse>builder()
                .of(mockRolePage)
                .content(mockRolesResponse)
                .build();
        AysResponse<AysPageResponse<AysRolesResponse>> mockResponse = AysResponse
                .successOf(pageOfResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

        // Verify
        Mockito.verify(roleReadService, Mockito.times(1))
                .findAll(Mockito.any(AysRoleListRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Test Role 1234",
            "Role *^%$#",
            " Test",
            "? Role",
            "J"
    })
    void givenRoleListRequest_whenNameDoesNotValid_thenReturnValidationError(String invalidName) throws Exception {

        // Given
        AysRoleListRequest mockListRequest = new AysRoleListRequestBuilder()
                .withValidValues()
                .withName(invalidName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/roles");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockSuperAdminToken.getAccessToken(), mockListRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(roleReadService, Mockito.never())
                .findAll(Mockito.any(AysRoleListRequest.class));
    }

    @Test
    void givenValidRoleListRequest_whenUserUnauthorized_thenReturnAccessDeniedException() throws Exception {
        // Given
        AysRoleListRequest mockListRequest = new AysRoleListRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/roles");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken(), mockListRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(roleReadService, Mockito.never())
                .findAll(Mockito.any(AysRoleListRequest.class));
    }


    @Test
    void givenValidRoleId_whenRoleFound_thenReturnAysRoleResponse() throws Exception {

        // Given
        String mockRoleId = AysRandomUtil.generateUUID();

        // When
        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .withId(mockRoleId)
                .build();

        Mockito.when(roleReadService.findById(mockRoleId))
                .thenReturn(mockRole);

        // Then
        String endpoint = BASE_PATH.concat("/role/").concat(mockRoleId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockAdminToken.getAccessToken());

        AysRoleResponse mockRoleResponse = roleToRoleResponseMapper
                .map(mockRole);
        AysResponse<AysRoleResponse> mockResponse = AysResponse
                .successOf(mockRoleResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

        // Verify
        Mockito.verify(roleReadService, Mockito.times(1))
                .findById(mockRoleId);
    }

    @Test
    void givenRoleId_whenUnauthorizedForGettingRoleById_thenReturnAccessDeniedException() throws Exception {

        // Given
        String mockRoleId = AysRandomUtil.generateUUID();

        // Then
        String endpoint = BASE_PATH.concat("/role/".concat(mockRoleId));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockUserToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(roleReadService, Mockito.never())
                .findById(mockRoleId);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "A",
            "493268349068342"
    })
    void givenInvalidId_whenIdNotValid_thenReturnValidationError(String invalidId) throws Exception {

        // Then
        String endpoint = BASE_PATH.concat("/role/").concat(invalidId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, mockAdminToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(roleUpdateService, Mockito.never())
                .update(Mockito.anyString(), Mockito.any(AysRoleUpdateRequest.class));
    }


    @Test
    void givenValidRoleCreateRequest_whenRoleCreated_thenReturnSuccess() throws Exception {
        // Given
        AysRoleCreateRequest mockCreateRequest = new AysRoleCreateRequestBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.doNothing()
                .when(roleCreateService)
                .create(Mockito.any(AysRoleCreateRequest.class));

        // Then
        String endpoint = BASE_PATH.concat("/role");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminToken.getAccessToken(), mockCreateRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(roleCreateService, Mockito.times(1))
                .create(Mockito.any(AysRoleCreateRequest.class));
    }

    @Test
    void givenValidRoleCreateRequest_whenUserUnauthorized_thenReturnAccessDeniedException() throws Exception {
        // Given
        AysRoleCreateRequest mockCreateRequest = new AysRoleCreateRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/role");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken(), mockCreateRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.FORBIDDEN;
        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(roleCreateService, Mockito.never())
                .create(Mockito.any(AysRoleCreateRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "A",
            "% fsdh     ",
            "493268349068342",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla nec odio nec urna tincidunt fermentum. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla nec odio nec urna tincidunt fermentum. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla nec odio nec urna tincidunt fermentum."
    })
    void givenInvalidRoleCreateRequest_whenNameIsNotValid_thenReturnValidationError(String invalidName) throws Exception {
        // Given
        AysRoleCreateRequest mockCreateRequest = new AysRoleCreateRequestBuilder()
                .withValidValues()
                .withName(invalidName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/role");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminToken.getAccessToken(), mockCreateRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(roleCreateService, Mockito.never())
                .create(Mockito.any(AysRoleCreateRequest.class));
    }

    @Test
    void givenInvalidRoleCreateRequest_whenPermissionIdsAreNull_thenReturnValidationError() throws Exception {
        // Given
        AysRoleCreateRequest mockCreateRequest = new AysRoleCreateRequestBuilder()
                .withValidValues()
                .withPermissionIds(null)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/role");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminToken.getAccessToken(), mockCreateRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(roleCreateService, Mockito.never())
                .create(Mockito.any(AysRoleCreateRequest.class));
    }

    @Test
    void givenInvalidRoleCreateRequest_whenPermissionIdsAreEmpty_thenReturnValidationError() throws Exception {
        // Given
        AysRoleCreateRequest mockCreateRequest = new AysRoleCreateRequestBuilder()
                .withValidValues()
                .withPermissionIds(Set.of())
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/role");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminToken.getAccessToken(), mockCreateRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(roleCreateService, Mockito.never())
                .create(Mockito.any(AysRoleCreateRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "55aed4c4facb4b66bdb5-309eaaef4453"
    })
    void givenInvalidRoleCreateRequest_whenPermissionIdIsNotValid_thenReturnValidationError(String invalidPermissionId) throws Exception {
        // Given
        AysRoleCreateRequest mockCreateRequest = new AysRoleCreateRequestBuilder()
                .withValidValues()
                .withPermissionIds(Set.of(invalidPermissionId))
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/role");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminToken.getAccessToken(), mockCreateRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(roleCreateService, Mockito.never())
                .create(Mockito.any(AysRoleCreateRequest.class));
    }


    @Test
    void givenValidIdAndRoleUpdateRequest_whenRoleUpdated_thenReturnSuccess() throws Exception {
        // Given
        String mockId = AysRandomUtil.generateUUID();
        AysRoleUpdateRequest mockUpdateRequest = new AysRoleUpdateRequestBuilder()
                .withValidValues()
                .build();

        // When
        Mockito.doNothing()
                .when(roleUpdateService)
                .update(Mockito.any(), Mockito.any(AysRoleUpdateRequest.class));

        // Then
        String endpoint = BASE_PATH.concat("/role/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminToken.getAccessToken(), mockUpdateRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(roleUpdateService, Mockito.times(1))
                .update(Mockito.anyString(), Mockito.any(AysRoleUpdateRequest.class));
    }

    @Test
    void givenValidIdAndRoleUpdateRequest_whenUserUnauthorized_thenReturnAccessDeniedException() throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AysRoleUpdateRequest mockUpdateRequest = new AysRoleUpdateRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/role/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockUserToken.getAccessToken(), mockUpdateRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.FORBIDDEN;
        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(roleUpdateService, Mockito.never())
                .update(Mockito.anyString(), Mockito.any(AysRoleUpdateRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "A",
            "493268349068342"
    })
    void givenInvalidIdAndValidRoleUpdateRequest_whenIdNotValid_thenReturnValidationError(String invalidId) throws Exception {

        // Given
        AysRoleUpdateRequest mockUpdateRequest = new AysRoleUpdateRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/role/").concat(invalidId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminToken.getAccessToken(), mockUpdateRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(roleUpdateService, Mockito.never())
                .update(Mockito.anyString(), Mockito.any(AysRoleUpdateRequest.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "A",
            " Role",
            "Role ",
            "123Role",
            ".Role",
            "% fsdh     ",
            "493268349068342",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla nec odio nec urna tincidunt fermentum. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla nec odio nec urna tincidunt fermentum. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla nec odio nec urna tincidunt fermentum."
    })
    void givenValidIdAndInvalidRoleUpdateRequest_whenNameIsNotValid_thenReturnValidationError(String invalidName) throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AysRoleUpdateRequest mockUpdateRequest = new AysRoleUpdateRequestBuilder()
                .withValidValues()
                .withName(invalidName)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/role/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminToken.getAccessToken(), mockUpdateRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(roleUpdateService, Mockito.never())
                .update(Mockito.anyString(), Mockito.any(AysRoleUpdateRequest.class));
    }

    @Test
    void givenValidIdAndInvalidRoleUpdateRequest_whenPermissionIdsAreNull_thenReturnValidationError() throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AysRoleUpdateRequest mockUpdateRequest = new AysRoleUpdateRequestBuilder()
                .withValidValues()
                .withPermissionIds(null)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/role/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminToken.getAccessToken(), mockUpdateRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(roleUpdateService, Mockito.never())
                .update(Mockito.anyString(), Mockito.any(AysRoleUpdateRequest.class));
    }

    @Test
    void givenValidIdAndInvalidRoleUpdateRequest_whenPermissionIdsAreEmpty_thenReturnValidationError() throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();
        AysRoleUpdateRequest mockUpdateRequest = new AysRoleUpdateRequestBuilder()
                .withValidValues()
                .withPermissionIds(Set.of())
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/role/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminToken.getAccessToken(), mockUpdateRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(roleUpdateService, Mockito.never())
                .update(Mockito.anyString(), Mockito.any(AysRoleUpdateRequest.class));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {
            "",
            "55aed4c4facb4b66bdb5-309eaaef4453"
    })
    void givenValidIdAndInvalidRoleUpdateRequest_whenPermissionIdIsNotValid_thenReturnValidationError(String invalidPermissionId) throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();

        Set<String> mockPermissionIds = new HashSet<>();
        mockPermissionIds.add(invalidPermissionId);
        AysRoleUpdateRequest mockUpdateRequest = new AysRoleUpdateRequestBuilder()
                .withValidValues()
                .withPermissionIds(mockPermissionIds)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/role/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminToken.getAccessToken(), mockUpdateRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(roleUpdateService, Mockito.never())
                .update(Mockito.anyString(), Mockito.any(AysRoleUpdateRequest.class));
    }


    @Test
    void givenValidId_whenRoleActivated_thenReturnSuccess() throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        Mockito.doNothing()
                .when(roleUpdateService)
                .activate(Mockito.any());

        // Then
        String endpoint = BASE_PATH.concat("/role/".concat(mockId).concat("/activate"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .patch(endpoint, mockSuperAdminToken.getAccessToken());

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(roleUpdateService, Mockito.times(1))
                .activate(Mockito.anyString());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "A",
            "493268349068342"
    })
    void givenId_whenIdIsNotValid_thenReturnValidationError(String invalidId) throws Exception {

        // Then
        String endpoint = BASE_PATH.concat("/role/".concat(invalidId).concat("/activate"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .patch(endpoint, mockSuperAdminToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(roleUpdateService, Mockito.never())
                .activate(Mockito.anyString());
    }


    @Test
    void givenValidId_whenRolePassivated_thenReturnSuccess() throws Exception {

        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        Mockito.doNothing()
                .when(roleUpdateService)
                .passivate(Mockito.any());

        // Then
        String endpoint = BASE_PATH.concat("/role/".concat(mockId).concat("/passivate"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .patch(endpoint, mockSuperAdminToken.getAccessToken());

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(roleUpdateService, Mockito.times(1))
                .passivate(Mockito.anyString());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "A",
            "493268349068342"
    })
    void givenId_whenIdIsNotValidForPassivation_thenReturnValidationError(String invalidId) throws Exception {

        // Then
        String endpoint = BASE_PATH.concat("/role/".concat(invalidId).concat("/passivate"));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .patch(endpoint, mockSuperAdminToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(roleUpdateService, Mockito.never())
                .passivate(Mockito.anyString());
    }


    @Test
    void givenValidId_whenRoleDeleted_thenReturnSuccess() throws Exception {
        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        Mockito.doNothing()
                .when(roleUpdateService)
                .delete(Mockito.any());

        // Then
        String endpoint = BASE_PATH.concat("/role/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .delete(endpoint, mockAdminToken.getAccessToken());

        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(roleUpdateService, Mockito.times(1))
                .delete(mockId);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "A",
            "493268349068342"
    })
    void givenId_whenIdDoesNotValid_thenReturnValidationError(String invalidId) throws Exception {

        // Then
        String endpoint = BASE_PATH.concat("/role/").concat(invalidId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .delete(endpoint, mockAdminToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorBuilder.VALIDATION_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isBadRequest())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .isNotEmpty());

        // Verify
        Mockito.verify(roleUpdateService, Mockito.never())
                .delete(Mockito.anyString());
    }

    @Test
    void givenValidId_whenRoleAlreadyDeleted_thenReturnConflict() throws Exception {
        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        AysRole mockRole = new AysRoleBuilder()
                .withValidValues()
                .withId(mockId)
                .withStatus(AysRoleStatus.DELETED)
                .build();

        Mockito.doThrow(new AysRoleAlreadyDeletedException(mockId))
                .when(roleUpdateService)
                .delete(mockRole.getId());

        // Then
        String endpoint = BASE_PATH.concat("/role/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .delete(endpoint, mockAdminToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorBuilder.ALREADY_EXIST;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isConflict())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(roleUpdateService, Mockito.times(1))
                .delete(mockRole.getId());
    }

    @Test
    void givenValidId_whenRoleUsing_thenReturnConflict() throws Exception {
        // Given
        String mockId = AysRandomUtil.generateUUID();

        // When
        Mockito.doThrow(new AysRoleAssignedToUserException(mockId))
                .when(roleUpdateService)
                .delete(mockId);

        // Then
        String endpoint = BASE_PATH.concat("/role/").concat(mockId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .delete(endpoint, mockAdminToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorBuilder.ALREADY_EXIST;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isConflict())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(roleUpdateService, Mockito.times(1))
                .delete(mockId);
    }

}
