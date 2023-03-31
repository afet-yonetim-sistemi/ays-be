package com.ays.backend.user.service;

import com.ays.backend.base.BaseServiceTest;
import com.ays.backend.mapper.UserMapper;
import com.ays.backend.user.controller.payload.request.AdminLoginRequest;
import com.ays.backend.user.controller.payload.request.AdminLoginRequestBuilder;
import com.ays.backend.user.controller.payload.request.AdminRegisterRequest;
import com.ays.backend.user.controller.payload.request.AdminRegisterRequestBuilder;
import com.ays.backend.user.model.Token;
import com.ays.backend.user.model.User;
import com.ays.backend.user.model.entities.RefreshToken;
import com.ays.backend.user.model.entities.UserEntity;
import com.ays.backend.user.model.entities.UserEntityBuilder;
import com.ays.backend.user.model.enums.UserRole;
import com.ays.backend.user.repository.OrganizationRepository;
import com.ays.backend.user.repository.UserRepository;
import com.ays.backend.user.security.JwtTokenProvider;
import com.ays.backend.user.security.JwtUserDetails;
import com.ays.backend.user.service.impl.AuthServiceImpl;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class AuthServiceTest extends BaseServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrganizationRepository organizationRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;


    @Test
    void shouldRegister() {

        // Given
        AdminRegisterRequest registerRequest = new AdminRegisterRequestBuilder().build();

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
        assertEquals(userEntity.getStatus(), user.getStatus());
        assertEquals(userEntity.getEmail(), user.getEmail());
        assertEquals(userEntity.getLastLoginDate(), user.getLastLoginDate());
    }

    @Test
    void shouldLogin() {

        // Given
        AdminLoginRequest loginRequest = new AdminLoginRequestBuilder().build();


        UserEntity user = UserEntity.builder()
                .username(loginRequest.getUsername())
                .password(loginRequest.getPassword())
                .role(UserRole.ROLE_ADMIN)
                .build();

        user.setId(1L);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());


        JwtUserDetails jwtUserDetails = new JwtUserDetails(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(jwtUserDetails, null);

        SecurityContextHolder.getContext().setAuthentication(auth);

//        JwtUserDetails userDetails = (JwtUserDetails) auth.getPrincipal(); // TODO : unused


        Token token = Token.builder()
                .accessTokenExpireIn(new Date().getTime() + 120000)
                .refreshToken("refreshToken")
                .accessToken("access-token")
                .build();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(token.getRefreshToken())
                .build();

        // when
        when(authenticationManager.authenticate(eq(authToken)))
                .thenReturn(auth);
        when(jwtTokenProvider.generateJwtToken(auth)).thenReturn(token);
        //when(refreshTokenService.createRefreshToken(eq(userDetails.getId()))).thenReturn(refreshToken);

        Token aysToken = authService.login(loginRequest);

        // then
        assertThat(aysToken).isNotNull();
        assertEquals(aysToken.getAccessToken(), token.getAccessToken());
        assertEquals(aysToken.getRefreshToken(), token.getRefreshToken());
        assertEquals(aysToken.getAccessTokenExpireIn(), token.getAccessTokenExpireIn());

    }

    @Test
    void shouldRefreshToken() {

        // Given
        String refreshToken = "Refresh Token";
        String username = "Admin Username";
        String password = "Admin Password";

        UserEntity user = UserEntity.builder()
                .username(username)
                .password(password)
                .role(UserRole.ROLE_ADMIN)
                .build();

        Token token = Token.builder()
                .accessTokenExpireIn(new Date().getTime() + 120000)
                .refreshToken("refreshToken")
                .accessToken("access-token")
                .build();

        JwtUserDetails jwtUserDetails = new JwtUserDetails(user);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(jwtUserDetails.getUsername(), jwtUserDetails.getPassword());

        // when
        when(jwtTokenProvider.getUserNameFromJwtToken(refreshToken)).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateJwtToken(auth)).thenReturn(token);


        Token renewToken = authService.refreshToken(refreshToken);

        // then
        assertEquals(token.getAccessToken(), renewToken.getAccessToken());
        assertEquals(token.getRefreshToken(), renewToken.getRefreshToken());
        assertEquals(token.getAccessTokenExpireIn(), token.getAccessTokenExpireIn());
    }

}
