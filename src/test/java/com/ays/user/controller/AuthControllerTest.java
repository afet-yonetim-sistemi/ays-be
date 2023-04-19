//package com.ays.user.controller;
//
//import com.ays.BaseRestControllerTest;
//import com.ays.admin_user.model.dto.request.AdminUserRegisterRequest;
//import com.ays.admin_user.service.AdminUserAuthService;
//import com.ays.auth.controller.dto.request.AysLoginRequest;
//import com.ays.auth.controller.dto.response.AysTokenResponse;
//import com.ays.auth.model.AysToken;
//import com.ays.user.controller.payload.request.AdminLoginRequestBuilder;
//import com.ays.user.controller.payload.request.AdminRegisterRequestBuilder;
//import com.ays.user.model.User;
//import com.ays.user.model.UserBuilder;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockHttpServletRequest;
//
//import java.util.Date;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//class AuthControllerTest extends BaseRestControllerTest {
//
//    @MockBean
//    private AdminUserAuthService adminUserAuthService;
//
//
//    private static final String BASE_PATH = "/api/v1/admin";
//
//    @Test
//    void shouldRegisterForAdmin() throws Exception {
//
//        // Given
//        AdminUserRegisterRequest registerRequest = new AdminRegisterRequestBuilder().build();
//
//        User user = new UserBuilder()
//                .withRegisterRequest(registerRequest).build();
//
//        MessageResponse messageResponse = new MessageResponse("success");
//
//        // when
//        when(adminUserAuthService.register(registerRequest)).thenReturn(user);
//
//        // then - Perform the POST request and assert the response
//        mockMvc.perform(post(ADMIN_CONTROLLER_BASEURL + "/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(registerRequest)))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.message").value(messageResponse.getMessage()));
//
//    }
//
//    @Test
//    void shouldLoginForAdmin() throws Exception {
//
//        // given
//        AysLoginRequest loginRequest = new AdminLoginRequestBuilder().build();
//
//        AysToken aysToken = AysToken.builder()
//                .accessTokenExpiresAt(new Date().getTime() + 120000)
//                .refreshToken("refreshToken")
//                .accessToken("Bearer access-token")
//                .build();
//
//        AysTokenResponse authResponse = AysTokenResponse.builder()
//                .accessToken(aysToken.getAccessToken())
//                .refreshTokenExpiresAt(aysToken.getAccessTokenExpiresAt())
//                .refreshToken(aysToken.getRefreshToken())
//                .build();
//
//        // when
//        when(adminUserAuthService.authenticate(loginRequest)).thenReturn(aysToken);
//
//
//        // then
//        mockMvc.perform(post(ADMIN_CONTROLLER_BASEURL + "/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.accessToken").value(authResponse.getAccessToken()))
//                .andExpect(jsonPath("$.accessTokenExpireIn").value(authResponse.getRefreshTokenExpiresAt()))
//                .andExpect(jsonPath("$.refreshToken").value(authResponse.getRefreshToken()));
//
//    }
//
//    @Test
//    void shouldRefreshTokenForAdmin() throws Exception {
//
//        // given
//
//        AysToken aysToken = AysToken.builder()
//                .accessTokenExpiresAt(new Date().getTime() + 120000)
//                .refreshToken("Bearer refresh-token")
//                .accessToken("Bearer access-token")
//                .build();
//
//        AysTokenResponse authResponse = AysTokenResponse.builder()
//                .accessToken(aysToken.getAccessToken())
//                .refreshTokenExpiresAt(aysToken.getAccessTokenExpiresAt())
//                .refreshToken(aysToken.getRefreshToken())
//                .build();
//
//
//        // when
//
//        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
//        mockHttpServletRequest.addHeader(HttpHeaders.AUTHORIZATION, aysToken.getRefreshToken());
//        String refreshToken = HttpServletRequestWrapper.getJwtToken(mockHttpServletRequest);
//
//        when(adminUserAuthService.refreshAccessToken(refreshToken)).thenReturn(aysToken);
//
//        // then
//        mockMvc.perform(post(ADMIN_CONTROLLER_BASEURL + "/refresh-token")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header(HttpHeaders.AUTHORIZATION, aysToken.getRefreshToken()))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.accessToken").value(authResponse.getAccessToken()))
//                .andExpect(jsonPath("$.accessTokenExpireIn").value(authResponse.getRefreshTokenExpiresAt()))
//                .andExpect(jsonPath("$.refreshToken").value(authResponse.getRefreshToken()));
//    }
//}
