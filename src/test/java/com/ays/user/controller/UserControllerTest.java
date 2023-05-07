package com.ays.user.controller;

import com.ays.AbstractRestControllerTest;
import com.ays.common.model.AysPage;
import com.ays.common.model.AysPhoneNumberBuilder;
import com.ays.common.model.dto.response.AysPageResponse;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.model.dto.response.AysResponseBuilder;
import com.ays.common.util.AysRandomUtil;
import com.ays.user.model.User;
import com.ays.user.model.UserBuilder;
import com.ays.user.model.dto.request.*;
import com.ays.user.model.dto.response.UserSavedResponse;
import com.ays.user.model.dto.response.UserSavedResponseBuilder;
import com.ays.user.model.dto.response.UsersResponse;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.model.entity.UserEntityBuilder;
import com.ays.user.model.enums.UserStatus;
import com.ays.user.model.mapper.UserEntityToUserMapper;
import com.ays.user.model.mapper.UserToUsersResponseMapper;
import com.ays.user.service.UserSaveService;
import com.ays.user.service.UserService;
import com.ays.util.AysMockMvcRequestBuilders;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends AbstractRestControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private UserSaveService userSaveService;

    private static final UserToUsersResponseMapper USER_TO_USERS_RESPONSE_MAPPER = UserToUsersResponseMapper.initialize();
    private static final UserEntityToUserMapper USER_ENTITY_TO_USER_MAPPER = UserEntityToUserMapper.initialize();

    private static final String BASE_PATH = "/api/v1/user";

    @Test
    void givenValidUserSaveRequest_whenUserSaved_thenReturnUserSavedResponse() throws Exception {
        // Given
        UserSaveRequest mockUserSaveRequest = new UserSaveRequestBuilder()
                .withPhoneNumber(new AysPhoneNumberBuilder().withValidFields().build())
                .build();

        User mockUser = new UserBuilder()
                .withUsername("123456")
                .withPassword("987654")
                .withStatus(UserStatus.ACTIVE).build();

        UserSavedResponse mockUserSavedResponse = new UserSavedResponseBuilder()
                .withUsername(mockUser.getUsername())
                .withPassword(mockUser.getPassword())
                .build();
        AysResponse<UserSavedResponse> mockResponse = AysResponseBuilder.successOf(mockUserSavedResponse);

        // When
        Mockito.when(userSaveService.saveUser(Mockito.any(UserSaveRequest.class)))
                .thenReturn(mockUser);

        // Then
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(BASE_PATH, mockAdminUserToken.getAccessToken(), mockUserSaveRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.time").isNotEmpty())
                .andExpect(jsonPath("$.httpStatus").value(mockResponse.getHttpStatus().getReasonPhrase()))
                .andExpect(jsonPath("$.isSuccess").value(mockResponse.getIsSuccess()))
                .andExpect(jsonPath("$.response").isNotEmpty())
                .andExpect(jsonPath("$.response.username").value(mockResponse.getResponse().getUsername()))
                .andExpect(jsonPath("$.response.password").value(mockResponse.getResponse().getPassword()));

        Mockito.verify(userSaveService, Mockito.times(1))
                .saveUser(Mockito.any(UserSaveRequest.class));
    }

    @Test
    void givenUserListRequest_whenUserToken_thenAysResponseSuccess() throws Exception {
        // Given
        final UserListRequest mockUserListRequest = UserListRequestBuilder.VALID
                .withSort(null).build();

        Page<UserEntity> mockUserEntities = new PageImpl<>(
                UserEntityBuilder.generateValidUserEntities(1)
        );
        List<User> mockUsers = USER_ENTITY_TO_USER_MAPPER.map(mockUserEntities.getContent());
        AysPage<User> mockAysPageOfUsers = AysPage.of(mockUserEntities, mockUsers);

        List<UsersResponse> mockUsersResponses = USER_TO_USERS_RESPONSE_MAPPER.map(mockAysPageOfUsers.getContent());

        // when
        Mockito.when(userService.getAllUsers(mockUserListRequest))
                .thenReturn(mockAysPageOfUsers);

        // Then
        AysPageResponse<UsersResponse> pageOfUsersResponse = AysPageResponse.<UsersResponse>builder()
                .of(mockAysPageOfUsers)
                .content(mockUsersResponses)
                .build();
        AysResponse<AysPageResponse<UsersResponse>> mockAysResponse = AysResponse.successOf(pageOfUsersResponse);
        mockMvc.perform(AysMockMvcRequestBuilders
                        .get(BASE_PATH, mockAdminUserToken.getAccessToken(), mockUserListRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.time").isNotEmpty())
                .andExpect(jsonPath("$.httpStatus").value(mockAysResponse.getHttpStatus().getReasonPhrase()))
                .andExpect(jsonPath("$.isSuccess").value(mockAysResponse.getIsSuccess()))
                .andExpect(jsonPath("$.response").isNotEmpty());

        Mockito.verify(userService, Mockito.times(1))
                .getAllUsers(mockUserListRequest);
    }


    @Test
    void givenUserId_whenAdminUserToken_thenReturnUserResponseSuccess() throws Exception {
        // Given
        String id = AysRandomUtil.generateUUID();
        User mockUser = new UserBuilder().build();

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin",
                null, Collections.singleton(new SimpleGrantedAuthority("ADMIN"))));
        SecurityContextHolder.setContext(securityContext);

        // When
        Mockito.when(userService.getUserById(id)).thenReturn(mockUser);

        // Then
        mockMvc.perform(AysMockMvcRequestBuilders
                        .get(BASE_PATH.concat("/" + id), mockAdminUserToken.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(userService, Mockito.times(1)).getUserById(id);
    }

    @Test
    void givenUserId_whenAdminUserToken_thenDeleteUserSuccess() throws Exception {
        // Given
        String id = AysRandomUtil.generateUUID();

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin",
                null, Collections.singleton(new SimpleGrantedAuthority("ADMIN"))));
        SecurityContextHolder.setContext(securityContext);

        // When
        Mockito.doNothing().when(userService).deleteUser(id);

        // Then
        mockMvc.perform(AysMockMvcRequestBuilders
                        .delete(BASE_PATH.concat("/" + id), mockAdminUserToken.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(userService, Mockito.times(1)).deleteUser(id);
    }

    @Test
    void givenUserId_whenAdminUserToken_thenUpdateUserSuccess() throws Exception {
        // Given
        UserUpdateRequest mockupdateRequest = UserUpdateRequest.builder().build();

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin",
                null, Collections.singleton(new SimpleGrantedAuthority("ADMIN"))));
        SecurityContextHolder.setContext(securityContext);

        // When
        Mockito.doNothing().when(userService).updateUser(mockupdateRequest);

        // Then
        mockMvc.perform(AysMockMvcRequestBuilders
                        .put(BASE_PATH, mockAdminUserToken.getAccessToken(), mockupdateRequest))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(userService, Mockito.times(1)).updateUser(mockupdateRequest);
    }
}