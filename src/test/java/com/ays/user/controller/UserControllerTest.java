package com.ays.user.controller;

import com.ays.AbstractRestControllerTest;
import com.ays.auth.model.AysToken;
import com.ays.auth.model.AysTokenBuilder;
import com.ays.common.model.AysPage;
import com.ays.common.model.AysSorting;
import com.ays.common.model.dto.response.AysResponse;
import com.ays.common.model.dto.response.AysResponseBuilder;
import com.ays.user.model.User;
import com.ays.user.model.UserBuilder;
import com.ays.user.model.dto.request.*;
import com.ays.user.model.mapper.UserToUserResponseMapper;
import com.ays.user.model.mapper.UserToUsersResponseMapper;
import com.ays.user.service.UserService;
import com.ays.util.AysMockMvcRequestBuilders;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends AbstractRestControllerTest {

    @MockBean
    private UserService userService;

    private final UserToUserResponseMapper userToUserResponseMapper = UserToUserResponseMapper.initialize();
    private final UserToUsersResponseMapper userToUsersResponseMapper = UserToUsersResponseMapper.initialize();

    private static final String BASE_PATH = "/api/v1/user";


    @Test
    void givenUserSaveRequest_whenAdminUserToken_thenReturnAysResponseSuccess() throws Exception {
        // Given
        UserSaveRequest mockUserSaveRequest = new UserSaveRequestBuilder()
                .withFirstName("First Name")
                .withLastName("Last Name")
                .build();
        AysToken mockAysToken = AysTokenBuilder.VALID_FOR_ADMIN;
        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin",
                null, Collections.singleton(new SimpleGrantedAuthority("ADMIN"))));
        SecurityContextHolder.setContext(securityContext);

        // When
        Mockito.doNothing().when(userService).saveUser(mockUserSaveRequest);

        // Then
        mockMvc.perform(AysMockMvcRequestBuilders
                        .post(BASE_PATH, mockAysToken.getAccessToken(), mockUserSaveRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.time").isNotEmpty())
                .andExpect(jsonPath("$.httpStatus").value(mockResponse.getHttpStatus().getReasonPhrase()))
                .andExpect(jsonPath("$.isSuccess").value(mockResponse.getIsSuccess()))
                .andExpect(jsonPath("$.response").doesNotExist());

        Mockito.verify(userService, Mockito.times(1)).saveUser(mockUserSaveRequest);

    }

    @Test
    void givenUserListRequest_whenUserToken_thenAysResponseSuccess() throws Exception {
        // Given
        final UserListRequest listRequest = UserListRequestBuilder.VALID.build();

        listRequest.getSort().add(AysSorting.builder()
                .property("username")
                .direction(Sort.Direction.ASC)
                .build());

        final List<User> users = List.of(User.builder().build());
        final AysPage<User> pageOfUsers = AysPage.<User>builder().content(users).build();

        AysToken mockAysToken = AysTokenBuilder.VALID_FOR_ADMIN;

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin",
                null, Collections.singleton(new SimpleGrantedAuthority("ADMIN"))));
        SecurityContextHolder.setContext(securityContext);

        // when
        Mockito.when(userService.getAllUsers(listRequest)).thenReturn(pageOfUsers);

        // Then
        mockMvc.perform(AysMockMvcRequestBuilders
                        .get(BASE_PATH, mockAysToken.getAccessToken(), listRequest))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(userService, Mockito.times(1)).getAllUsers(listRequest);
    }


    @Test
    void givenUserId_whenAdminUserToken_thenReturnUserResponseSuccess() throws Exception {
        // Given
        String id = UUID.randomUUID().toString();
        User mockUser = new UserBuilder().build();

        AysToken mockAysToken = AysTokenBuilder.VALID_FOR_ADMIN;

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin",
                null, Collections.singleton(new SimpleGrantedAuthority("ADMIN"))));
        SecurityContextHolder.setContext(securityContext);

        // When
        Mockito.when(userService.getUserById(id)).thenReturn(mockUser);

        // Then
        mockMvc.perform(AysMockMvcRequestBuilders
                        .get(BASE_PATH.concat("/" + id), mockAysToken.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(userService, Mockito.times(1)).getUserById(id);
    }

    @Test
    void givenUserId_whenAdminUserToken_thenDeleteUserSuccess() throws Exception {
        // Given
        String id = UUID.randomUUID().toString();

        AysToken mockAysToken = AysTokenBuilder.VALID_FOR_ADMIN;

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin",
                null, Collections.singleton(new SimpleGrantedAuthority("ADMIN"))));
        SecurityContextHolder.setContext(securityContext);

        // When
        Mockito.doNothing().when(userService).deleteUser(id);

        // Then
        mockMvc.perform(AysMockMvcRequestBuilders
                        .delete(BASE_PATH.concat("/" + id), mockAysToken.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(userService, Mockito.times(1)).deleteUser(id);
    }

    @Test
    void givenUserId_whenAdminUserToken_thenUpdateUserSuccess() throws Exception {
        // Given
        UserUpdateRequest mockupdateRequest = UserUpdateRequest.builder().build();

        AysToken mockAysToken = AysTokenBuilder.VALID_FOR_ADMIN;
        AysResponse<Void> mockResponse = AysResponseBuilder.SUCCESS;

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin",
                null, Collections.singleton(new SimpleGrantedAuthority("ADMIN"))));
        SecurityContextHolder.setContext(securityContext);

        // When
        Mockito.doNothing().when(userService).updateUser(mockupdateRequest);

        // Then
        mockMvc.perform(AysMockMvcRequestBuilders
                        .put(BASE_PATH, mockAysToken.getAccessToken(), mockupdateRequest))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(userService, Mockito.times(1)).updateUser(mockupdateRequest);
    }
}