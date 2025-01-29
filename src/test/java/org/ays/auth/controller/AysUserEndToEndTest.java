package org.ays.auth.controller;

import org.ays.AysEndToEndTest;
import org.ays.auth.model.AysRole;
import org.ays.auth.model.AysUser;
import org.ays.auth.model.AysUserBuilder;
import org.ays.auth.model.enums.AysUserStatus;
import org.ays.auth.model.mapper.AysUserToResponseMapper;
import org.ays.auth.model.request.AysUserCreateRequest;
import org.ays.auth.model.request.AysUserCreateRequestBuilder;
import org.ays.auth.model.request.AysUserListRequest;
import org.ays.auth.model.request.AysUserListRequestBuilder;
import org.ays.auth.model.request.AysUserUpdateRequest;
import org.ays.auth.model.request.AysUserUpdateRequestBuilder;
import org.ays.auth.model.response.AysUserResponse;
import org.ays.auth.model.response.AysUsersResponse;
import org.ays.auth.port.AysRoleReadPort;
import org.ays.auth.port.AysUserReadPort;
import org.ays.auth.port.AysUserSavePort;
import org.ays.common.model.AysPhoneNumberBuilder;
import org.ays.common.model.request.AysPhoneNumberRequestBuilder;
import org.ays.common.model.response.AysErrorResponse;
import org.ays.common.model.response.AysErrorResponseBuilder;
import org.ays.common.model.response.AysPageResponse;
import org.ays.common.model.response.AysResponse;
import org.ays.common.model.response.AysResponseBuilder;
import org.ays.common.util.AysRandomUtil;
import org.ays.institution.model.Institution;
import org.ays.institution.model.InstitutionBuilder;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.ays.util.AysValidTestData;
import org.ays.util.UUIDTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class AysUserEndToEndTest extends AysEndToEndTest {

    @Autowired
    private AysUserSavePort userSavePort;

    @Autowired
    private AysUserReadPort userReadPort;

    @Autowired
    private AysRoleReadPort roleReadPort;


    private final AysUserToResponseMapper userToResponseMapper = AysUserToResponseMapper.initialize();


    private static final String BASE_PATH = "/api/v1";


    @Test
    void givenValidUserListRequest_whenUsersFoundForSuperAdmin_thenReturnAysPageResponseOfUsersResponse() throws Exception {

        // Initialize
        Institution institution = new InstitutionBuilder()
                .withId(AysValidTestData.SuperAdmin.INSTITUTION_ID)
                .build();

        AysRole role = roleReadPort.findAllActivesByInstitutionId(institution.getId())
                .stream()
                .findFirst()
                .orElseThrow();

        userSavePort.save(
                new AysUserBuilder()
                        .withValidValues()
                        .withoutId()
                        .withFirstName("Öykü")
                        .withLastName("İzgi")
                        .withEmailAddress("oyku.izgi@afetyonetimsistemi.org")
                        .withCity("Iğdır")
                        .withStatus(AysUserStatus.ACTIVE)
                        .withRoles(List.of(role))
                        .withInstitution(institution)
                        .build()
        );

        // Given
        AysUserListRequest listRequest = new AysUserListRequestBuilder()
                .withValidValues()
                .withFirstName("öykü")
                .withLastName("İzgi")
                .withEmailAddress("oyku.izgi")
                .withCity("ığdır")
                .withStatuses(Set.of(AysUserStatus.ACTIVE))
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/users");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, superAdminToken.getAccessToken(), listRequest);


        AysResponse<AysPageResponse<AysUsersResponse>> mockResponse = AysResponseBuilder.successPage();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.content()
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.contentSize()
                        .value(1))
                .andExpect(AysMockResultMatchersBuilders.firstContent("id")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("emailAddress")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("firstName")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("lastName")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("phoneNumber")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("city")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("status")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("createdAt")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("updatedAt")
                        .doesNotHaveJsonPath());
    }

    @Test
    void givenValidUserListRequest_whenUsersFoundForAdmin_thenReturnAysPageResponseOfUsersResponse() throws Exception {

        // Given
        AysUserListRequest listRequest = new AysUserListRequestBuilder()
                .withValidValues()
                .withoutOrders()
                .withCity("Mersin")
                .withStatuses(Set.of(AysUserStatus.ACTIVE))
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/users");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, adminToken.getAccessToken(), listRequest);


        AysResponse<AysPageResponse<AysUsersResponse>> mockResponse = AysResponseBuilder.successPage();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.content()
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.contentSize()
                        .value(1))
                .andExpect(AysMockResultMatchersBuilders.firstContent("id")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("emailAddress")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("firstName")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("lastName")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("phoneNumber")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("city")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("status")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("createdAt")
                        .exists())
                .andExpect(AysMockResultMatchersBuilders.firstContent("updatedAt")
                        .doesNotHaveJsonPath());
    }


    @Test
    void givenValidUserId_whenUserExists_thenReturnUserResponse() throws Exception {

        // Initialize
        Institution institution = new InstitutionBuilder()
                .withId(AysValidTestData.Admin.INSTITUTION_ID)
                .build();

        AysRole role = roleReadPort.findAllActivesByInstitutionId(institution.getId())
                .stream()
                .findFirst()
                .orElseThrow();

        AysUser user = userSavePort.save(
                new AysUserBuilder()
                        .withValidValues()
                        .withoutId()
                        .withRoles(List.of(role))
                        .withInstitution(institution)
                        .build()
        );

        // Given
        String userId = user.getId();

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(userId);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(endpoint, adminToken.getAccessToken());

        AysUserResponse mockUserResponse = userToResponseMapper
                .map(user);

        AysResponse<AysUserResponse> mockResponse = AysResponse
                .successOf(mockUserResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("id")
                        .value(user.getId()))
                .andExpect(AysMockResultMatchersBuilders.response("emailAddress")
                        .value(user.getEmailAddress()))
                .andExpect(AysMockResultMatchersBuilders.response("firstName")
                        .value(user.getFirstName()))
                .andExpect(AysMockResultMatchersBuilders.response("lastName")
                        .value(user.getLastName()))
                .andExpect(AysMockResultMatchersBuilders.response("phoneNumber.countryCode")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("phoneNumber.lineNumber")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("city")
                        .value(user.getCity()))
                .andExpect(AysMockResultMatchersBuilders.response("status")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("roles[*].id")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("roles[*].name")
                        .isNotEmpty())
                .andExpect(AysMockResultMatchersBuilders.response("createdUser")
                        .value(user.getCreatedUser()))
                .andExpect(AysMockResultMatchersBuilders.response("createdAt")
                        .isNotEmpty());
    }


    @Test
    void givenUserCreateRequest_whenUserCreated_thenReturnSuccess() throws Exception {

        // Initialize
        Institution institution = new InstitutionBuilder()
                .withId(AysValidTestData.Admin.INSTITUTION_ID)
                .build();

        List<AysRole> roles = roleReadPort.findAllActivesByInstitutionId(institution.getId());

        // Given
        Set<String> roleIds = roles.stream()
                .map(AysRole::getId)
                .collect(Collectors.toSet());
        AysUserCreateRequest createRequest = new AysUserCreateRequestBuilder()
                .withValidValues()
                .withRoleIds(roleIds)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, adminToken.getAccessToken(), createRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Optional<AysUser> userFromDatabase = userReadPort.findByEmailAddress(createRequest.getEmailAddress());

        Assertions.assertTrue(userFromDatabase.isPresent());
        Assertions.assertNotNull(userFromDatabase.get().getId());
        Assertions.assertNotNull(userFromDatabase.get().getInstitution());
        Assertions.assertEquals(createRequest.getFirstName(), userFromDatabase.get().getFirstName());
        Assertions.assertEquals(createRequest.getLastName(), userFromDatabase.get().getLastName());
        Assertions.assertEquals(createRequest.getEmailAddress(), userFromDatabase.get().getEmailAddress());
        Assertions.assertEquals(createRequest.getPhoneNumber().getCountryCode(), userFromDatabase.get().getPhoneNumber().getCountryCode());
        Assertions.assertEquals(createRequest.getPhoneNumber().getLineNumber(), userFromDatabase.get().getPhoneNumber().getLineNumber());
        Assertions.assertEquals(createRequest.getCity(), userFromDatabase.get().getCity());
        Assertions.assertEquals(AysUserStatus.ACTIVE, userFromDatabase.get().getStatus());
        createRequest.getRoleIds()
                .forEach(roleId ->
                        Assertions.assertTrue(
                                userFromDatabase.get().getRoles().stream().anyMatch(role -> role.getId().equals(roleId))
                        )
                );
        Assertions.assertNotNull(userFromDatabase.get().getCreatedUser());
        Assertions.assertNotNull(userFromDatabase.get().getCreatedAt());
        Assertions.assertNull(userFromDatabase.get().getUpdatedUser());
        Assertions.assertNull(userFromDatabase.get().getUpdatedAt());
        Assertions.assertTrue(UUIDTestUtil.isValid(userFromDatabase.get().getCreatedUser()));
    }


    @Test
    void givenValidIdAndUserUpdateRequest_whenUserUpdated_thenReturnSuccess() throws Exception {

        // Initialize
        Institution institution = new InstitutionBuilder()
                .withId(AysValidTestData.Admin.INSTITUTION_ID)
                .build();

        List<AysRole> roles = roleReadPort.findAllActivesByInstitutionId(institution.getId());

        AysUser user = userSavePort.save(
                new AysUserBuilder()
                        .withValidValues()
                        .withoutId()
                        .withRoles(roles)
                        .withInstitution(institution)
                        .build()
        );

        // Given
        String id = user.getId();

        Set<String> roleIds = roles.stream()
                .map(AysRole::getId)
                .limit(1)
                .collect(Collectors.toSet());
        AysUserUpdateRequest updateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .withFirstName("John")
                .withLastName("Doe")
                .withEmailAddress("john.doe@afetyonetimsistemi.org")
                .withCity("Ankara")
                .withRoleIds(roleIds)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(id);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, adminToken.getAccessToken(), updateRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Optional<AysUser> userFromDatabase = userReadPort.findById(id);

        Assertions.assertTrue(userFromDatabase.isPresent());
        Assertions.assertNotNull(userFromDatabase.get().getId());
        Assertions.assertNotNull(userFromDatabase.get().getInstitution());
        Assertions.assertEquals(updateRequest.getFirstName(), userFromDatabase.get().getFirstName());
        Assertions.assertEquals(updateRequest.getLastName(), userFromDatabase.get().getLastName());
        Assertions.assertEquals(updateRequest.getEmailAddress(), userFromDatabase.get().getEmailAddress());
        Assertions.assertEquals(updateRequest.getPhoneNumber().getCountryCode(), userFromDatabase.get().getPhoneNumber().getCountryCode());
        Assertions.assertEquals(updateRequest.getPhoneNumber().getLineNumber(), userFromDatabase.get().getPhoneNumber().getLineNumber());
        Assertions.assertEquals(updateRequest.getCity(), userFromDatabase.get().getCity());
        updateRequest.getRoleIds()
                .forEach(roleId ->
                        Assertions.assertTrue(
                                userFromDatabase.get().getRoles().stream().anyMatch(role -> role.getId().equals(roleId))
                        )
                );
        Assertions.assertNotNull(userFromDatabase.get().getUpdatedUser());
        Assertions.assertNotNull(userFromDatabase.get().getUpdatedAt());
        Assertions.assertTrue(UUIDTestUtil.isValid(userFromDatabase.get().getUpdatedUser()));
    }

    @Test
    void givenValidIdAndUserUpdateRequest_whenRolesUpdatedOnly_thenReturnSuccess() throws Exception {

        // Initialize
        Institution institution = new InstitutionBuilder()
                .withId(AysValidTestData.Admin.INSTITUTION_ID)
                .build();

        List<AysRole> roles = roleReadPort.findAllActivesByInstitutionId(institution.getId());

        AysUser user = userSavePort.save(
                new AysUserBuilder()
                        .withValidValues()
                        .withFirstName("Test")
                        .withLastName("Dene")
                        .withEmailAddress("Test.deneme@afetyonetimsistemi.org")
                        .withCity("İzmir")
                        .withPhoneNumber(new AysPhoneNumberBuilder().withValidValues().build())
                        .withoutId()
                        .withRoles(roles)
                        .withInstitution(institution)
                        .build()
        );

        // Given
        String id = user.getId();
        AysRole newRole = roles.get(0);
        List<AysRole> newRoles = List.of(newRole);

        Set<String> newRoleIds = newRoles.stream()
                .map(AysRole::getId)
                .limit(1)
                .collect(Collectors.toSet());

        AysUserUpdateRequest updateRequest = new AysUserUpdateRequestBuilder()
                .withValidValues()
                .withFirstName("Test")
                .withLastName("Dene")
                .withEmailAddress("Test.deneme@afetyonetimsistemi.org")
                .withCity("İzmir")
                .withPhoneNumber(new AysPhoneNumberRequestBuilder().withValidValues().build())
                .withRoleIds(newRoleIds)
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(id);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, adminToken.getAccessToken(), updateRequest);

        AysResponse<Void> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Optional<AysUser> userFromDatabase = userReadPort.findById(id);

        Assertions.assertTrue(userFromDatabase.isPresent());
        Assertions.assertNotNull(userFromDatabase.get().getId());
        Assertions.assertNotNull(userFromDatabase.get().getInstitution());
        newRoleIds.forEach(roleId -> Assertions.assertTrue(
                        userFromDatabase.get().getRoles().stream()
                                .anyMatch(role -> role.getId().equals(roleId))
                )
        );
        Assertions.assertNotNull(userFromDatabase.get().getUpdatedUser());
        Assertions.assertNotNull(userFromDatabase.get().getUpdatedAt());
        Assertions.assertTrue(UUIDTestUtil.isValid(userFromDatabase.get().getUpdatedUser()));
    }


    @Test
    void givenValidId_whenActivateUser_thenReturnSuccess() throws Exception {

        // Initialize
        Institution institution = new InstitutionBuilder()
                .withId(AysValidTestData.Admin.INSTITUTION_ID)
                .build();

        List<AysRole> roles = roleReadPort.findAllActivesByInstitutionId(institution.getId());

        AysUser user = userSavePort.save(
                new AysUserBuilder()
                        .withId(AysRandomUtil.generateUUID())
                        .withValidValues()
                        .withRoles(roles)
                        .withInstitution(institution)
                        .withStatus(AysUserStatus.PASSIVE)
                        .build()
        );

        // Given
        String id = user.getId();

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(id).concat("/activate");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .patch(endpoint, adminToken.getAccessToken());

        AysResponse<Void> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Optional<AysUser> userFromDatabase = userReadPort.findById(user.getId());

        Assertions.assertTrue(userFromDatabase.isPresent());
        Assertions.assertEquals(userFromDatabase.get().getId(), user.getId());
        Assertions.assertEquals(AysUserStatus.ACTIVE, userFromDatabase.get().getStatus());
        Assertions.assertTrue(UUIDTestUtil.isValid(userFromDatabase.get().getUpdatedUser()));
    }

    @Test
    void givenAlreadyActiveUserId_whenActivateUser_thenReturnUserAlreadyActiveError() throws Exception {

        // Initialize
        Institution institution = new InstitutionBuilder()
            .withId(AysValidTestData.Admin.INSTITUTION_ID)
            .build();

        List<AysRole> roles = roleReadPort.findAllActivesByInstitutionId(institution.getId());

        AysUser user = userSavePort.save(
            new AysUserBuilder()
                .withId(AysRandomUtil.generateUUID())
                .withValidValues()
                .withRoles(roles)
                .withInstitution(institution)
                .withStatus(AysUserStatus.ACTIVE)
                .build()
        );

        // Given
        String id = user.getId();

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(id).concat("/activate");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
            .patch(endpoint, adminToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.CONFLICT_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
            .andExpect(AysMockResultMatchersBuilders.status()
                .isConflict())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                .value("user is already active!"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess")
                .value(false))
            .andExpect(MockMvcResultMatchers.jsonPath("$.time").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").exists());

        // Verify
        Optional<AysUser> userFromDatabase = userReadPort.findById(id);

        Assertions.assertTrue(userFromDatabase.isPresent());
        Assertions.assertEquals(userFromDatabase.get().getId(), user.getId());
        Assertions.assertEquals(AysUserStatus.ACTIVE, userFromDatabase.get().getStatus());
        Assertions.assertNull(userFromDatabase.get().getUpdatedUser());

    }

    @Test
    void givenNotVerifiedUserId_whenActivateUser_thenReturnUserNotPassiveError() throws Exception {

        // Initialize
        Institution institution = new InstitutionBuilder()
            .withId(AysValidTestData.Admin.INSTITUTION_ID)
            .build();

        List<AysRole> roles = roleReadPort.findAllActivesByInstitutionId(institution.getId());

        AysUser user = userSavePort.save(
            new AysUserBuilder()
                .withId(AysRandomUtil.generateUUID())
                .withValidValues()
                .withRoles(roles)
                .withInstitution(institution)
                .withStatus(AysUserStatus.NOT_VERIFIED)
                .build()
        );

        // Given
        String id = user.getId();

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(id).concat("/activate");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
            .patch(endpoint, adminToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.CONFLICT_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
            .andExpect(AysMockResultMatchersBuilders.status()
                .isConflict())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                .value("user is not passive!"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess")
                .value(false))
            .andExpect(MockMvcResultMatchers.jsonPath("$.time").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").exists());

        // Verify
        Optional<AysUser> userFromDatabase = userReadPort.findById(id);

        Assertions.assertTrue(userFromDatabase.isPresent());
        Assertions.assertEquals(userFromDatabase.get().getId(), user.getId());
        Assertions.assertEquals(AysUserStatus.NOT_VERIFIED, userFromDatabase.get().getStatus());
        Assertions.assertNull(userFromDatabase.get().getUpdatedUser());

    }

    @Test
    void givenDeletedUserId_whenActivateUser_thenReturnUserNotPassiveError() throws Exception {

        // Initialize
        Institution institution = new InstitutionBuilder()
            .withId(AysValidTestData.Admin.INSTITUTION_ID)
            .build();

        List<AysRole> roles = roleReadPort.findAllActivesByInstitutionId(institution.getId());

        AysUser user = userSavePort.save(
            new AysUserBuilder()
                .withId(AysRandomUtil.generateUUID())
                .withValidValues()
                .withRoles(roles)
                .withInstitution(institution)
                .withStatus(AysUserStatus.DELETED)
                .build()
        );

        // Given
        String id = user.getId();

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(id).concat("/activate");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
            .patch(endpoint, adminToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.CONFLICT_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
            .andExpect(AysMockResultMatchersBuilders.status()
                .isConflict())
            .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                .value("user is not passive!"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess")
                .value(false))
            .andExpect(MockMvcResultMatchers.jsonPath("$.time").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").exists());

        // Verify
        Optional<AysUser> userFromDatabase = userReadPort.findById(id);

        Assertions.assertTrue(userFromDatabase.isPresent());
        Assertions.assertEquals(userFromDatabase.get().getId(), user.getId());
        Assertions.assertEquals(AysUserStatus.DELETED, userFromDatabase.get().getStatus());
        Assertions.assertNull(userFromDatabase.get().getUpdatedUser());

    }



    @Test
    void givenValidId_whenUserDeleted_thenReturnSuccess() throws Exception {

        // Initialize
        Institution institution = new InstitutionBuilder()
                .withId(AysValidTestData.Admin.INSTITUTION_ID)
                .build();

        AysRole role = roleReadPort.findAllActivesByInstitutionId(institution.getId())
                .stream()
                .findFirst()
                .orElseThrow();

        AysUser user = userSavePort.save(
                new AysUserBuilder()
                        .withValidValues()
                        .withoutId()
                        .withFirstName("Violet")
                        .withLastName("Ync")
                        .withEmailAddress("violet.ync@afetyonetimsistemi.org")
                        .withCity("Ankara")
                        .withStatus(AysUserStatus.ACTIVE)
                        .withRoles(List.of(role))
                        .withInstitution(institution)
                        .build()
        );

        // Given
        String id = user.getId();

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(id);
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .delete(endpoint, adminToken.getAccessToken());

        AysResponse<Void> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Optional<AysUser> userFromDatabase = userReadPort.findById(id);

        Assertions.assertTrue(userFromDatabase.isPresent());
        Assertions.assertNotNull(userFromDatabase.get().getId());
        Assertions.assertEquals(AysUserStatus.DELETED, userFromDatabase.get().getStatus());
        Assertions.assertTrue(UUIDTestUtil.isValid(userFromDatabase.get().getUpdatedUser()));
    }


    @Test
    void givenValidId_whenPassivateUser_thenReturnSuccess() throws Exception {

        // Initialize
        Institution institution = new InstitutionBuilder()
                .withId(AysValidTestData.Admin.INSTITUTION_ID)
                .build();

        List<AysRole> roles = roleReadPort.findAllActivesByInstitutionId(institution.getId());

        AysUser user = userSavePort.save(
                new AysUserBuilder()
                        .withId(AysRandomUtil.generateUUID())
                        .withValidValues()
                        .withRoles(roles)
                        .withInstitution(institution)
                        .withStatus(AysUserStatus.ACTIVE)
                        .build()
        );

        // Given
        String id = user.getId();

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(id).concat("/passivate");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .patch(endpoint, adminToken.getAccessToken());

        AysResponse<Void> mockResponse = AysResponseBuilder.success();

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Optional<AysUser> userFromDatabase = userReadPort.findById(user.getId());

        Assertions.assertTrue(userFromDatabase.isPresent());
        Assertions.assertEquals(userFromDatabase.get().getId(), user.getId());
        Assertions.assertEquals(AysUserStatus.PASSIVE, userFromDatabase.get().getStatus());
        Assertions.assertTrue(UUIDTestUtil.isValid(userFromDatabase.get().getUpdatedUser()));
    }

    @Test
    void givenAlreadyPassiveUserId_whenTryToPassivate_thenReturnUserAlreadyPassiveError() throws Exception {

        // Initialize
        Institution institution = new InstitutionBuilder()
                .withId(AysValidTestData.Admin.INSTITUTION_ID)
                .build();

        List<AysRole> roles = roleReadPort.findAllActivesByInstitutionId(institution.getId());

        AysUser user = userSavePort.save(
                new AysUserBuilder()
                        .withValidValues()
                        .withoutId()
                        .withRoles(roles)
                        .withInstitution(institution)
                        .withStatus(AysUserStatus.PASSIVE)
                        .build()
        );

        // Given
        String userId = user.getId();

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(userId).concat("/passivate");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .patch(endpoint, adminToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.CONFLICT_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("user is already passive!"));

        // Verify
        Optional<AysUser> userFromDatabase = userReadPort.findById(userId);

        Assertions.assertTrue(userFromDatabase.isPresent());
        Assertions.assertEquals(userFromDatabase.get().getId(), user.getId());
        Assertions.assertEquals(AysUserStatus.PASSIVE, userFromDatabase.get().getStatus());
        Assertions.assertNull(userFromDatabase.get().getUpdatedUser());
        Assertions.assertNull(userFromDatabase.get().getUpdatedAt());
    }

    @ParameterizedTest
    @EnumSource(value = AysUserStatus.class, names = {
            "NOT_VERIFIED",
            "DELETED"
    })
    void givenInactiveUserId_whenTryToPassivate_thenReturnUserNotActiveError(AysUserStatus status) throws Exception {

        // Initialize
        Institution institution = new InstitutionBuilder()
                .withId(AysValidTestData.Admin.INSTITUTION_ID)
                .build();

        List<AysRole> roles = roleReadPort.findAllActivesByInstitutionId(institution.getId());

        AysUser user = userSavePort.save(
                new AysUserBuilder()
                        .withValidValues()
                        .withoutId()
                        .withRoles(roles)
                        .withInstitution(institution)
                        .withStatus(status)
                        .build()
        );

        // Given
        String userId = user.getId();

        // Then
        String endpoint = BASE_PATH.concat("/user/").concat(userId).concat("/passivate");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .patch(endpoint, adminToken.getAccessToken());

        AysErrorResponse mockErrorResponse = AysErrorResponseBuilder.CONFLICT_ERROR;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("user is not active! userId:" + userId));

        // Verify
        Optional<AysUser> userFromDatabase = userReadPort.findById(userId);

        Assertions.assertTrue(userFromDatabase.isPresent());
        Assertions.assertEquals(userFromDatabase.get().getId(), user.getId());
        Assertions.assertEquals(status, userFromDatabase.get().getStatus());
        Assertions.assertNull(userFromDatabase.get().getUpdatedUser());
        Assertions.assertNull(userFromDatabase.get().getUpdatedAt());
    }

}
