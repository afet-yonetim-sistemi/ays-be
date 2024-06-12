package org.ays.user.controller;

import org.ays.AbstractRestControllerTest;
import org.ays.common.model.response.AysErrorResponse;
import org.ays.common.model.response.AysResponse;
import org.ays.common.util.exception.model.AysErrorBuilder;
import org.ays.user.model.User;
import org.ays.user.model.UserBuilder;
import org.ays.user.model.dto.request.UserSupportStatusUpdateRequest;
import org.ays.user.model.dto.request.UserSupportStatusUpdateRequestBuilder;
import org.ays.user.model.dto.response.UserSelfResponse;
import org.ays.user.model.enums.UserSupportStatus;
import org.ays.user.model.mapper.UserToUserSelfResponseMapper;
import org.ays.user.service.UserSelfService;
import org.ays.util.AysMockMvcRequestBuilders;
import org.ays.util.AysMockResultMatchersBuilders;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;


class UserSelfControllerTest extends AbstractRestControllerTest {

    @MockBean
    private UserSelfService userSelfService;


    private final UserToUserSelfResponseMapper userToUserSelfResponseMapper = UserToUserSelfResponseMapper.initialize();


    private static final String BASE_PATH = "/api/v1/user-self";

    @Test
    void givenValidUserToken_whenUserFound_thenReturnUserSelfResponse() throws Exception {
        // Given
        String mockAccessToken = mockUserToken.getAccessToken();

        // When
        User mockUser = new UserBuilder().build();
        Mockito.when(userSelfService.getUserSelfInformation())
                .thenReturn(mockUser);

        // Then
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(BASE_PATH, mockAccessToken);

        UserSelfResponse mockUserSelfResponse = userToUserSelfResponseMapper.map(mockUser);
        AysResponse<UserSelfResponse> mockResponse = AysResponse.successOf(mockUserSelfResponse);

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .isNotEmpty());

        // Verify
        Mockito.verify(userSelfService, Mockito.times(1))
                .getUserSelfInformation();
    }

    @Test
    void givenInvalidAccessToken_whenUserUnauthorizedForGetting_thenReturnAccessDeniedException() throws Exception {
        // Given
        String mockAccessToken = mockAdminUserToken.getAccessToken();

        // Then
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .get(BASE_PATH, mockAccessToken);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(userSelfService, Mockito.never())
                .getUserSelfInformation();
    }


    @Test
    void givenValidUserSupportStatusUpdateRequest_whenUserRole_thenReturnSuccess() throws Exception {

        // Given
        UserSupportStatus userSupportStatus = UserSupportStatus.READY;

        UserSupportStatusUpdateRequest mockUpdateRequest = new UserSupportStatusUpdateRequestBuilder()
                .withSupportStatus(userSupportStatus).build();

        // When
        Mockito.doNothing().when(userSelfService)
                .updateUserSupportStatus(Mockito.any(UserSupportStatusUpdateRequest.class));

        // Then
        String endpoint = BASE_PATH.concat("/status/support");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockUserToken.getAccessToken(), mockUpdateRequest);

        AysResponse<Void> mockResponse = AysResponse.SUCCESS;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isOk())
                .andExpect(AysMockResultMatchersBuilders.response()
                        .doesNotExist());

        // Verify
        Mockito.verify(userSelfService, Mockito.times(1))
                .updateUserSupportStatus(Mockito.any(UserSupportStatusUpdateRequest.class));
    }

    @Test
    void givenValidUserSupportStatusUpdateRequest_whenAdminRole_thenReturnAccessDeniedException() throws Exception {
        // Given
        UserSupportStatus userSupportStatus = UserSupportStatus.READY;

        UserSupportStatusUpdateRequest mockUpdateRequest = new UserSupportStatusUpdateRequestBuilder()
                .withSupportStatus(userSupportStatus).build();

        // Then
        String endpoint = BASE_PATH.concat("/status/support");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = AysMockMvcRequestBuilders
                .put(endpoint, mockAdminUserToken.getAccessToken(), mockUpdateRequest);

        AysErrorResponse mockErrorResponse = AysErrorBuilder.FORBIDDEN;

        aysMockMvc.perform(mockHttpServletRequestBuilder, mockErrorResponse)
                .andExpect(AysMockResultMatchersBuilders.status()
                        .isForbidden())
                .andExpect(AysMockResultMatchersBuilders.subErrors()
                        .doesNotExist());

        // Verify
        Mockito.verify(userSelfService, Mockito.never())
                .updateUserSupportStatus(Mockito.any(UserSupportStatusUpdateRequest.class));
    }

}
