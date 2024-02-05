package com.ays.admin_user.controller;

import com.ays.AbstractRestControllerTest;
import com.ays.admin_user.model.AdminUser;
import com.ays.admin_user.model.dto.request.AdminUserListRequest;
import com.ays.admin_user.model.dto.request.AdminUserListRequestBuilder;
import com.ays.admin_user.model.dto.response.AdminUsersResponse;
import com.ays.admin_user.model.entity.AdminUserEntity;
import com.ays.admin_user.model.entity.AdminUserEntityBuilder;
import com.ays.admin_user.model.mapper.AdminUserEntityToAdminUserMapper;
import com.ays.admin_user.model.mapper.AdminUserToAdminUsersResponseMapper;
import com.ays.admin_user.service.AdminUserService;
import com.ays.common.model.AysPage;
import com.ays.common.model.dto.response.AysPageResponse;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.util.exception.model.AysError;
import com.ays.common.util.exception.model.AysErrorBuilder;
import com.ays.util.AysMockMvcRequestBuilders;
import com.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

class AdminUserControllerTest extends AbstractRestControllerTest {

    @MockBean
    private AdminUserService adminUserService;


    private final AdminUserToAdminUsersResponseMapper adminUserToAdminUsersResponseMapper = AdminUserToAdminUsersResponseMapper.initialize();

    private final AdminUserEntityToAdminUserMapper adminUserEntityToAdminUserMapper = AdminUserEntityToAdminUserMapper.initialize();


    private static final String BASE_PATH = "/api/v1";

    @Test
    void givenValidUserListRequest_whenAdminUsersFound_thenReturnAdminUsersResponse() throws Exception {

        // Given
        AdminUserListRequest mockAdminUserListRequest = new AdminUserListRequestBuilder()
                .withValidValues()
                .build();

        // When
        Page<AdminUserEntity> mockAdminUserEntities = new PageImpl<>(
                AdminUserEntityBuilder.generateValidUserEntities(1)
        );
        List<AdminUser> mockAdminUsers = adminUserEntityToAdminUserMapper.map(mockAdminUserEntities.getContent());
        AysPage<AdminUser> mockAysPageOfUsers = AysPage.of(mockAdminUserEntities, mockAdminUsers);
        Mockito.when(adminUserService.getAdminUsers(Mockito.any(AdminUserListRequest.class)))
                .thenReturn(mockAysPageOfUsers);

        // Then
        String endpoint = BASE_PATH.concat("/admins");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockAdminUserToken.getAccessToken(), mockAdminUserListRequest);

        List<AdminUsersResponse> mockAdminUsersResponses = adminUserToAdminUsersResponseMapper.map(mockAysPageOfUsers.getContent());
        AysPageResponse<AdminUsersResponse> pageOfAdminUsersResponse = AysPageResponse.<AdminUsersResponse>builder()
                .of(mockAysPageOfUsers)
                .content(mockAdminUsersResponses)
                .build();
        AysResponse<AysPageResponse<AdminUsersResponse>> mockResponse = AysResponse.successOf(pageOfAdminUsersResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

        // Verify
        Mockito.verify(adminUserService, Mockito.times(1))
                .getAdminUsers(Mockito.any(AdminUserListRequest.class));
    }

    @Test
    void givenValidUserListRequest_whenAdminUserUnauthorizedForListing_thenReturnAccessDeniedException() throws Exception {
        // Given
        AdminUserListRequest mockAdminUserListRequest = new AdminUserListRequestBuilder()
                .withValidValues()
                .build();

        // Then
        String endpoint = BASE_PATH.concat("/admins");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .post(endpoint, mockUserToken.getAccessToken(), mockAdminUserListRequest);

        AysError mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(adminUserService, Mockito.never())
                .getAdminUsers(Mockito.any(AdminUserListRequest.class));
    }

}
