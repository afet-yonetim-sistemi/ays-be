package com.ays.user.service;

import com.ays.BaseServiceTest;
import com.ays.admin_user.controller.dto.request.AdminUserRegisterRequest;
import com.ays.admin_user.service.impl.AdminUserAuthServiceImpl;
import com.ays.auth.controller.dto.request.AysLoginRequest;
import com.ays.auth.model.AysToken;
import com.ays.auth.model.AysUserDetails;
import com.ays.user.controller.payload.request.AdminLoginRequestBuilder;
import com.ays.user.controller.payload.request.AdminRegisterRequestBuilder;
import com.ays.user.model.User;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.model.entity.UserEntityBuilder;
import com.ays.user.model.enums.UserRole;
import com.ays.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthServiceTest extends BaseServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminUserAuthServiceImpl authService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;


    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;


    @Test
    void shouldRegister() {

        // Given
        AdminUserRegisterRequest registerRequest = new AdminRegisterRequestBuilder().build();

        UserEntity userEntity = new UserEntityBuilder()
                .withRegisterRequest(registerRequest, passwordEncoder).build();

        User user = User.builder()
                .username(userEntity.getUsername())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .role(userEntity.getRole())
                .countryCode(userEntity.getCountryCode())
                .lineNumber(userEntity.getLineNumber())
                .status(userEntity.getStatus())
                .email(userEntity.getEmail())
                .lastLoginDate(userEntity.getLastLoginDate())
                .build();

        // when
        //when(organizationRepository.existsById(registerRequest.getOrganizationId())).thenReturn(true);
        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.empty());
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userMapper.mapUserEntityToUser(any(UserEntity.class))).thenReturn(user);

        var registerUser = authService.register(registerRequest);

        // then
        assertEquals(userEntity.getUsername(), user.getUsername());
        assertEquals(userEntity.getFirstName(), user.getFirstName());
        assertEquals(userEntity.getLastName(), user.getLastName());
        assertEquals(userEntity.getRole().ordinal(), user.getRole().ordinal());
        assertEquals(userEntity.getCountryCode(), user.getCountryCode());
        assertEquals(userEntity.getLineNumber(), user.getLineNumber());
        Assertions.assertEquals(userEntity.getStatus(), user.getStatus());
        assertEquals(userEntity.getEmail(), user.getEmail());
        assertEquals(userEntity.getLastLoginDate(), user.getLastLoginDate());
    }

    @Test
    void shouldLogin() {

        // Given
        AysLoginRequest loginRequest = new AdminLoginRequestBuilder().build();


        UserEntity user = UserEntity.builder()
                .id(1L)
                .username(loginRequest.getUsername())
                .password(loginRequest.getPassword())
                .role(UserRole.ROLE_ADMIN)
                .build();

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());


        AysUserDetails aysUserDetails = new AysUserDetails(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(aysUserDetails, null);

        SecurityContextHolder.getContext().setAuthentication(auth);

        AysToken token = AysToken.builder()
                .accessTokenExpiresAt(new Date().getTime() + 120000)
                .refreshToken("refreshToken")
                .accessToken("access-token")
                .build();


        // when
        when(authenticationManager.authenticate(authToken)).thenReturn(auth);
        when(jwtTokenProvider.generateJwtToken(auth)).thenReturn(token);

        AysToken aysToken = authService.authenticate(loginRequest);

        // then
        assertThat(aysToken).isNotNull();
        assertEquals(aysToken.getAccessToken(), token.getAccessToken());
        assertEquals(aysToken.getRefreshToken(), token.getRefreshToken());
        assertEquals(aysToken.getAccessTokenExpiresAt(), token.getAccessTokenExpiresAt());

    }

    @Test
    void shouldRefreshToken() {

        // Given

        String refreshToken = "Refresh token";
        String username = "Admin Username";
        String password = "Admin Password";

        UserEntity user = UserEntity.builder()
                .username(username)
                .password(password)
                .role(UserRole.ROLE_ADMIN)
                .build();

        AysToken aysToken = AysToken.builder()
                .accessTokenExpiresAt(new Date().getTime() + 120000)
                .refreshToken("refreshToken")
                .accessToken("access-token")
                .build();

        AysUserDetails aysUserDetails = new AysUserDetails(user);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(aysUserDetails.getUsername(), aysUserDetails.getPassword());

        // when
        when(jwtTokenProvider.getUserNameFromJwtToken(refreshToken)).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateJwtToken(auth, refreshToken)).thenReturn(aysToken);


        AysToken renewAysToken = authService.refreshAccessToken(refreshToken);

        // then
        assertEquals(aysToken.getAccessToken(), renewAysToken.getAccessToken());
        assertEquals(aysToken.getRefreshToken(), renewAysToken.getRefreshToken());
        assertEquals(aysToken.getAccessTokenExpiresAt(), aysToken.getAccessTokenExpiresAt());
    }

}
