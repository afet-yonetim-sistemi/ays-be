package com.ays.backend.user.controller;

import com.ays.backend.base.BaseRestControllerTest;
import com.ays.backend.user.controller.payload.request.AdminLoginRequest;
import com.ays.backend.user.controller.payload.request.AdminRegisterRequest;
import com.ays.backend.user.controller.payload.response.AuthResponse;
import com.ays.backend.user.controller.payload.response.MessageResponse;
import com.ays.backend.user.model.entities.User;
import com.ays.backend.user.model.entities.UserBuilder;
import com.ays.backend.user.service.AuthService;
import com.ays.backend.user.service.dto.UserDTO;
import com.ays.backend.user.service.dto.UserTokenDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AuthControllerTest extends BaseRestControllerTest {

    private final String ADMIN_CONTROLLER_BASEURL = "/api/v1/admin";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    void shouldRegisterForAdmin() throws Exception {

        // Given
        AdminRegisterRequest registerRequest = new UserBuilder().getRegisterRequest();

        User user = new UserBuilder()
                .withRegisterRequest(registerRequest,passwordEncoder).build();

        UserDTO userDto = UserDTO.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userRole(user.getUserRole())
                .countryCode(user.getCountryCode())
                .lineNumber(user.getLineNumber())
                .userStatus(user.getStatus())
                .email(user.getEmail())
                .lastLoginDate(user.getLastLoginDate())
                .build();


        MessageResponse messageResponse = new MessageResponse("success");


        // when
        when(authService.register(registerRequest)).thenReturn(userDto);

        // then - Perform the POST request and assert the response
        mockMvc.perform(post(ADMIN_CONTROLLER_BASEURL + "/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value(messageResponse.getMessage()));

    }

    @Test
    void shouldLoginForAdmin() throws Exception {

        AdminLoginRequest loginRequest = new UserBuilder().getLoginRequest();


        UserTokenDTO userTokenDTO = UserTokenDTO.builder()
                .expireDate(new Date().getTime() + 120000)
                .refreshToken("refreshToken")
                .accessToken("Bearer access-token")
                .username("adminUsername")
                .build();

        AuthResponse authResponse = AuthResponse.builder()
                .expireDate(userTokenDTO.getExpireDate())
                .refreshToken(userTokenDTO.getRefreshToken())
                .accessToken(userTokenDTO.getAccessToken())
                .build();

        // when
        when(authService.login(loginRequest)).thenReturn(userTokenDTO);


        // then
        mockMvc.perform(post(ADMIN_CONTROLLER_BASEURL + "/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expireDate").value(authResponse.getExpireDate()))
                .andExpect(jsonPath("$.refreshToken").value(authResponse.getRefreshToken()))
                .andExpect(jsonPath("$.accessToken").value(authResponse.getAccessToken()));

    }
}
